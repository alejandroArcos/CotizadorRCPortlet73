<%@ include file="../init.jsp"%>

<style>
	.site-wrapper .stepper li.completed a .circle {
		background-color: #388e3c !important;
	}
</style>

<section id="landing-agentes" class="upper-case-all">
		
		<div class="section-heading">
			<div class="container-fluid">
				<h4 class="title text-left">
<!-- 					HabitacionPortlet.cotPaqFamiliar -->
					Cotizador Paquete Familiar
				</h4>
			</div>
		</div>
	
		<div class="container-fluid">
		
			<div class="row">
				<div class="col-md-12">
					<ul class="stepper stepper-horizontal container-fluid">
						<li id="step1" class="completed">
							<a href="javascript:void(0)">
								<span class="circle">1</span> <span class="label">Datos de cotización</span>
							</a>
						</li>
						<li id="step2" class="completed">
							<a href="javascript:void(0)">
								<span class="circle">2</span> <span class="label">Datos de riesgo y modalidad</span>
							</a>
						</li>
						<li id="step3" class="active">
							<a href="javascript:void(0)">
								<span class="circle active_2">3</span> <span class="label active_2">Propuesta de Cotización</span>
							</a>
						</li>
						<li id="step4">
							<a href="javascript:void(0)">
								<span class="circle">4</span> <span class="label">Emisión</span>
							</a>
						</li>
					</ul>
				</div>
			</div>
			
			<div style="position: relative;">
				
				
			</div>
			
			<!-- paos 3 -->

			<div class="container-fluid" id="paso3">
				<div class="row divFolioP3">
					<div class="col-md-10">
						<span id="titPoliza" class="ml-5 font-weight-bold"></span>
					</div>
					<div class="col-md-2" style="text-align: right;">
						<div class="md-form form-group">
							<input id="idFolio3" type="text" name="idFolio3" class="form-control" value="111852 - 1" disabled="">
							<label for="idFolio3" class="active"> Folio: </label>
						</div>
					</div>
				</div>
				<div class="row" style="padding: 0 70px">
					<div id="divTbl" class="col-md-12">
						<table class="customTable" style="width: 100%;">
							<!-- <table class="table simple-data-table table-striped table-bordered" style="width:100%;"> -->
							<thead>
								<tr>
									<th>
										Edificio
									</th>
									<th>
										Suma Asegurada
									</th>
									<th>
										Prima
									</th>
									<th>
										Deducible
									</th>
								</tr>
							</thead>
							<tbody id="tabPaso3">
								
								
									 
										
									   <tr><th>Incendio Edificio</th><td></td><td></td><td></td></tr>
									
									<tr><td>Incendio-edificio</td><td class="number">           $100,000.00</td><td class="number">                $40.00</td><td>Sin deducible</td></tr>
								
									 
										
									   <tr><th>Incendio Contenidos</th><td></td><td></td><td></td></tr>
									
									<tr><td>Incendio-contenidos</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									
									<tr><td>Bienes bajo convenio expreso</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									 
										
									   <tr><th>Pérdidas Consecuenciales</th><td></td><td></td><td></td></tr>
									
									<tr><td>Gastos extraordinarios</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									
									<tr><td>Pérdida de rentas</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									 
										
									   <tr><th>Rotura de Cristales</th><td></td><td></td><td></td></tr>
									
									<tr><td>Rotura de cristales</td><td class="number">            $10,000.00</td><td class="number">               $120.00</td><td>5% sobre la pérdida con mínimo de 3 DSMGVDF</td></tr>
								
									 
										
									   <tr><th>Robo de Contenidos</th><td></td><td></td><td></td></tr>
									
									<tr><td>Robo Seccion I</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									
									<tr><td>Robo Seccion II</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									
									<tr><td>Robo Seccion III</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									 
										
									   <tr><th>Diversos Miscelaneos</th><td></td><td></td><td></td></tr>
									
									<tr><td>Dinero y valores (dentro y fuera)</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									 
										
									   <tr><th>Responsabilidad Civil</th><td></td><td></td><td></td></tr>
									
									<tr><td>Responsabilidad civil</td><td class="number">             $1,000.00</td><td class="number">                 $1.76</td><td>Sin deducible</td></tr>
								
									 
										
									   <tr><th>Diversos Técnicos</th><td></td><td></td><td></td></tr>
									
									<tr><td>Equipo electronico clasico</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									
									<tr><td>Equipo electrónico móvil y portátil</td><td class="number">Excluido</td><td class="number">Excluido</td><td>Excluido</td></tr>
								
									 
										
									   <tr><th>Beneficios Adicionales</th><td></td><td></td><td></td></tr>
									
									<tr><td>Servicio de Asistencia en el Hogar</td><td class="number">Amparada</td><td class="number">Amparada</td><td>Sin deducible</td></tr>
								
							</tbody>
						</table>
					</div>
					
					<div id="divTblEndBj" class="col-md-12 d-none">
						<div class="row justify-content-center">
							<div id="titulosEndBj"></div>
							<div id="datosEndBj" class="table-wrapper-scroll-table">
								
							</div>
							<div id="totalEndBj"></div>
						</div>			
					</div>
				</div>
				<div class="row" style="padding: 0 70px;">
					<div class="col-md-3">
						<div id="divCederComision" class="row ">
							<div class="col-sm-12">
								<!-- Grid row -->
								<div class="form-row align-items-center">
									<!-- Grid column -->
									<div class="col-11">
										<!-- Material input -->
										<div class="md-form">
											<input type="text" id="txtCederComision" class="form-control">
											<label id="titCederComision" for="txtCederComision">Cesión Art. 41</label>
										</div>
									</div>
									<!-- Grid column -->
	
									<!-- Grid column -->
									<div class="col-1">
										<!-- Material input -->
	
										<div class="md-form input-group mb-3">
											<div class="input-group-prepend">
												<span class="input-group-text md-addon">%</span>
											</div>
	
										</div>
									</div>
									<!-- Grid column -->
	
								</div>
								<!-- Grid row -->
							</div>
							<div class="col-sm-12">
								<button type="button" class="btn btn-pink waves-effect waves-light pull-right btn-block" id="btnCederComision" name="btnCederComision">
									CEDER COMISIóN
								</button>
							</div>
							
							
							
							<div class="col-sm-12 text-center mt-5">
								<form action="rea-coa" method="POST">
									<input type="hidden" id="modoVista" name="modoVista" value="1">
									<button class="btn btn-pink btn-block" id="btn1" type="submit">Colocación de Coaseguro</button>
								</form>
							</div>
							<div class="col-sm-12 text-center mt-3">
								<form action="rea-coa" method="POST">
									<input type="hidden" id="modoVista" name="modoVista" value="2">
									<button class="btn btn-pink btn-block" id="btn2" type="submit">Colocación de Reaseguro</button>
								</form>
							</div>
