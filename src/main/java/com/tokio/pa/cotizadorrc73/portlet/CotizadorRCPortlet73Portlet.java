package com.tokio.pa.cotizadorrc73.portlet;

import com.tokio.pa.cotizadorModularServices.Bean.Cliente;
import com.tokio.pa.cotizadorModularServices.Bean.Coasegurador;
import com.tokio.pa.cotizadorModularServices.Bean.CoaseguradorResponse;
import com.tokio.pa.cotizadorModularServices.Bean.CotizadorDataResponse;
import com.tokio.pa.cotizadorModularServices.Bean.DatoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.InfoCotizacion;
import com.tokio.pa.cotizadorModularServices.Bean.ListaRegistro;
import com.tokio.pa.cotizadorModularServices.Bean.PersonaResponse;
import com.tokio.pa.cotizadorModularServices.Bean.ReaseguroResponse;
import com.tokio.pa.cotizadorModularServices.Bean.Registro;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorCoaseguro;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorPaso1;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorReaseguro;
import com.tokio.pa.cotizadorModularServices.Util.CotizadorModularUtil;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;

import java.io.IOException;
import java.util.Comparator;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author urielfloresvaldovinos
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=CotizadorRCPortlet73",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.requires-namespaced-parameters=false",
		"com.liferay.portlet.private-request-attributes=false"
	},
	service = Portlet.class
)
public class CotizadorRCPortlet73Portlet extends MVCPortlet {
	
private static final Log _log = LogFactoryUtil.getLog(CotizadorRCPortlet73Portlet.class);
	
	
	@Reference
	CotizadorPaso1 _CMServicesP1;
	
	@Reference
	CotizadorGenerico _CMServicesGenerico;
	
	@Reference
	CotizadorCoaseguro _CMServicesCoaseguro;
	
	@Reference
	CotizadorReaseguro _CMServicesReaseguro;
	
	
	long modoVista = 0;
	InfoCotizacion infCotizacion;
	User user;
	int idPerfilUser;
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {
		
		Gson gson = new Gson();
		
		llenaInfoCotizacion(renderRequest, renderResponse);
		
		cargaInformacionPantalla(renderRequest);
		
		renderRequest.setAttribute("infCotizacion", infCotizacion);
		renderRequest.setAttribute("infoCotJson", gson.toJson(infCotizacion));
		renderRequest.setAttribute("infCoti", CotizadorModularUtil.encodeURL(infCotizacion));
		
		super.render(renderRequest, renderResponse);
	}
	
	
	private void llenaInfoCotizacion(RenderRequest renderRequest, RenderResponse renderResponse) {

		try {
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));

			user = (User) renderRequest.getAttribute(WebKeys.USER);
			idPerfilUser = (int) originalRequest.getSession().getAttribute("idPerfil");
			
			String inf = originalRequest.getParameter("infoCotizacion");
			
			infCotizacion = CotizadorModularUtil.decodeURL(inf);
			
			_log.info(infCotizacion.toString());
			
