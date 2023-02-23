package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorModularServices.Bean.PersonaResponse;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
		"mvc.command.name=/cotizadores/coaseguro/getCoaseguradores"
	},
	service = MVCResourceCommand.class
)

public class GetCoaseguradoresResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorPaso1 _CMServicesP1;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		
		Gson gson = new Gson();
		
		PersonaResponse respuesta = _CMServicesP1.getListaPersonas("", 4,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(respuesta));
	}
	

}