<!-- 							<div class="col-sm-12 text-center mt-3"> -->
<!-- 								<form action="cotizador-rc" method="POST"> -->
<!-- 									<input type="hidden" id="modoVista" name="modoVista" value="3"> -->
<!-- 									<button class="btn btn-pink btn-block" id="btn3"type="submit">Reaseguro Facultativo</button> -->
<!-- 								</form> -->
<!-- 							</div> -->
							
							
						</div>
	
	
					</div>
					<div class="col-md-6">
						<table class="table-borderless" style="width: 100%;">
							<tbody id="tabPaso3_2">
								<tr><td>Prima Neta:</td><td id="primaNeta" class="number"> $161.76 </td></tr>
								<tr><td>Recargo por Pago Fraccionado:</td><td class="number"> $0.00  </td></tr>
								<tr><td>Gastos de Expedición:</td><td class="number">  $37.52 </td></tr>
								<tr><td>I.V.A.:</td><td class="number">  $31.88 </td></tr>
							</tbody>
							<tfoot>
								<tr>
									<td>
										<b>Prima total:</b>
									</td>
									<td class="number" id="valPrimTot">
										<b id="tabPaso3_3">
											$231.16
										</b>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
					
					<div class="col-md-3">
						<div class="row " id="divPrimaObj">
							<div class="col-sm-12">
								<div class="md-form">
									
									<input type="text" id="txtPrimaObj" class="form-control ">
									<label id="titPrimaObj" for="txtPrimaObj">Prima Objetivo: (Dólares)</label>
								</div>
							</div>
							<div class="col-sm-12">
								<button type="button" class="btn btn-pink waves-effect waves-light pull-right btn-block" id="btnRecalcularPrima" name="btnRecalcularPrima">
									Recalcular prima
								</button>
							</div>
						</div>
					</div>
	
				</div>
	
				<div class="row" style="padding:70px">
					
				
					<div class="col-sm-12">
	
						
						<button type="button" class="btn btn-pink waves-effect waves-light pull-right d-none" id="btnEmitEndoso" name="btnEmitEndoso" onclick="$('#btnFacturaSuscrip').trigger('click')" disabled="">
							Emitir endoso
						</button>
						
						
						<button type="button" class="btn btn-pink waves-effect waves-light pull-right" id="btnEnvCotiSusAgente" name="btnEnvCotiSusAgente" hidden="">
							Enviar Cotización 
						</button>
						<button type="button" class="btn btn-blue waves-effect waves-light pull-right" id="btnNoAcepPropuesta" name="btnNoAcepPropuesta" hidden="">
							No aceptar propuesta
						</button>
						<button type="button" class="btn btn-pink waves-effect waves-light pull-right" id="btnRecotizar" name="btnRecotizar" hidden="">
							Recotizar
						</button>
						

						<!-- button class="btn btn-pink waves-effect waves-light pull-right" id="paso3_emision" disabled  >
							Solicitar Emisi&oacute;n
						</button -->
						<form action="http://172.25.10.44:8080/group/portal-agentes/paquete-familiar-maquetado?p_p_id=HomeOwnerQuotation_INSTANCE_KOW2joc9TkI4&amp;p_p_lifecycle=1&amp;p_p_state=normal&amp;p_p_mode=view&amp;_HomeOwnerQuotation_INSTANCE_KOW2joc9TkI4_javax.portlet.action=%2Fcotizadores%2FactionPaso4&amp;p_auth=IOQcBZAo" method="post" id="paso3-form">
							<input type="hidden" id="infoCotizacion" name="infoCotizacion" value="">
							<button type="button" class="btn btn-pink waves-effect waves-light btn-ubicacion pull-right" id="paso3_next" disabled="">
								Solicitar Emisión
							</button>
						</form>
						<button type="button" class="btn btn-pink waves-effect waves-light pull-right" id="paso3_slip">
							Generar Slip
						</button>
						<form action="http://172.25.10.44:8080/group/portal-agentes/paquete-familiar-maquetado?p_p_id=HomeOwnerQuotation_INSTANCE_KOW2joc9TkI4&amp;p_p_lifecycle=1&amp;p_p_state=normal&amp;p_p_mode=view&amp;_HomeOwnerQuotation_INSTANCE_KOW2joc9TkI4_javax.portlet.action=%2Fcotizadores%2FactionPaso2&amp;p_auth=IOQcBZAo" method="post" id="paso3-form-back">
							<input type="hidden" id="infoCotizacionBack" name="infoCotizacionBack" value="">
							<button type="button" class="btn btn-blue waves-effect waves-light pull-right" id="paso3_back">
								Regresar
							</button>
						</form>
						<button type="button" class="btn btn-blue waves-effect waves-light pull-right d-none" id="renovacion_back">
							Regresar
						</button>
						
						<button type="button" class="btn btn-blue waves-effect waves-light pull-right d-none" id="paso1_back" onclick="regresaPaso1();	">
							Regresar
						</button>
					</div>
					<div class="col-sm-12" hidden="">
						<div class=" col-sm-6"></div>
						<div class="form-check form-check-inline col-sm-2">
							<input type="radio" class="form-check-input" id="chkfactauto" name="rdofactura" value="1" checked="checked">
							<label class="form-check-label" for="chkfactauto">Generar la factura automática</label>
						</div>
	
						<!-- Material inline 2 -->
						<div class="form-check form-check-inline col-sm-2">
							<input type="radio" class="form-check-input" id="chkfactmanual" name="rdofactura" value="2">
							<label class="form-check-label" for="chkfactmanual">Generar la factura manual</label>
						</div>
						<button type="button" class="btn btn-pink waves-effect waves-light pull-right" id="btnFacturaSuscrip" name="btnFacturaSuscrip">Emitir</button>
						
					</div>
				</div>
	
			</div>
			
		</div>
	</section>