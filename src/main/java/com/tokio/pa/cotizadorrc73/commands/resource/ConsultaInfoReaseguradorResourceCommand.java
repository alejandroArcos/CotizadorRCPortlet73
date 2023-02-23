package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import  com.tokio.pa.cotizadorModularServices.Bean.Documento;
import com.tokio.pa.cotizadorModularServices.Bean.DocumentoResponse;
import com.tokio.pa.cotizadorModularServices.Bean.IdCarpetaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.Persona;
import com.tokio.pa.cotizadorModularServices.Bean.PersonaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorReaseguro;

import java.io.PrintWriter;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
			"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
			"mvc.command.name=/cotizadores/reaseguro/getInfoReasegurador"
		},
		service = MVCResourceCommand.class
	)

public class ConsultaInfoReaseguradorResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;
	
	@Reference
	CotizadorPaso1 _CMServicesP1;

	@Reference
	CotizadorGenerico _ServiceGenerico;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		String ramo = ParamUtil.getString(resourceRequest, "ramo");
		String responseMigracionString = ParamUtil.getString(resourceRequest, "responseMigracion");
		String folio = ParamUtil.getString(resourceRequest, "folio");
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		String codigoReasegurador = ParamUtil.getString(resourceRequest, "codigoReasegurador");
		int ubicacion = 1;
		
		ReaseguroResponse responseMigracion = gson.fromJson(responseMigracionString,
				ReaseguroResponse.class);
		
		ReaseguroResponse responseReasegurador = _CMServicesReaseguro
				.consultaRegFacultativoReaseguro(cotizacion, 
						version,
						folio,
						responseMigracion.getPoliza(),
						responseMigracion.getCertificado(),
						responseMigracion.getFolio(),
						codigoReasegurador,
						responseMigracion.getPaquete(),
						207,
						1,
						1,
						ramo,
						idPerfilUser,
						user.getScreenName(),
						CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PersonaResponse reaseguradores = _CMServicesP1.getListaPersonas("", 6,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PersonaResponse conductos = _CMServicesP1.getListaPersonas("", 5,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		int reaseguradora=0;
		for(Persona p : reaseguradores.getPersonas()) {
			if(p.getCodigo().equals(responseReasegurador.getRegistro().getCompania())) {
				responseReasegurador.getRegistro().setCompania(String.valueOf(p.getIdPersona()));
				reaseguradora=p.getIdPersona();
				break;
			}
		}
		
		for(Persona p : conductos.getPersonas()) {
			if(p.getCodigo().equals(responseReasegurador.getRegistro().getConducto())) {
				responseReasegurador.getRegistro().setConducto(String.valueOf(p.getIdPersona()));
				break;
			}
		}
		
		//Consulta de Documentos:
		JsonArray jsonDocumentos = new JsonArray();
		JsonObject enviaDocumentos = new JsonObject();
		
		IdCarpetaResponse carpeta = _ServiceGenerico.SeleccionaIdCarpeta(Integer.parseInt(folio),	cotizacion, version);
		enviaDocumentos.addProperty("idCarpeta", carpeta.getIdCarpeta());
		enviaDocumentos.addProperty("idCatalogoDetalle",7669);
		jsonDocumentos.add(enviaDocumentos);
		
		DocumentoResponse respuesta = _ServiceGenerico.wsDocumentos(0,"B",jsonDocumentos, 1, 
				"DOCREASEGURO", cotizacion, "", user.getScreenName(),CotizadorRCPortlet73PortletKeys.ReaseguroFacultativo);
		
		String fileURF= ubicacion + "_" + ramo + "_" + reaseguradora + "_" + "URF";
		String fileCobertura= ubicacion + "_" + ramo + "_" + reaseguradora + "_" + "CARTACOBERTURA";
		String fileOtro= ubicacion + "_" + ramo + "_" + reaseguradora + "_" + "OTRO";
		
		for(Documento d:respuesta.getLista()) {
			
			if(d.getNombre().equals(fileURF)) {
				responseReasegurador.setFileURF(d.getDocumento());
				responseReasegurador.setFileURFName(d.getNombre()+"."+d.getExtension());
			}
			if(d.getNombre().equals(fileCobertura)) {
				responseReasegurador.setFileCobertura(d.getDocumento());
				responseReasegurador.setFileCoberturaName(d.getNombre()+"."+d.getExtension());
			}
			if(d.getNombre().equals(fileOtro)) {
				responseReasegurador.setFileOtro(d.getDocumento());
				responseReasegurador.setFileOtroName(d.getNombre()+"."+d.getExtension());
			}
			
		}
		
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(responseReasegurador));
		
	}

}
