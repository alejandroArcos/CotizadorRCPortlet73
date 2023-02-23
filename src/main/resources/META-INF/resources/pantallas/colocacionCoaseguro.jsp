<%@ include file="../init.jsp"%>

<portlet:resourceURL id="/cotizadores/coaseguro/getCoaseguradores" var="getCoaseguradoresURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/coaseguro/guardaInfoCoaseguro" var="guardaInfoCoaseguroURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/coaseguro/guardaDocCoaseguro" var="guardaDocCoaseguroURL" cacheability="FULL" />
<portlet:resourceURL id="/cotizadores/coaseguro/regresaCotizador" var="regresaCotizadorURL" cacheability="FULL" />

<section id="coti-RC" class="upper-case-all">
	<div class="section-heading">
		<div class="container-fluid">
			<h4 class="title text-left"><liferay-ui:message key="CotizadorRCPortlet.titColocaCoaseguro" /></h4> 
		</div>
	</div>
	
	<div class="container-fluid" id="divPaso1">
		<div class="row divFolio">
			<div class="col-md-9 mt-4">
				<span id="spanNombre">
					${datosCliente.nombre} ${datosCliente.appPaterno} ${datosCliente.appMaterno}
				</span>
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
				<span><liferay-ui:message key="CotizadorRCPortlet.titCoaseguro" /></span>
				<span> ${tipoCoaseguro}</span>
			</div>
		</div>
		
	</div>
	<div class="padding70">
		
		<div class="row mt-3">
		
			<div class="col-md-10">
				<div class="contentCoasegurador">
					<div  class="row rowCoasegurador">
						<div class="col-md-3 text-center">
							<p class="mt-4"><liferay-ui:message key="CotizadorRCPortlet.titLider" /></p>
						</div>
						<div class="col-md-6">
						<div class="md-form form-group">
							<select name="coaseLider" id="coaseLider" class="mdb-select form-control-sel colorful-select dropdown-primary coasegurador" searchable='<liferay-ui:message key="CotizadorRCPortlet.buscar" />' ${ infCotizacion.tipoCoaseguro == 2576 || estado == 360 ? 'disabled' : '' }>
								<option value="-1" selected><liferay-ui:message key="CotizadorRCPortlet.selectOpDefoult" /></option>
								<c:forEach items="${listaPersonas}" var="option">
									<option codigo="${option.codigo}" value="${option.idPersona}" ${ (infCotizacion.tipoCoaseguro == 2576) && (option.codigo == '000000000001') ? 'selected' : (coaseguradores[0].p_coasegurador == option.idPersona && infCotizacion.tipoCoaseguro != 2576 ? 'selected' : '') }>${option.nombre}${option.appPaterno}${option.appMaterno}</option>
								</c:forEach>
							</select>
								<label for="coaseLider">
									Nombre
								</label>
							</div>
						</div>
						<div class="col-md-2">
							<div class="md-form form-group">
								<fmt:formatNumber var="porcentaje" type="number" minFractionDigits="2" maxFractionDigits="2" value="${coaseguradores[0].p_participacion}" />
								<input id="porcenParti" type="text" name="porcenParti" class="form-control auxPorcen" value="${porcentaje}" ${estado == 360 ? 'disabled' : ''} />
								<label for="porcenParti">
									% de participación
								</label>
							</div>
						</div>
						<div class="col-md-1">
						</div>
					</div>
					
					<div  class="row rowCoaseguradorSeguidor">
						<div class="col-md-3 text-center">
							<p class="mt-4"><liferay-ui:message key="CotizadorRCPortlet.titSeguidor" /></p>
						</div>
						<div class="col-md-6">
							<div class="md-form form-group">
								<select name="coaseSeguidor1" id="coaseSeguidor1" class="mdb-select form-control-sel colorful-select dropdown-primary coasegurador" searchable='<liferay-ui:message key="CotizadorRCPortlet.buscar" />' ${estado == 360 ? 'disabled' : ''}>
									<option value="-1" selected><liferay-ui:message key="CotizadorRCPortlet.selectOpDefoult" /></option>
									<c:forEach items="${listaPersonas}" var="option">
										<option codigo="${option.codigo}" value="${option.idPersona}" ${ coaseguradores[1].p_coasegurador ==  option.idPersona ? 'selected' : ''}>${option.nombre}${option.appPaterno}${option.appMaterno}</option>
									</c:forEach>
								</select>
								<label for="coaseSeguidor1">
									Nombre
								</label>
							</div>
						</div>
						<div class="col-md-2">
							<div class="md-form form-group">
								<fmt:formatNumber var="porcentaje" type="number" minFractionDigits="2" maxFractionDigits="2" value="${coaseguradores[1].p_participacion}" />
								<input id="porcenParti1" type="text" name="porcenParti1" class="form-control auxPorcen" value="${porcentaje}" ${estado == 360 ? 'disabled' : ''}>
								<label for="porcenParti1">
									% de participación
								</label>
							</div>
						</div>
						<div class="col-md-1">
						</div>
					</div>
					<c:if test="${ coaseguradores.size() > 2}">
						<c:forEach var="i" begin="2" end="${coaseguradores.size() - 1}">
							<div  class="row rowCoaseguradorSeguidor rowCoasegurador${i}">
								<div class="col-md-3 text-center">
									<p class="mt-4"><liferay-ui:message key="CotizadorRCPortlet.titSeguidor" /></p>
								</div>
								<div class="col-md-6">
									<div class="md-form form-group">
										<select name="coaseSeguidor${i}" id="coaseSeguidor${i}" class="mdb-select form-control-sel colorful-select dropdown-primary coasegurador" searchable='<liferay-ui:message key="CotizadorRCPortlet.buscar" />' ${estado == 360 ? 'disabled' : ''}>
											<option value="-1" selected><liferay-ui:message key="CotizadorRCPortlet.selectOpDefoult" /></option>
											<c:forEach items="${listaPersonas}" var="option">
												<option codigo="${option.codigo}" value="${option.idPersona}" ${ coaseguradores[i].p_coasegurador ==  option.idPersona ? 'selected' : ''}>${option.nombre}${option.appPaterno}${option.appMaterno}</option>
											</c:forEach>
										</select>
										<label for="coaseSeguidor${i}">
											Nombre
										</label>
									</div>
								</div>
								<div class="col-md-2">
									<div class="md-form form-group">
										<fmt:formatNumber var="porcentaje" type="number" minFractionDigits="2" maxFractionDigits="2" value="${coaseguradores[i].p_participacion}" />
										<input id="porcenParti${i}" type="text" name="porcenParti${i}" class="form-control auxPorcen" value="${porcentaje}" ${estado == 360 ? 'disabled' : ''}>
										<label for="porcenParti${i}">
											% de participación
										</label>
									</div>
								</div>
								<div class="col-md-1">
									<c:if test="${estado != 360}">
										<a onclick="deleteCoaseg(${i})" class="btn-floating blue-gradient mt-0 waves-effect waves-light"><i class="fas fa-trash-alt"></i></a>
									</c:if>
								</div>
							</div>
						</c:forEach>
					</c:if>
				</div>
				<div class="porcenTot">
					<div class="row">
						<div class="col-md-4"></div>
						<div class="col-md-4"></div>
						<div class="col-md-3">
							<div class="md-form form-group">
								<input id="porcenPartiTot" type="text" name="porcenPartiTot" class="form-control" disabled>								
							</div>
						</div>
						<div class="col-md-1"></div>
					</div>
				</div>
			</div>
			
			<div class="col-md-2 text-center">
				<c:if test="${estado != 360}">
					<button onclick="addSeguidor();" class="btn btn-blue" id="btnAdd">Agregar</button>
				</c:if>
				<button class="btn btn-pink" id="btnCancel">Cancelar</button>
				<c:if test="${estado != 360}">
					<button class="btn btn-blue" id="btnNext">Continuar</button>
				</c:if>
			
			</div>
			
		</div>
		
		<div id="divFiles">
			<c:if test="${estado != 360 }">
				<div class="row mt-3 d-flex justify-content-center rowAuxDoc countDoc0">
					<div class="col-md-3">
						<div class="md-form">
			 				<div class="file-field">
			 					<a class="btn-floating blue-gradient mt-0 pull-left">
			      					<i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
			      					<input type="file" class="inFile"
			      						accept="application/msword, application/pdf, application/vnd.openxmlformats-officedocument.wordprocessingml.document, image/jpeg, image/png" />
			    				</a>
			    				<div class="file-path-wrapper">
			      					<input class="file-path validate" type="text" placeholder="Examinar">
			    				</div>
			  				</div>
						</div>
					</div>
					<div class="col-md-1">
						<div class="md-form">
			   				<a onclick="btnAddFile();" class="btn-floating blue-gradient mt-0 pull-right">
			     					<i class="fa fa-plus" aria-hidden="true"></i>
			   				</a>
			   			</div>
		  			</div>
				</div>
			</c:if>
			<c:if test="${estado == 360}">
				<c:forEach var="documentos" items="${documentos}">		
					<div class="row mt-3 d-flex justify-content-center rowAuxDoc countDoc">
						<div class="col-md-3">
							<div class="md-form">
				 				<div class="file-field">
				 					<a class="btn-floating blue-gradient mt-0 pull-left">
				      					<i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
				      					<input type="file" class="inFile"
				      						accept="application/msword, application/pdf, application/vnd.openxmlformats-officedocument.wordprocessingml.document, image/jpeg, image/png" />
				    				</a>
				    				<div class="file-path-wrapper">
				      					<input class="file-path validate" type="text" placeholder="Examinar">
				    				</div>
				  				</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:if>
		</div>
		
		
	</div>
	
</section>

<script src="<%=request.getContextPath()%>/js/objetos.js?v=${version}"></script>
<script src="<%=request.getContextPath()%>/js/main.js?v=${version}"></script>

<script>

	var infCotizacion = ${infoCotJson};
	var infCotiURL = "${infCoti}";
	
	var getCoaseguradoresURL = '${getCoaseguradoresURL}';
	var guardaInfoCoaseguroURL = '${guardaInfoCoaseguroURL}';
	var guardaDocCoaseguroURL = '${guardaDocCoaseguroURL}';
	var regresaCotizadorURL = '${regresaCotizadorURL}';
	
	var estado = ${estado}

</script>