package com.tokio.pa.cotizadorrc73.commands.resource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.pa.cotizadorrc73.constants.CotizadorRCPortlet73PortletKeys;
import com.tokio.pa.cotizadorModularServices.Bean.DocumentoResponse;
import com.tokio.pa.cotizadorModularServices.Bean.IdCarpetaResponse;
import com.tokio.pa.cotizadorModularServices.Constants.CotizadorModularServiceKey;
import com.tokio.pa.cotizadorModularServices.Interface.CotizadorGenerico;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CotizadorRCPortlet73PortletKeys.COTIZADORRCPORTLET73,
		"mvc.command.name=/cotizadores/coaseguro/guardaDocReaseguro"
	},
	service = MVCResourceCommand.class
)

public class GuardaDocumentosReaseguroResourceCommand extends BaseMVCResourceCommand {
	
	@Reference
	CotizadorGenerico _ServiceGenerico;
	
	@Reference
	private DLAppService _dlAppService;
	
	private static final Log _log = LogFactoryUtil
			.getLog(GuardaDocumentosReaseguroResourceCommand.class);
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		Gson gson = new Gson();
		
		int folio = ParamUtil.getInteger(resourceRequest, "folio");
		int cotizacion = ParamUtil.getInteger(resourceRequest, "cotizacion");
		int version = ParamUtil.getInteger(resourceRequest, "version");
		int ubicacion = ParamUtil.getInteger(resourceRequest, "ubicacion");
		String ramo = ParamUtil.getString(resourceRequest, "ramo");
		int reaseguradora = ParamUtil.getInteger(resourceRequest, "reaseguradora");
		
		boolean savefileURF = ParamUtil.getBoolean(resourceRequest, "savefileURF");
		boolean savefileCobertura = ParamUtil.getBoolean(resourceRequest, "savefileCobertura");
		boolean savefileOtro = ParamUtil.getBoolean(resourceRequest, "savefileOtro");
		
		String fileURFExt = ParamUtil.getString(resourceRequest, "fileURFExt");
		String fileCoberturaExt = ParamUtil.getString(resourceRequest, "fileCoberturaExt");
		String fileOtroExt = ParamUtil.getString(resourceRequest, "fileOtroExt");
		

		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		String p_usuario = user.getScreenName();
		
		try {
			UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(resourceRequest);
			IdCarpetaResponse carpeta = _ServiceGenerico.SeleccionaIdCarpeta(folio,	cotizacion, version);
			JsonArray jsonDocumentos = new JsonArray();
			
			List<Long> documentosEliminar = new ArrayList<Long>();
			if(savefileURF) {
				String nombre= ubicacion + "_" + ramo + "_" + reaseguradora + "_" + "URF";
				armaInfoDocumento(resourceRequest, uploadRequest, "fileURF", nombre,fileURFExt, carpeta.getIdCarpeta(),
						documentosEliminar, jsonDocumentos);
			}
			if(savefileCobertura) {
				String nombre= ubicacion + "_" + ramo + "_" + reaseguradora + "_" + "CARTACOBERTURA";
				armaInfoDocumento(resourceRequest, uploadRequest, "fileCobertura", nombre, fileCoberturaExt, carpeta.getIdCarpeta(),
						documentosEliminar, jsonDocumentos);
			}
			if(savefileOtro) {
				String nombre= ubicacion + "_" + ramo + "_" + reaseguradora + "_" + "OTRO";
				armaInfoDocumento(resourceRequest, uploadRequest, "fileOtro", nombre, fileOtroExt, carpeta.getIdCarpeta(),
						documentosEliminar, jsonDocumentos);
			}
			
			try {
				DocumentoResponse respuesta = _ServiceGenerico.wsDocumentos(0,"I",jsonDocumentos, 1, 
						"DOCREASEGURO", cotizacion, "", p_usuario,CotizadorRCPortlet73PortletKeys.ReaseguroFacultativo);
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(gson.toJson(respuesta));

			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
			}finally {
				for(Long idDoc:documentosEliminar) {
					elimianArchivo(idDoc);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error en el archivo");
		}
	}
	
	void armaInfoDocumento(ResourceRequest resourceRequest,UploadPortletRequest uploadRequest,String atributo,
			String nombre,String extension,int idCarpeta,List<Long> documentosEliminar,JsonArray jsonDocumentos) {
		File file = uploadRequest.getFile(atributo);
		String mimeType = uploadRequest.getContentType(atributo);
		
		JsonObject enviaDocumentos = new JsonObject();
		
		
		enviaDocumentos.addProperty("nombre", nombre);
		enviaDocumentos.addProperty("extension", extension);
		enviaDocumentos.addProperty("idCarpeta", idCarpeta);
		enviaDocumentos.addProperty("idDocumento", "0");
		enviaDocumentos.addProperty("idCatalogoDetalle",7669);

		Map<String, Object> info = null;
		
		info = guardaDocumentos(resourceRequest, file, nombre, mimeType,extension);
		documentosEliminar.add((Long)info.get("idDoc"));
		
		enviaDocumentos.addProperty("documento", "");
		enviaDocumentos.addProperty("url", (String) info.get("url"));
		enviaDocumentos.addProperty("leer", 1);
		
		
		jsonDocumentos.add(enviaDocumentos);
		System.out.println("json :" + enviaDocumentos.toString());
	}
	
	Map<String, Object> guardaDocumentos(ResourceRequest resourceRequest, File file, String nombre, String mimeType, String ext){
		try {
			
			Map<String, Object> respuesta = new HashMap<String, Object>();

			long idGroup = PortalUtil.getScopeGroupId(resourceRequest);
			ServiceContext serviceContext = ServiceContextFactory.getInstance(DLFolder.class.getName(), resourceRequest);
			
			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			
			User user = (User) resourceRequest.getAttribute(WebKeys.USER);
			
			String cotizacion = ParamUtil.getString(resourceRequest, "cotizacion");
			String version = ParamUtil.getString(resourceRequest, "version");
			String folio = ParamUtil.getString(resourceRequest, "folio");
			String urlOrigin = ParamUtil.getString(resourceRequest, "urlOrigin");


			System.out.println("------------------------urlOrigin : " + urlOrigin );
			String aux2 = user.getScreenName() + "-" + nombre + "-F_" + folio + "-C_" + cotizacion + "-V_" + version;
			
			DLFolder fCotizadores = DLFolderLocalServiceUtil.getFolder(idGroup, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					"Documentos_Cotizadores");
			
			
			FileEntry fileEntry = _dlAppService.addFileEntry(idGroup, fCotizadores.getFolderId(), nombre + "." + ext,
					mimeType, nombre + "." + ext, aux2, "hi", file, serviceContext);
			
			
			String urlDoc = urlOrigin + "/documents/" + idGroup + "/" + fileEntry.getFolderId() + "/" + fileEntry.getFileName()
			+ "/" + fileEntry.getUuid();
			
			System.out.println(urlDoc);
			
			_log.debug("--------------------------> documento:" + urlDoc);
			
			respuesta.put("url", urlDoc); 
			respuesta.put("idDoc", fileEntry.getFileEntryId()); 
			return respuesta;
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	void elimianArchivo(long idDoc){
		try {
			_dlAppService.deleteFileEntry(idDoc);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
