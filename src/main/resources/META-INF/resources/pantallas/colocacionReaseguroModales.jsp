<%@ include file="../init.jsp"%>
<div class="modal" id="modalMensajeSolicitarVOBOREA" tabindex="-1" role="dialog" aria-labelledby="clienteExistenttLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">
					Usted ha solicitado el Vo Bo de la colocaci&oacute;n de Reaseguro Facultativo
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
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

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnCerrar">Cerrar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal" id="modalMensajeVOBOREA" tabindex="-1" role="dialog" aria-labelledby="clienteExistenttLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">
					Usted ha dado el Vo Bo de la colocaci&oacute;n de Reaseguro Facultativo
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
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

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnCerrar2">Cerrar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal" id="modalMensajeRECHAZOREA" tabindex="-1" role="dialog" aria-labelledby="clienteExistenttLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">
					Usted ha rechazado la colocaci&oacute;n de Reaseguro Facultativo
				</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!--Body-->
			<div class="modal-body">

				<div class="row">
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

			<!--Footer-->
			<div class="modal-footer justify-content-center">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-pink waves-effect waves-light" id="btnCerrar3">Cerrar</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>