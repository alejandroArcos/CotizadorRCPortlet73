package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorModularServices.Bean.CoaseguradorResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorCoaseguro;

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
		"mvc.command.name=/cotizadores/coaseguro/guardaInfoCoaseguro"
	},
	service = MVCResourceCommand.class
)

public class GuardaInformacionCoaseguroResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorCoaseguro _cotizadorCoaseguro;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		Gson gson = new Gson();
		
		int p_cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int p_version = ParamUtil.getInteger(resourceRequest, "version");
		int p_idPerfil = (int) originalRequest.getSession().getAttribute("idPerfil");
		String auxCoaseguradores = ParamUtil.getString(resourceRequest, "coaseguradores");
		JsonArray p_coaseguradores = gson.fromJson(auxCoaseguradores, JsonArray.class);
		String p_usuario = user.getScreenName();
		
		
		CoaseguradorResponse response = _cotizadorCoaseguro.guardaCoaseguradores(p_cotizacion,
				p_version, p_idPerfil, p_coaseguradores, p_usuario,
				CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(response));
	}
}
