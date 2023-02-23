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
			"mvc.command.name=/cotizadores/reaseguro/solicitarVobo"
		},
		service = MVCResourceCommand.class
	)

public class SolicitaVoboReaseguroResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		String infCotizacionString = ParamUtil.getString(resourceRequest, "infCotizacion");
		String sitio = ParamUtil.getString(resourceRequest, "sitio");
		
		InfoCotizacion infCotizacion = gson.fromJson(infCotizacionString, InfoCotizacion.class);
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		int idPerfil = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		infCotizacion.setModo(ModoCotizacion.VOBO_REASEGURO);
		
		String link = CotizadorModularUtil.encodeURL(infCotizacion);
		
		/*
		switch(infCotizacion.getTipoCotizacion()) {
		
			case RC:
				link = "http://172.25.10.44:8080/group/portal-agentes/cotizador-rc?infoCotizacion="
						+ link;
				break;
			case EMPRESARIAL:
				break;
			case FAMILIAR:
				break;
			case TRANSPORTES:
				break;
			default:
				break;
		}
		*/
		
		link = sitio + "?infoCotizacion="
				+ link;
		
		ReaseguroResponse responseVobo = _CMServicesReaseguro.voboReaseguro(
				(int)infCotizacion.getCotizacion(), infCotizacion.getVersion(),
				(int)infCotizacion.getFolio(), idPerfil, 362, user.getScreenName(),
				CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		if(responseVobo.getCode() == 0) {
			
			String subject = "Solicitud de Vo Bo Reaseguro Facultativo";
			String body = "<!DOCTYPE html> <html>   <head>  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /> </head>   <body>     <header></header>     <section>       "
					+ "<p>         Se le ha asignado la cotizaci&oacute;n  ${folio} la cual requiere de su atenci&oacute;n para validar la colocaci&oacute;n de Reaseguro Facultativo.    </p>  "
					+ "<p>         Link directo al folio: ${link}       </p>       <p>Le enviamos un cordial saludo</p>     </section>     <footer>       <img         src='https://preview.ibb.co/i3vFWp/Firma_Correo_Tokio_Marine.png'         alt='Firma Correo Tokio Marine'         width='35%'       />     </footer>   </body> </html> ";
			
			body = StringUtil.replace(body,
					new String[] { "${folio}", "${link}" },
					new String[] { infCotizacion.getFolio()+"", link});
			
			String[] listMails = {"uriel.flores@globalquark.com.mx", 
					"ricardo_sanchez@tokiomarine.com.mx",
					"maricruz_jimenez@tokiomarine.com.mx"};
			
			
			new SendMail().SendMAil(listMails, body,  subject);
		}
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(responseVobo));
	}

}
