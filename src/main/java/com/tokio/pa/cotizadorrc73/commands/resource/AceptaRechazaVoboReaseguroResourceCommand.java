package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorrc73.util.SendMail;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Enum.ModoCotizacion;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorReaseguro;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate = true,
		property = {
			"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
			"mvc.command.name=/cotizadores/reaseguro/voboReaseguro"
		},
		service = MVCResourceCommand.class
	)

public class AceptaRechazaVoboReaseguroResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		String infCotizacionString = ParamUtil.getString(resourceRequest, "infCotizacion");
		int estatus = ParamUtil.getInteger(resourceRequest, "estatus");
		String sitio = ParamUtil.getString(resourceRequest, "sitio");
		String comentarios = ParamUtil.getString(resourceRequest, "comentarios");
		
		InfoCotizacion infCotizacion = gson.fromJson(infCotizacionString, InfoCotizacion.class);
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		int idPerfil = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		ReaseguroResponse responseVobo = _CMServicesReaseguro.voboReaseguro(
				(int)infCotizacion.getCotizacion(), infCotizacion.getVersion(),
				(int)infCotizacion.getFolio(), idPerfil, estatus == 0 ? 310 : 340, user.getScreenName(),
				CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		String link = "";
		
		switch(infCotizacion.getTipoCotizacion()) {
		
			case RC:
				link = sitio + "/group/portal-agentes/cotizador-rc";
				break;
			case EMPRESARIAL:
				link = sitio + "/group/portal-agentes/property-quotation";
				break;
			case TRANSPORTES:
				link = sitio + "/group/portal-agentes/cotizador-transportes";
				break;
			default:
				link = sitio;
				break;
		}
		
		if(responseVobo.getCode() == 0) {
			
			String subject = "";
			String body = "";
			String[] listMails = {"uriel.flores@globalquark.com.mx", 
					"ricardo_sanchez@tokiomarine.com.mx",
					"maricruz_jimenez@tokiomarine.com.mx"};
			
			if(estatus == 0) {
			
				subject = "Rechazo Vo Bo Reaseguro Facultativo";
				body =	"<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /> </head>   <body>     <header></header>     <section>       <p>         El analista de Reaseguro ha rechazado la colocaci&oacute;n de Reaseguro facultativo de la cotizaci&oacute;n   ${folio}  para continuar con el proceso ingrese nuevamente a la cotizaci&oacute;n. </p>"
						+ "<p>Comentarios: " + comentarios + "</p>"
						+ "<p>Link directo al folio: ${link}</p><p>Le enviamos un cordial saludo</p></section><footer>       <img         src='https://preview.ibb.co/i3vFWp/Firma_Correo_Tokio_Marine.png'         alt='Firma Correo Tokio Marine'         width='35%'       />     </footer>   </body> </html> ";
			
				infCotizacion.setModo(ModoCotizacion.EDICION);
			}
			else {
				subject = "Vo Bo Reaseguro Facultativo";
				body =	"<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /> </head>   <body>     <header></header>     <section>       <p>         El analista de Reaseguro ha validado la colocaci&oacute;n de Reaseguro facultativo de la cotizaci&oacute;n  ${folio} para continuar con el proceso ingrese nuevamente a la cotizaci&oacute;n.</p>"
						+ "<p>Link directo al folio: ${link}</p><p>Le enviamos un cordial saludo</p></section><footer>       <img         src='https://preview.ibb.co/i3vFWp/Firma_Correo_Tokio_Marine.png'         alt='Firma Correo Tokio Marine'         width='35%'       />     </footer>   </body> </html> ";
				
				infCotizacion.setModo(ModoCotizacion.CONSULTA);
			}
			
			link += "?infoCotizacion=" + CotizadorModularUtil
					.encodeURL(infCotizacion);
			
			body = StringUtil.replace(body,
					new String[] { "${folio}", "${link}" },
					new String[] { infCotizacion.getFolio()+"", link});
			
			new SendMail().SendMAil(listMails, body,  subject);
		}
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(responseVobo));
	}

}
