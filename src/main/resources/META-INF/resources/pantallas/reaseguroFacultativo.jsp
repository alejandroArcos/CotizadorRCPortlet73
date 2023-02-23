<%@ include file="../init.jsp"%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reaseguroFacultativo.css?v=${version}">

<portlet:resourceURL id="/cotizadores/reaseguro/guardaInfoFacultativo" var="guardaInfoFacultativoURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/getInfoReasegurador" var="getInfoReaseguradorURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/getReaseguradores" var="getReaseguradoresURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/eliminaReasegurador" var="eliminaReaseguradorURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/coaseguro/guardaDocReaseguro" var="guardaDocReaseguroURL" cacheability="FULL" />

<style>
	.site-wrapper #coti-RC .table-striped tbody tr.selected {
		background-color: pink; 
	}
</style>

<fmt:setLocale value="es_MX" />

<section id="coti-RC" class="upper-case-all">
	
	<div class="section-heading">
		<div class="container-fluid">
			<h4 class="title text-left"><liferay-ui:message key="CotizadorRCPortlet.titFacultativo" /></h4> 
		</div>
	</div>
	
	<div class="container-fluid" id="headerFacultativo">
		<div class="row divFolio">
			<div class="col-md-9 mt-4">
				${datosCliente.nombre} ${datosCliente.appPaterno} ${datosCliente.appMaterno}
			</div>
			<div class="col-md-3" style="text-align: right;">
				<div class="md-form form-group">
					<input id="txtFolioP1" type="text" name="txtFolioP1" class="form-control" value="${infCotizacion.folio} - ${infCotizacion.version}"  disabled>
					<label class="active" for="txtFolioP1">
						<liferay-ui:message key="CotizadorRCPortlet.titFolio" />
					</label>
				</div>
			</div>
		</div>
	</div>
	<div class="container" id="divFacultativo">
		<div class="row mt-5">
            <div class="table-wrapper col-md-12">
            	<table class="table table-striped table-bordered" style="width:100%;" id="table2">
            		<thead>
						<tr>
<!-- 							<th class="th_interm">Intermediario</th> -->
							<th>Reasegurador</th>
							<th>Estado</th>
							<th><liferay-ui:message key="CotizadorRCPortlet.camParticipacion" /></th>
							<th>Valor Reasegurador</th>
							<th>Prima Cedida</th>
							<th><liferay-ui:message key="CotizadorRCPortlet.fechaCreacion" /></th>
							<th>Usuario</th>
						</tr>
					</thead>
					<tbody>	
						<c:set var="auxPorcen" value="0" />
						<c:forEach items="${reaseguroFacultativo.reaseguradores}" var="entry">
							<tr class="rowReasegurador">							
