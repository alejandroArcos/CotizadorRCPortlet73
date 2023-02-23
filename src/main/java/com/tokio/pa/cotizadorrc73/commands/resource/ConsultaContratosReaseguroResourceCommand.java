package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorReaseguro;

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
			"mvc.command.name=/cotizadores/reaseguro/getContratosReaseguro"
		},
		service = MVCResourceCommand.class
	)

public class ConsultaContratosReaseguroResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;

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
		
		ReaseguroResponse responseMigracion = gson.fromJson(responseMigracionString,
				ReaseguroResponse.class);
		
		ReaseguroResponse responseContratos = _CMServicesReaseguro
				.consultaContratosReaseguro(cotizacion, 
						version,
						folio,
						responseMigracion.getPoliza(),
						responseMigracion.getCertificado(),
						responseMigracion.getFolio(),
						"",
						responseMigracion.getPaquete(),
						207,
						1,
						1,
						ramo,
						idPerfilUser,
						user.getScreenName(),
						CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(responseContratos));
	}
	

}