			renderRequest.setAttribute("modoVista", infCotizacion.getRc());
		}
		catch(Exception e) {
			// TODO: handle exception
			System.err.println("------------------ llenaInfoCotizacion:");
			e.printStackTrace();
		}
	}
	
	private void cargaInformacionPantalla(RenderRequest renderRequest) {
		
		switch(infCotizacion.getRc()) {
			case 1:
				try {
					PersonaResponse respuesta = _CMServicesP1.getListaPersonas("", 4,
						user.getScreenName(), CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
					
					CotizadorDataResponse cotizadorData = _CMServicesP1
							.getCotizadorData(infCotizacion.getFolio(), infCotizacion.getCotizacion(),
									infCotizacion.getVersion(), user.getScreenName(),
									CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
					
					DatoCotizacion cotizacion = cotizadorData.getDatosCotizacion();
					
					Cliente cliente = cotizacion.getDatosCliente();
					
					ListaRegistro listaCatCoaseguro = fGetCatalogos(
							CotizadorModularServiceKey.TMX_CTE_ROW_TODOS,
							CotizadorModularServiceKey.TMX_CTE_TRANSACCION_GET,
							CotizadorModularServiceKey.LIST_CAT_COASEGURO,
							CotizadorModularServiceKey.TMX_CTE_CAT_ACTIVOS, user.getScreenName(),
							CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73, renderRequest);
					
					for(Registro registro : listaCatCoaseguro.getLista()) {
						
						if(registro.getIdCatalogoDetalle() == infCotizacion.getTipoCoaseguro()) {
							renderRequest.setAttribute("tipoCoaseguro", registro.getValor());
						}
					}
					
					CoaseguradorResponse response = _CMServicesCoaseguro
							.consultaCoaseguradores((int) infCotizacion.getCotizacion(),
									infCotizacion.getVersion(), idPerfilUser, user.getScreenName(),
									CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
					
					if(response.getP_coaseguradores() != null) {
						response.getP_coaseguradores()
							.sort(Comparator.comparing(Coasegurador::getP_orden));
					}
					
					renderRequest.setAttribute("estado", cotizacion.getIdEstado());
					renderRequest.setAttribute("coaseguradores", response.getP_coaseguradores());
					renderRequest.setAttribute("datosCliente", cliente);
					renderRequest.setAttribute("listaPersonas", respuesta.getPersonas());
					renderRequest.setAttribute("documentos", response.getP_docreaseguro());
				}
				catch(Exception e) {
					_log.error(e.getMessage());
					
					e.printStackTrace();
				}
			
				break;
			case 2:
				
				try {
					
					ReaseguroResponse responseMigracion = _CMServicesReaseguro.migrarCotizacion(
							(int)infCotizacion.getCotizacion(), infCotizacion.getVersion(),
							""+infCotizacion.getFolio(), 207, idPerfilUser, user.getScreenName(),
							CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
					
					
					if(responseMigracion.getCode() == 0) {
						ReaseguroResponse responseConsulta = _CMServicesReaseguro
								.consultaColocacionReaseguro((int)infCotizacion.getCotizacion(), 
										infCotizacion.getVersion(),
										responseMigracion.getFolio(),
										responseMigracion.getPoliza(),
										responseMigracion.getCertificado(),
										responseMigracion.getFolio(),
										"",
										responseMigracion.getPaquete(),
										207,
										1,
										1,
										"",
										idPerfilUser,
										user.getScreenName(),
										CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
						
						if(responseConsulta.getCode() == 0) {
							
							Gson gson = new Gson();
							
							responseConsulta.setCertificado(responseMigracion.getCertificado());
							responseConsulta.setPoliza(responseMigracion.getPoliza());
							responseConsulta.setPaquete(responseMigracion.getPaquete());
							responseConsulta.setFolio(responseMigracion.getFolio());
							
							CotizadorDataResponse cotizadorData = _CMServicesP1
									.getCotizadorData(infCotizacion.getFolio(), infCotizacion.getCotizacion(),
											infCotizacion.getVersion(), user.getScreenName(),
											CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73);
							
							DatoCotizacion cotizacion = cotizadorData.getDatosCotizacion();
							
							Cliente cliente = cotizacion.getDatosCliente();
							
							final PortletSession psession = renderRequest.getPortletSession();
							
							String auxNombre = "LIFERAY_SHARED_F=" + infCotizacion.getFolio() + "_C="
									+ infCotizacion.getCotizacion() + "_V=" + infCotizacion.getVersion()
									+ "_VOBOREASEGURO";

							System.out.println("auxNombre" + auxNombre);
							
							boolean voboReaseguro = false;
							
							try {
								voboReaseguro = (boolean) psession.getAttribute(auxNombre,
									PortletSession.APPLICATION_SCOPE);
							}
							catch(Exception e) {
							}
							
							renderRequest.setAttribute("voboReaseguro", voboReaseguro);
							renderRequest.setAttribute("datosCliente", cliente);
							renderRequest.setAttribute("estado", cotizacion.getIdEstado());
							renderRequest.setAttribute("ubicaciones", responseMigracion.getNumeroUbicaciones());
							renderRequest.setAttribute("reaseguro", responseConsulta);
							renderRequest.setAttribute("reaseguroJson", gson.toJson(responseConsulta));
						}
						else {
							
						}
					}
					else {
						
					}
				}
				catch(Exception e) {
					_log.error(e.getMessage());
					
					e.printStackTrace();
				}
				break;
		}
	}
	
	private ListaRegistro fGetCatalogos(int p_rownum, String p_tiptransaccion, String p_codigo,
			int p_activo, String p_usuario, String p_pantalla, RenderRequest renderRequest) {
		try {
			ListaRegistro lr = _CMServicesGenerico.getCatalogo(p_rownum, p_tiptransaccion, p_codigo,
					p_activo, p_usuario, p_pantalla);

			lr.getLista().sort(Comparator.comparing(Registro::getDescripcion));
			return lr;
		} catch (Exception e) {
			System.err.print("----------------- error en traer los catalogos");
			e.printStackTrace();
			SessionErrors.add(renderRequest, "errorConocido");
			renderRequest.setAttribute("errorMsg", "Error en catalogos");
			SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest)
					+ SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			return null;
		}
	}
}