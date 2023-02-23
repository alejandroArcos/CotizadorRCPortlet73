<%@ include file="../init.jsp"%>
<%@ include file="./colocacionReaseguroModales.jsp"%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reaseguroFacultativo.css?v=${version}">


<portlet:resourceURL id="/cotizadores/coaseguro/getCoaseguradores" var="getCoaseguradoresURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/getContratosReaseguro" var="getContratosReaseguroURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/guardaContratos" var="guardaContratosURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/coaseguro/regresaCotizador" var="regresaCotizadorURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/solicitarVobo" var="solicitarVoboURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/reaseguro/voboReaseguro" var="voboReaseguroURL" cacheability="FULL" />

<portlet:actionURL var="reaseguroFacultativo" name="/cotizadores/reaseguro/reaseguroFacultativo"/>

<fmt:setLocale value="es_MX" />

<style>
	.site-wrapper #coti-RC .table-striped tbody tr.selected td {
		background-color: pink; 
	}	
	div#table1_length {
	    display: none !important;
	}
</style>

<section id="coti-RC" class="upper-case-all">
	
	<div class="section-heading">
		<div class="container-fluid">
			<h4 class="title text-left"><liferay-ui:message key="CotizadorRCPortlet.titColocaReaseguro" /></h4> 
		</div>
	</div>
	
	<div class="container-fluid" id="divPaso1">
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
		<div class="row mt-5">
			<div class="col-md-12 text-center">
				<span> ¿ La colocación de Reaseguro Facultativo es para todas las ubicaciones ? </span>
				<span class="switch ml-5" >
					<label>
						No
						<input id="chktoggleRC" type="checkbox" >
						<span class="lever"></span>
						Si
					</label>
				</span>
			</div>
		</div>
		
		
		<div class="padding70">
			<div class="row mt-5">
				<div class="col-md-1">
					<div class="md-form form-group">
						<input id="ubicaActual" type="text" name="ubicaActual" class="form-control" value="1" readonly>
						<label for="ubicaActual">Ubicación</label>
					</div>
				</div>
				<div class="col-md-1 mt-3">
					<span> de ${ubicaciones} disponible(s)</span>
				</div>
				<div class="col-md-2 text-right mt-2">
					<button class="btn btn-blue" id="btnAceptaALl">Entrar</button>
				</div>
				<div class="col-md-1">
					<div class="md-form form-group">
						<input id="ubicaNext" type="text" name="ubicaNext" class="form-control">
						<label for="ubicaNext"># Ubicación</label>
					</div>
				</div>
			</div>
			
			<div class="row mt-5">
	            <div class="table-wrapper col-md-12">
	            	<table class="table table-striped table-bordered" style="width:100%;" id="table1">
	            		<thead>
							<tr>
								<th>Ramo</th>
								<th>Nombre</th>
								<th>Inciso</th>
								<th>Valor Asegurado</th>
								<th>Valor Prima</th>
								<th>Paq. Reaseguro</th>
								<th>Comision</th>
								<th>F. Inicio</th>
								<th>F. Term.</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${reaseguro.ramosReaseguro}" var="option">
								<tr>
									<td id="ramo">${option.ramo}</td>
									<td id="nombreRamo">${option.nombreRamo}</td>
									<td>${option.inciso}</td>
									<td id="valorAsegurado" class="number"><fmt:formatNumber value = "${option.valorAsegurado}" type = "currency"/></td>
									<td id="valorPrima"><fmt:formatNumber value = "${option.valorPrima}" type = "currency"/></td>
									<td>${option.paqReaseguro}</td>
									<td><fmt:formatNumber value = "${option.comision}" type = "currency"/></td>
									<td>${option.fechInicio.toString().split(" ")[0]}</td>
									<td>${option.fecTermino.toString().split(" ")[0]}</td>
								</tr>
							</c:forEach>
						</tbody>
	            	</table>
	            </div>
			</div>
			
			<div class="row mt-5">
				<div class="col-md-12">
					<div class="table-responsive text-nowrap">
 						<table class="table" id="tableContratos">
							<thead style="background-color: #1976d2; color: white;">
      							<tr>
        							<th scope="col">Contrato</th>
        							<th scope="col">Capacidad</th>
        							<th scope="col">% PR</th>
        							<th scope="col">Prima</th>
        							<th scope="col">Valor de Riesgo</th>
     	 						</tr>
    						</thead>
    						<tbody>
      							
    						</tbody>
  						</table>
					</div>
				</div>
			</div>
			
			<div class="row mt-5">
				<div class="col-md-12 text-center">
					<button class="btn btn-blue" id="facultativo" disabled>Facultativo</button>
					<button class="btn btn-blue" id="solVoboRea" ${voboReaseguro ? '' : 'disabled'}>Solicitar Vo Bo Rea</button>
					<button class="btn btn-blue" id="voboRea" >Vo Bo Rea</button>
					<button class="btn btn-blue" id="rechazoRea" >Rechazo Rea</button>
					<button class="btn btn-pink" id="cancelar" >Cancelar</button>
					<button class="btn btn-blue" id="aceptar" ${voboReaseguro ? 'disabled' : ''}>Aceptar</button>
				
				</div>
			</div>
			
		</div>
	</div>
	
</section>

<!-- Modal Rechazo Reaseguro -->
<div class="modal" id="modalRechazo" tabindex="-1" role="dialog" aria-labelledby="modalRechazoLabel"
	aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header orange">
				<h5 class="modal-title" id="modalRechazoLabel"></h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			
			<!--Body-->
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12" style="text-align: center;">
						<textarea id="comentarioRechazo" name="comentarioRechazo"
							class="md-textarea form-control" rows="3" maxlength="1000"
							style="text-transform: uppercase;">
						</textarea>
						<label for="comentarioRechazo">
							Comentarios de rechazo Reaseguro Facultativo
						</label>
					</div>
				</div>
			</div>

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-blue waves-effect waves-light" data-dismiss="modal" id="btnModalRechazo">Aceptar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<form action="${reaseguroFacultativo}" method="POST" style="display: none;" id="formFacultativo">
	<input type="hidden" name="reaseguroResponse" id="reaseguroResponse" />
	<input type="hidden" name="cotizacion" id="cotizacion" />
	<input type="hidden" name="ramo" id="ramo" />
	<input type="hidden" name="nombreRamo" id="nombreRamo" />
	<input type="hidden" name="version" id="version" />
	<input type="hidden" name="folio" id="folio" />
	<input type="hidden" name="infoCotJson" id="infoCotJson" />
	<input type="hidden" name="infoCotizacion" id="infoCotizacion" value="${infCoti}" />
	<input type="hidden" name="porcentaje" id="porcentaje" />
	<input type="hidden" name="valorAseguradoFacultativo" id="valorAseguradoFacultativo" />
</form>

<script>

	var getContratosReaseguroURL = "${getContratosReaseguroURL}";
	var guardaContratosURL = "${guardaContratosURL}";
	var solicitarVoboURL = "${solicitarVoboURL}";
	var regresaCotizadorURL = '${regresaCotizadorURL}';
	var voboReaseguroURL = '${voboReaseguroURL}';

	var reaseguroResponse = ${reaseguroJson};
	var infoCotJson = ${infoCotJson};
	var infCotiURL = "${infCoti}";
	
	var estado = ${estado};
	
	var disabled = "";
	
</script>

<script src="<%=request.getContextPath()%>/js/objetos.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/mainReaseguro.js?v=${version}"></script>