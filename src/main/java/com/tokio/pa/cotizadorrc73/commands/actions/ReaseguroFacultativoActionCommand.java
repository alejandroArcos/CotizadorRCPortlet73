package com.tokio.pa.cotizadorrc73.commands.actions;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.PersonaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.RamoReaseguro;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorReaseguro;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;

import java.util.ArrayList;
import java.util.Comparator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"javax.portlet.name="+ CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
		"mvc.command.name=/cotizadores/reaseguro/reaseguroFacultativo"
	},
	service = MVCActionCommand.class
)

public class ReaseguroFacultativoActionCommand extends BaseMVCActionCommand {
	
	@Reference
	CotizadorGenerico _CMServicesGenerico;
	
	@Reference
	CotizadorPaso1 _CMServicesP1;
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		
		Gson gson = new Gson();
		
		HttpServletRequest originalRequest = PortalUtil
				.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));
		
		User user = (User) actionRequest.getAttribute(WebKeys.USER);
		int idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
		
		String ramo = ParamUtil.getString(actionRequest, "ramo");
		String nombreRamo = ParamUtil.getString(actionRequest, "nombreRamo");
		String responseMigracionString = ParamUtil.getString(actionRequest, "reaseguroResponse");
		String folio = ParamUtil.getString(actionRequest, "folio");
		int cotizacion = ParamUtil.getInteger(actionRequest, "cotizacion");
		int version = ParamUtil.getInteger(actionRequest, "version");
		String infoCotizacion = ParamUtil.getString(actionRequest, "infoCotJson");
		String infoCotiUrl = ParamUtil.getString(actionRequest, "infoCotizacion");
		double valorAsegurado = ParamUtil.getDouble(actionRequest, "valorAseguradoFacultativo");
		double porcentaje = ParamUtil.getDouble(actionRequest, "porcentaje");
		
		ReaseguroResponse responseMigracion = gson.fromJson(responseMigracionString,
				ReaseguroResponse.class);
		
		RamoReaseguro ramoReaseguro = new RamoReaseguro();
		ramoReaseguro.setRamo(ramo);
		ramoReaseguro.setNombreRamo(nombreRamo);
		
		ArrayList<RamoReaseguro> ramosReaseguro = new ArrayList<RamoReaseguro>();
		ramosReaseguro.add(ramoReaseguro);
		
		responseMigracion.setRamosReaseguro(ramosReaseguro);
		
		ReaseguroResponse response = _CMServicesReaseguro
				.consultaFacultativoReaseguro(cotizacion, version,
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
		
		PersonaResponse reaseguradores = _CMServicesP1.getListaPersonas("", 6,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		PersonaResponse conducto = _CMServicesP1.getListaPersonas("", 5,
				user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
		
		ListaRegistro listaCatMoneda = fGetCatalogos(
				CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
				CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
				CotizadorModularServiceKey.LIST_CAT_MONEDA,
				CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
				CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73, actionRequest);
		
		actionRequest.setAttribute("reaseguroFacultativo", response);
		actionRequest.setAttribute("reaseguroFacultativoJson", gson.toJson(response));
		actionRequest.setAttribute("reaseguroResponseJson", gson.toJson(responseMigracion));
		actionRequest.setAttribute("reaseguroResponse", responseMigracion);
		actionRequest.setAttribute("reaseguradores", reaseguradores.getPersonas());
		actionRequest.setAttribute("conducto", conducto.getPersonas());
		actionRequest.setAttribute("listaCatMoneda", listaCatMoneda.getLista());
		actionRequest.setAttribute("infoCotJson", infoCotizacion);
		actionRequest.setAttribute("infCoti", infoCotiUrl);
		actionRequest.setAttribute("valorAsegurado", valorAsegurado);
		actionRequest.setAttribute("porcentaje", porcentaje);
		actionRequest.setAttribute("ramo", ramo);
		actionRequest.setAttribute("nombreRamo", nombreRamo);
		actionRequest.setAttribute("moneda", response.getMoneda());
		
		actionResponse.setRenderParameter("jspPage", "/pantallas/reaseguroFacultativo.jsp");
	}
	
	private ListaRegistro fGetCatalogos(int p_rownum, String p_tiptransaccion, String p_codigo,
			int p_activo, String p_usuario, String p_pantalla, ActionRequest actionRequest) {
		try {
			ListaRegistro lr = _CMServicesGenerico.getCatalogo(p_rownum, p_tiptransaccion, p_codigo,
					p_activo, p_usuario, p_pantalla);

			lr.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return lr;
		} catch (Exception e) {
			System.err.print("----------------- error en traer los catalogos");
			e.printStackTrace();
			SessionErrors.add(actionRequest, "errorConocido");
			actionRequest.setAttribute("errorMsg", "Error en catalogos");
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			return null;
		}
	}

}
