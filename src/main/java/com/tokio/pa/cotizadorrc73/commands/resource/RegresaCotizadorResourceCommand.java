package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;

import java.io.PrintWriter;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
		"mvc.command.name=/cotizadores/coaseguro/regresaCotizador"
	},
	service = MVCResourceCommand.class
)

public class RegresaCotizadorResourceCommand extends BaseMVCResourceCommand {
	
	InfoCotizacion infCotizacion;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		String infCotString = ParamUtil.getString(resourceRequest, "infCoti");
		
		String urlCotizador = "";
		
		infCotizacion = CotizadorModularUtil.decodeURL(infCotString);
		
		switch(infCotizacion.getTipoCotizacion()) {
			case TRANSPORTES:
				urlCotizador = "cotizador-transportes?infoCotizacion";
				break;
			case EMPRESARIAL:
				urlCotizador = "property-quotation?infoCotizacion";
				break;
			case RC:
				urlCotizador = "cotizador-rc?infoCotizacion";
				break;
			default:
				urlCotizador = "consulta-cotizaciones?infoCotizacion";
				break;
		}
		
		JsonObject response = new JsonObject();
		
		Gson gson = new Gson();
		
		response.addProperty("url", urlCotizador);
		response.addProperty("infoCot", infCotString);
		
		PrintWriter writer = resourceResponse.getWriter();
		writer.write(gson.toJson(response));
	}

}
