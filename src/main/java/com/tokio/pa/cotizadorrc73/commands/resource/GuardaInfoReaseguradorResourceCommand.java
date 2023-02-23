package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorModularServices.Bean.Persona;
import com.tokio.pa.cotizadorModularServices.Bean.PersonaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroFacultativo;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorReaseguro;

import java.io.PrintWriter;

import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
			"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
			"mvc.command.name=/cotizadores/reaseguro/guardaInfoFacultativo"
		},
		service = MVCResourceCommand.class
	)

public class GuardaInfoReaseguradorResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;
	
	@Reference
	CotizadorPaso1 _CMServicesP1;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		String ramo = ParamUtil.getString(resourceRequest, "ramo");
		String reaseguroFacultativoString = ParamUtil.getString(resourceRequest,
				"reaseguroFacultativo"); 
		String responseMigracionString = ParamUtil.getString(resourceRequest, "responseMigracion");
		String folio = ParamUtil.getString(resourceRequest, "folio");
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		
		ReaseguroFacultativo reaseguroFacultativo = gson.fromJson(reaseguroFacultativoString,
				ReaseguroFacultativo.class);
		
		ReaseguroResponse responseMigracion = gson.fromJson(responseMigracionString, 
				ReaseguroResponse.class);
		
		PersonaResponse reaseguradores = _CMServicesP1.getListaPersonas("", 6,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PersonaResponse conductos = _CMServicesP1.getListaPersonas("", 5,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		for(Persona p : reaseguradores.getPersonas()) {
			if(reaseguroFacultativo.getCompania().equals(String.valueOf(p.getIdPersona()))) {
				reaseguroFacultativo.setCompania(p.getCodigo());
				break;
			}
		}
		
		for(Persona p : conductos.getPersonas()) {
			if(reaseguroFacultativo.getConducto().equals(String.valueOf(p.getIdPersona()))) {
				reaseguroFacultativo.setConducto(p.getCodigo());
				break;
			}
		}
		
		
		ReaseguroResponse responseRegFac = _CMServicesReaseguro.guardarFacultativo(cotizacion,
				version, folio, responseMigracion.getPoliza(), responseMigracion.getCertificado(),
				responseMigracion.getFolio(), reaseguroFacultativo.getCompania(),
				responseMigracion.getPaquete(), 207, 1, 1, ramo, idPerfilUser, user.getScreenName(),
				CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73, reaseguroFacultativo);
		
		generaVarSesion(resourceRequest, folio, cotizacion, version);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(responseRegFac));
	}
	
	private void generaVarSesion(ResourceRequest resourceRequest, String folio, int cotizacion,
			int version) {
		
		final PortletSession psession = resourceRequest.getPortletSession();
		
		String voboReaseguro = "LIFERAY_SHARED_F=" + folio +
				"_C=" + cotizacion +
				"_V=" + version +
				"_VOBOREASEGURO";
		
		psession.setAttribute(voboReaseguro, true, PortletSession.APPLICATION_SCOPE);
	}

}