<%-- 								<td id="${entry.codigoReasegurador}" class='codigoReasegurador'>${entry.nombreReasegurador}</td> --%>
								<td id="${entry.codigoReasegurador}" class='codigoReasegurador'>${entry.nombreReasegurador}</td>
								<td>${entry.estado}</td>
								<td class="porcentajePart">${entry.participacion}</td>
								<td><fmt:formatNumber value = "${entry.valorReasegurado}" type = "currency"/></td>
								<td><fmt:formatNumber value = "${entry.primaCedida}" type = "currency"/></td>
								<td>${entry.fechaCreacion.toString().split(" ")[0]}</td>
								<td>${entry.usuario}</td>
								<c:set var="auxPorcen" value="${auxPorcen + entry.participacion}" />
							</tr>
						</c:forEach>
					</tbody>
            	</table>
            </div>
		</div>
		<div class="row">
			<div class="col-md-12 text-right">
				<button class="btn btn-blue" id="tConsulta">Consultar</button>
				<button class="btn btn-blue" id="tModifica">Modificar</button>
				<button class="btn btn-pink" id="tElimina">Eliminar</button>
			</div>
		</div>
		<br>
		<br>
		<div class="container" id="formulario">		
			<div class="row">
				<div class="col-md-12">
					<div class="form-inline divRdoType">
						<div class="form-check">
							<input class="form-check-input form-control" name="tipoConsulta" type="radio" id="radio_rea" value="0" checked="checked">
							<label class="form-check-label" for="radio_rea">
								<liferay-ui:message key="CotizadorRCPortlet.optReasegurador" />
							</label>
						</div>
						<div class="form-check">
							<input class="form-check-input form-control" name="tipoConsulta" type="radio" id="radio_int" value="1">
							<label class="form-check-label" for="radio_int">
								<liferay-ui:message key="CotizadorRCPortlet.optIntermediario" />
							</label>
						</div>
					</div>
				</div>
			
				<div class="col-md-12 in_inter d-none">
					<div class="md-form form-group">
						<select id="reaseConducto" name="reaseConducto" class="mdb-select form-control-sel colorful-select dropdown-primary coasegurador" searchable='<liferay-ui:message key="CotizadorRCPortlet.buscar" />' >
							<option value="-1" selected><liferay-ui:message key="CotizadorRCPortlet.selectOpDefoult" /></option>
							<c:forEach items="${conducto}" var="option">
								<option value="${option.idPersona}">${option.nombre}${option.appPaterno}${option.appMaterno}</option>
							</c:forEach>
						</select>
						<label for="reaseConducto">Intermediario</label>
					</div>
				</div>
			
				<div class="col-md-12 in_rea">
					<div class="md-form form-group">
						<select id="reaseCompania" name="reaseCompania" class="mdb-select form-control-sel colorful-select dropdown-primary coasegurador" searchable='<liferay-ui:message key="CotizadorRCPortlet.buscar" />' >
							<option value="-1" selected><liferay-ui:message key="CotizadorRCPortlet.selectOpDefoult" /></option>
							<c:forEach items="${reaseguradores}" var="option">
								<option value="${option.idPersona}" title="${option.codigo}">${option.nombre}${option.appPaterno}${option.appMaterno}</option>
							</c:forEach>
						</select>
						<label for="reaseCompania">Reasegurador</label>
					</div>
				</div>
			</div>
			
			
			<div class="row">
				<div class="col-md-6">
					<div class="md-form form-group">
						<input id="reasePorcentaje" type="text" name="reasePorcentaje" class="form-control campoPorcentajePartFacultativo">
						<label for="reasePorcentaje"> <liferay-ui:message key="CotizadorRCPortlet.PorcentajePart" /></label>
					</div>
				</div>
				<div class="col-md-6">
					<div class="md-form form-group">
						<input id="reaseValor" type="text" name="reaseValor" class="form-control">
						<label for="reaseValor"><liferay-ui:message key="CotizadorRCPortlet.ValorPart" /></label>
					</div>
				</div>
			</div>
			
			<div class="row mt-3">
				<div class="col-md-6">
					<div class="col-md-12 mt-3">
						<div class="form-check">
							<input type="checkbox" class="form-check-input" id="checkControl">
							<label class="form-check-label" for="checkControl"><liferay-ui:message key="CotizadorRCPortlet.clausulactrl" /></label>
						</div>
					</div>
					<div class="col-md-12 mt-3">
						<div class="form-check">
							<input type="checkbox" class="form-check-input" id="checkGarantia">
							<label class="form-check-label" for="checkGarantia"><liferay-ui:message key="CotizadorRCPortlet.garantiaPP" /></label>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="md-form form-group">
						<input id="URF" type="text" name="URF" class="form-control">
						<label for="URF">URF</label>
					</div>
				</div>
			</div>
			
			<div class="row mt-5">
				<div class="col-md-6">
					<div class="md-form form-group d-none" id="div_date">
						<input type="date" id="dc_date" name="dc_date" class="form-control datepicker">
						<label for="dc_date">Fecha</label>
					</div>
				</div>
				<div class="col-md-6">
					<div class="md-form form-group">
						<input id="noWings" type="text" name="noWings" class="form-control">
						<label for="noWings"><liferay-ui:message key="CotizadorRCPortlet.numNotaCe" /></label>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
							<div class="col-md-6">
								<div class="md-form input-group">
									<div>
										<input id="siniestro" type="number" class="form-control" min="1" pattern="[0-9]+">
										<label for="avisoSiniestro" placeholder="Aviso de Siniestro" aria-label="Aviso de Siniestro" aria-describedby="material-addon2" style="width: auto;">
										<liferay-ui:message key="CotizadorRCPortlet.avisoSiniestro" /></label>
									</div>
									<div>
										<div class="input-group-append">
											<span class="input-group-text md-addon" id="material-addon2"><liferay-ui:message key="CotizadorRCPortlet.diasEtiqueta" /></span>
										</div>
									</div>
								</div>
							</div>
								
							<div class="col-md-6">
								<div class="md-form input-group">
									<div class="col-md-8">
										<input  class="form-control tasa" id="contado" type="text" name="contadoValor">
										<label for="siniestroContado">
									<liferay-ui:message key="CotizadorRCPortlet.siniestroContado" /></label>
									
									</div>
									<div class="col-md-4">
									<div class="input-group-append">
										<select name="dc_moneda" id="dc_moneda" class="mdb-select form-control-sel" disabled>
											<c:forEach items="${listaCatMoneda}" var="option">
												<option value="${option.idCatalogoDetalle}" ${ moneda ==  option.idCatalogoDetalle ? 'selected' : ''}>${option.otro}</option>
											</c:forEach>
										</select>
										</div>
									</div>
								</div>
							</div>
						</div>
							
				<div class="col-md-12">
					<div class="col-md-3">
						<div class="md-form form-group">
							<input id="ramo" type="text" name="ramo" class="form-control" value="${nombreRamo}" disabled>
							<label for="ramo">Ramo</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input id="comPorcentaje" type="text" name="comPorcentaje" class="form-control campoPorcentajeFacultativo" required>
							<label for="comPorcentaje"><liferay-ui:message key="CotizadorRCPortlet.comPorcent" /></label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input id="fuerPorcentaje" type="text" name="fuerPorcentaje" class="form-control campoPorcentajeFacultativo">
							<label for="fuerPorcentaje">Fuerte Porcentaje</label>
						</div>
					</div>
					<div class="col-md-3">
						<div class="md-form form-group">
							<input id="taza" type="text" name="taza" class="form-control tasa">
							<label for="taza">Tasa por mil</label>
						</div>
					</div>
				</div>
			</div>

			<div class="row col-md-12 d-flex justify-content-center">
				<div class="col-md-2 mt-4" id='divURF'>
					<span>URF</span>
				</div>
				<div class="col-md-3 d-none" id='dwnldDivURF'>
					<div class="md-form">
						<div class="file-field">
							<a id='dwnldLnkURF' class="btn-floating blue-gradient mt-0 pull-left">
								<i class="fas fa-cloud-download-alt" aria-hidden="true"></i>
							</a>
						</div>
					</div>
				</div>
				<div class="col-md-3" id='upldDivURF'>
					<div class="md-form">
						<div class="file-field">
							<a class="btn-floating blue-gradient mt-0 pull-left">
								<i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
								<input id="fileURF" type="file">
							</a>
							<div class="file-path-wrapper">
								<input id="fileExaURF" class="file-path validate" type="text" placeholder="Examinar">
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-1" id='strashDivURF'>
					<div class="md-form">
						<a class="btn-floating blue-gradient mt-0 pull-right">
							<i class="fas fa-trash-alt" aria-hidden="true"></i>
						</a>
					</div>
				</div>
			</div>
			
			<div class="row col-md-12 d-flex justify-content-center">
				<div class="col-md-2 mt-4" id='divCobertura'>
					<span>Carta cobertura</span>
				</div>
				<div class="col-md-3 d-none" id='dwnldDivCobertura'>
					<div class="md-form">
						<div class="file-field">
							<a id='dwnldLnkCobertura' class="btn-floating blue-gradient mt-0 pull-left">
								<i class="fas fa-cloud-download-alt" aria-hidden="true"></i>
							</a>
						</div>
					</div>
				</div>
				<div class="col-md-3" id='upldDivCobertura'>
					<div class="md-form">
						<div class="file-field">
							<a class="btn-floating blue-gradient mt-0 pull-left">
								<i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
								<input id="fileCobertura" type="file">
							</a>
							<div class="file-path-wrapper">
								<input id="fileExaCobertura" class="file-path validate" type="text" placeholder="Examinar">
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-1" id='strashDivCobertura'>
					<div class="md-form">
						<a class="btn-floating blue-gradient mt-0 pull-right">
							<i class="fas fa-trash-alt" aria-hidden="true"></i>
						</a>
					</div>
				</div>
			</div>
			<div class="row col-md-12 d-flex justify-content-center">
				<div class="col-md-2 mt-4" id='divOtro'>
					<span>Otro</span>
				</div>
				<div class="col-md-3 d-none" id='dwnldDivOtro'>
					<div class="md-form">
						<div class="file-field">
							<a id='dwnldLnkOtro' class="btn-floating blue-gradient mt-0 pull-left">
								<i class="fas fa-cloud-download-alt" aria-hidden="true"></i>
							</a>
						</div>
					</div>
				</div>
				<div class="col-md-3" id='upldDivOtro'>
					<div class="md-form">
						<div class="file-field">
							<a class="btn-floating blue-gradient mt-0 pull-left">
								<i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
								<input id="fileOtro" type="file">
							</a>
							<div class="file-path-wrapper">
								<input class="file-path validate" type="text" placeholder="Examinar">
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-1" id='strashDivOtro'>
					<div class="md-form">
						<a class="btn-floating blue-gradient mt-0 pull-right">
							<i class="fas fa-trash-alt" aria-hidden="true"></i>
						</a>
					</div>
				</div>
			</div>
			
			
			<div class="col-md-12">
				<div class="col-md-12 text-right">
					<button class="btn btn-blue" id="saveRea">Guardar</button>
				</div>
			</div>
			
		</div>	
		<br>
		<br>
		<br>
		<div class="row col-md-12">
			<div class="col-md-12 text-right">
				<button class="btn btn-pink" id="btnCancelaAll">Cancelar</button>
				<button class="btn btn-blue" id="btnAceptaALl">Aceptar</button>
				<button class="btn btn-blue" id="btnRegresarALl">Regresar</button>
			</div>
		</div>		
		
	</div>	
</section>

<div class="modal" id="modalIntermediario" tabindex="-1" role="dialog" aria-labelledby="intermediarioLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnIntermediario">Si</button>
					</div>
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal">No</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

			
<script>
	var ramoGuardar = '';
	var guardaInfoFacultativoURL = '${guardaInfoFacultativoURL}';
	var getInfoReaseguradorURL = '${getInfoReaseguradorURL}';
	var getReaseguradoresURL = '${getReaseguradoresURL}';
	var eliminaReaseguradorURL = '${eliminaReaseguradorURL}';
	var guardaDocReaseguroURL = '${guardaDocReaseguroURL}';
	
	var reaseguroResponse = ${reaseguroResponseJson};
	var infoCotJson = ${infoCotJson};
	var infCotiURL = "${infCoti}";
	
	var porcentaje = ${porcentaje};
	var valorAsegurado = ${valorAsegurado};
	var nombreRamo = "${nombreRamo}";
	var ramo = "${ramo}";
	
	var banderaModifica = false;
	
	var auxPorcen = ${auxPorcen};
	
	var estado = ${estado};

</script>

<script src="<%=request.getContextPath()%>/js/objetos.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/mainReaseguro.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/mainFacultativo.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/reaFacultativo.js?v=${version}"></script>