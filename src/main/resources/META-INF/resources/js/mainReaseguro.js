var optionsXlsButton = {
	dom: 'fBrltip',
	buttons: [
		{
			extend:    'excelHtml5',
			text:      '<a class="btn-floating btn-sm teal waves-effect waves-light py-2 my-0">XLS</a>',
			titleAttr: 'Exportar XLS',
			className:"btn-unstyled",
			exportOptions: {
				/*columns: ':not(:last)',*/
				format: {
					body: function ( data, row, column, node ) {
						return data.replace( /[$,%]/g, '' );
					}
				}
			}
		}
	],
	language: {
		"sProcessing": "Procesando...",
		"sLengthMenu": "Mostrando _MENU_ registros por página",
		"sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
		"sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
		"sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
		"sSearch": "Filtrar:",
		"sLoadingRecords": "Cargando...",
		"oPaginate": {
		"sFirst": "<i class='fa fa-angle-double-left'>first_page</i>",
		"sLast": "<i class='fa fa-angle-double-right'>last_page</i>",
		"sNext": "<i class='fa fa-angle-right' aria-hidden='false'></i>",
		"sPrevious": "<i class='fa fa-angle-left' aria-hidden='false'></i>"
		

		}
	}
};

$( document ).ready(function() {
	
	$( '.datepicker' ).pickadate( {
		format : 'dd-mm-yyyy',
		formatSubmit : 'yyyy-mm-dd',
		min : new Date(),
		max : false
	} );
	
	var stockTable = $('#table1').dataTable(optionsXlsButton);
	
	stockTable.on('click', 'tbody tr' ,function() {
		$('#table1 tbody tr').removeClass('selected');
		$(this).toggleClass('selected');
		
		var auxRamo = $(this).find("#ramo")[0].textContent;
		
		if($(".rowContrato").length > 0) {
			
			var auxContratos = JSON.parse(generaInfoContratos());
			var auxPorcentaje = 0;
			
			$.each(auxContratos, function(key, value) {
				auxPorcentaje += value.porcentajePrima;
			});
			
			if(auxPorcentaje < 100) {
				showMessageError('.navbar', 'El porcentaje de distribución en contratos debe ser el 100%.', 0)
			}
			else  {
				showLoader();
				$.post(guardaContratosURL,{
					responseMigracion: JSON.stringify(reaseguroResponse),
					ramo: ramoGuardar,
					folio: infoCotJson.folio,
					cotizacion: infoCotJson.cotizacion,
					version: infoCotJson.version,
					contratosReaseguro: generaInfoContratos()
				}).done(function(data) {
					hideLoader();
					var responseGuardado = JSON.parse(data);
					
					if(responseGuardado.code == 0) {
						ramoGuardar=auxRamo;
						showMessageError('.navbar', responseGuardado.msg + " Guardado Contratos", 0);
						showLoader();
						$.post(getContratosReaseguroURL, {
							responseMigracion: JSON.stringify(reaseguroResponse),
							ramo: auxRamo,
							folio: infoCotJson.folio,
							cotizacion: infoCotJson.cotizacion,
							version: infoCotJson.version
						}).done(function(data) {
							hideLoader();
							console.log(data);
							
							var response = JSON.parse(data);
							
							var totPorcentajePrima = 0;
							var totPrima = 0;
							var totRiesgo = 0;
							
							$("#tableContratos tbody tr").remove();
							
							$.each(response.ContratosReaseguro, function(key, value) {
								$("#tableContratos tbody").append("<tr class='rowContrato'>"
										+ "<td id='nombreContrato' codigo='" + value.codigoContrato + "'>" + value.contrato + "</td>" 
										+ "<td id='capacidad'>" + daFormatoMoneda(value.capacidad) + "</td>"
										+ "<td class='prima'><input value='" + value.porcentajePrima + "' class='porcentajePrima campoPorcentaje' " + disabled + "/></td>"
										+ "<td id='primaContrato'>" + daFormatoMoneda(value.prima) + "</td>"
										+ "<td id='valorContrato'>" + daFormatoMoneda(value.valorRiesgo) + "</td>"
										+ "</tr>");
								
								totPorcentajePrima += value.porcentajePrima;
								totPrima += value.prima;
								totRiesgo += value.valorRiesgo;
								
								if(key == response.ContratosReaseguro.length -1) {
									if(value.porcentajePrima > 0) {
										$("#facultativo").attr('disabled', false);
										$("#aceptar").attr('disabled', true);
									}
								}
								
							});
							
							$("#tableContratos tbody").append("<tr>"
									+ "<td></td>" 
									+ "<td>Totales</td>"
									+ "<td id='totPrima'>" + totPorcentajePrima + "</td>"
									+ "<td id='totPrimaV'>" + daFormatoMoneda(totPrima) + "</td>"
									+ "<td id='totValor'>" + daFormatoMoneda(totRiesgo) + "</td>"
									+ "</tr>");
						});
					}
					
				});
			}
		} 
		else {
			ramoGuardar=auxRamo;
			showLoader();
			$.post(getContratosReaseguroURL, {
				responseMigracion: JSON.stringify(reaseguroResponse),
				ramo: auxRamo,
				folio: infoCotJson.folio,
				cotizacion: infoCotJson.cotizacion,
				version: infoCotJson.version
			}).done(function(data) {
				hideLoader();
				console.log(data);
				
				var response = JSON.parse(data);
				
				var totPorcentajePrima = 0;
				var totPrima = 0;
				var totRiesgo = 0;
				
				$("#tableContratos tbody tr").remove();
				
				$.each(response.ContratosReaseguro, function(key, value) {
					$("#tableContratos tbody").append("<tr class='rowContrato'>"
							+ "<td id='nombreContrato' codigo='" + value.codigoContrato + "'>" + value.contrato + "</td>" 
							+ "<td id='capacidad'>" + daFormatoMoneda(value.capacidad) + "</td>"
							+ "<td class='prima'><input value='" + value.porcentajePrima + "' class='porcentajePrima campoPorcentaje' id='valorPorcentaje' " + disabled + "/></td>"
							+ "<td id='primaContrato'>" + daFormatoMoneda(value.prima) + "</td>"
							+ "<td id='valorContrato'>" + daFormatoMoneda(value.valorRiesgo) + "</td>"
							+ "</tr>");
					
					totPorcentajePrima += value.porcentajePrima;
					totPrima += value.prima;
					totRiesgo += value.valorRiesgo;
					
					if(key == response.ContratosReaseguro.length -1) {
						if(value.porcentajePrima > 0) {
							$("#facultativo").attr('disabled', false);
							$("#aceptar").attr('disabled', true);
						}
					}
				});
				
				$("#tableContratos tbody").append("<tr>"
						+ "<td></td>" 
						+ "<td>Totales</td>"
						+ "<td id='totPrima'>" + totPorcentajePrima + "</td>"
						+ "<td id='totPrimaV'>" + daFormatoMoneda(totPrima) + "</td>"
						+ "<td id='totValor'>" + daFormatoMoneda(totRiesgo) + "</td>"
						+ "</tr>");
			});
		}
	});

	seleccionaModo();
	validaEstado();
});

function validaEstado() {
	switch(estado) {
		case 360:
			$("input").attr('disabled', true);
			disabled = "disabled";
		default:
			console.log(estado);
			break;
	}
}

$("#tableContratos").on("keyup blur", '.campoPorcentaje',function() {
	validaCampoPorcentaje(actualizaPorcentajes);
});

$(".campoPorcentajeFacultativo").on("keyup blur",function() {
	validaCampoPorcentajeFacultativo();	
});

$(".campoPorcentajePartFacultativo").on("keyup blur",function() {
	validaCampoPorcentajePartFacultativo();	
});

$(".th_interm").on("keyup blur",function() {
	$(".th_interm").hide();
	$('td:nth-child(50)').hide();	
});

$('#coti-RC').on("keyup", ".tasa", function() {
	var aux = $(event.target).val().split('.');
	$(event.target).val(aux[0]);
	$(event.target).val(function (index, value ) {
		console.log('valor1: ' + value.replace(/\D/g, "") );
		console.log('valor2: ' + value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",") );
		if ( aux.length > 1 ) {
			return value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",") + '.' + aux[1].slice(0,2);						
		}else{
			return value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");			
		}
	});
});

function validaCampoPorcentajeFacultativo() {
	$(event.target).val(function(index, value) {
		var expReg1 = /^\d{1,3}(\.)?$/;
		var expReg2 = /^\d{1,3}(\.\d{1,2})?$/;
		var validate = expReg1.test(value) || expReg2.test(value);
		
		if(!validate){
			value = value.slice(0, -1);
		}
		
		if(parseFloat(value) > 100) {
			showMessageError('.navbar', 'El porcentaje no puede superar el 100.00% ', 0);
			return '100.00';
		}	        
		 return value;
	 });
}

function validaCampoPorcentajePartFacultativo() {
	$(event.target).val(function(index, value) {
		var expReg1 = /^\d{1,3}(\.)?$/;
		var expReg2 = /^\d{1,3}(\.\d{1,6})?$/;
		var validate = expReg1.test(value) || expReg2.test(value);
		
		if(!validate){
			value = value.slice(0, -1);
		}
		
		if(parseFloat(value) > (porcentaje - auxPorcen) && !banderaModifica) {
			showMessageError('.navbar', 'El porcentaje no puede superar el ' + (porcentaje - auxPorcen) + '% ', 0);
			return (porcentaje - auxPorcen);
		}
	        
		 return value;
	 });
}

function validaCampoPorcentaje(_callBack){
	$(event.target).val(function(index, value) {
		
		var expReg1 = /^\d{1,3}(\.)?$/;
		var expReg2 = /^\d{1,3}(\.\d{1,6})?$/;
		var validate = expReg1.test(value) || expReg2.test(value);
		
		if(!validate){
			value = value.slice(0, -1);
		}
		
		if(parseFloat(value) > 100) {
			showMessageError('.navbar', 'El porcentaje no puede superar el 100.000000% ', 0);
			return '100.000000';
		}
		
		/*
		var aux = value.replace(/[$,]/g, '');
		aux = aux
			.replace(/\D/g, "")
			.replace(/([0-9])([0-9]{2,6})$/, '$1.$2');
		
		if (parseFloat(aux) > 100) {
			showMessageError('.navbar', 'El porcentaje no puede superar el 100.000000% ', 0);
			return '100.000000';
		}
		
		var res = aux.split(".");
		
		if(res.length > 1) {
			if( res[0].length >= 2) {
				if(parseFloat(aux) == 0) {
					
					showMessageError('.navbar', 'El porcentaje mínimo es 00.01% ', 0);
					return '00.000001';
				}
			}
		}
		*/
		if(valIsNullOrEmpty(value)){
			return 0;
		}
		else {
			return value;
		}
	});
	
	_callBack($(event.target));
}

function actualizaPorcentajes(campo) {
	
	var auxCapacidad = parseFloat($(campo).closest('tr').find("#capacidad").text()
			.replace( /[$,]/g, '' ));
	
	var auxPorcentajes = 0;
	
	var valorAsegurado = $("tr.selected").find("#valorAsegurado")[0].textContent.replace(/[$,]/g, '');
	
	if(parseFloat(valorAsegurado) * (parseFloat($(campo).val())/100) > auxCapacidad && auxCapacidad != 0) {
		
		showMessageError('.navbar', 'Excedió la capacidad del contrato, ajuste el % PR', 0);
		
		$(campo).val(0);
		/*$(campo).val(((auxCapacidad / parseFloat(valorAsegurado)) * 100).toFixed(2)); */
		$(campo).keyup();
	}
	else {
		
	$.each($("#tableContratos .campoPorcentaje"), function(key, value) {
		
		if(!valIsNullOrEmpty($(this).val())) {
		
			auxPorcentajes += parseFloat($(this).val());
		}
	});
	
		if(auxPorcentajes <= 100) {
		
			$("#totPrima")[0].textContent = auxPorcentajes;
		
			
			var valorPrima = $("tr.selected").find("#valorPrima")[0].textContent.replace(/[$,]/g, '');
			
			var auxPorcentajeContrato = (parseFloat($(campo).val())/100);
			
			var auxValorAsegurado = daFormatoMoneda(auxPorcentajeContrato * valorAsegurado);
			var auxValorPrima = daFormatoMoneda(auxPorcentajeContrato * valorPrima);
			
			var rowContrato = $(campo).closest('tr');
			
			$(rowContrato).find("#primaContrato")[0].textContent = auxValorPrima;
			$(rowContrato).find("#valorContrato")[0].textContent = auxValorAsegurado;
			
			actualizaPrimas();
			actualizaValor();
		}
		else {
			auxPorcentajes = auxPorcentajes - parseFloat($(campo).val());
			auxPorcentajes = 100 - auxPorcentajes;
			$(campo).val(auxPorcentajes);
			$(campo).keyup();
		}
	}
}

function actualizaPrimas() {
	
	var auxPrima = 0;
	
	$.each($("#tableContratos .rowContrato"), function(key, value) {
		
			auxPrima += parseFloat($(this).find('#primaContrato')[0].textContent.replace(/[$,]/g, ''));
	});
	
	$("#totPrimaV")[0].textContent = daFormatoMoneda(auxPrima);
	
}

function actualizaValor() {
	
	var auxValor = 0;
	
	$.each($("#tableContratos .rowContrato"), function(key, value) {
		
			auxValor += parseFloat($(this).find('#valorContrato')[0].textContent.replace(/[$,]/g, ''));
	});
	
	$("#totValor")[0].textContent = daFormatoMoneda(auxValor);	
}


function seleccionaModo() {
	
	switch(infoCotJson.modo) {
		case "REASEGURO":
			$("#voboRea").addClass('d-none');
			$("#rechazoRea").addClass('d-none');
			$("#btnRegresarALl").addClass('d-none');
			break;
		case "VOBO_REASEGURO":
			$("#solVoboRea").addClass('d-none');
			$("#tModifica").addClass('d-none');
			$("#tElimina").addClass('d-none');
			$("#aceptar").addClass('d-none');
			
			disabled = "disabled";
			
			$("#btnCancelaAll").addClass('d-none');
			$("#btnAceptaALl").addClass('d-none');
			$("#formulario").addClass('d-none');
					
			break;
	}
}

function porcentajeFacultativo() {
	
	var auxPorcentajeFacultativo = 0;
	
	$.each($("#tableContratos .rowContrato"), function(key, value) {
		
		if($(value).find("#nombreContrato")[0].textContent == "FACULTATIVO") {
			auxPorcentajeFacultativo = $(value).find("#valorPorcentaje").val();
		}
	});	
	
	return auxPorcentajeFacultativo;
}

function valorAseguradoFacultativo() {
	
	var auxValorContrato = "";
	
	/*
	$.each($("#tableContratos .rowContrato"), function(key, value) {
		
		if($(value).find("#nombreContrato")[0].textContent == "FACULTATIVO") {
			auxValorContrato = $(value).find("#valorContrato")[0].textContent.replace(/[$,]/g, '');
		}
	});
	*/
	
	return $("tr.selected").find("#valorAsegurado")[0].textContent.replace(/[$,]/g, '');
}

$("#facultativo").click(function() {
	
	var auxRamo = $("tr.selected").find("#ramo")[0].textContent;
	
	var auxContratos = JSON.parse(generaInfoContratos());
	var auxPorcentaje = 0;
	
	$.each(auxContratos, function(key, value) {
		auxPorcentaje += value.porcentajePrima;
	});
	
	if(auxPorcentaje < 100) {
		showMessageError('.navbar', 'El porcentaje de distribución en contratos debe debe ser el 100%.', 0)
	}
	else  {
		showLoader();
		$.post(guardaContratosURL,{
			responseMigracion: JSON.stringify(reaseguroResponse),
			ramo: auxRamo,
			folio: infoCotJson.folio,
			cotizacion: infoCotJson.cotizacion,
			version: infoCotJson.version,
			contratosReaseguro: generaInfoContratos()
		}).done(function(data) {
			hideLoader();
			var responseGuardado = JSON.parse(data);
			
			if(responseGuardado.code == 0) {
		
				$("#reaseguroResponse").val(JSON.stringify(reaseguroResponse));
				$("#infoCotJson").val(JSON.stringify(infoCotJson));
				$("#cotizacion").val(infoCotJson.cotizacion);
				$("#formFacultativo #ramo").val($("tr.selected").find("#ramo")[0].textContent);
				$("#formFacultativo #nombreRamo").val($("tr.selected").find("#nombreRamo")[0].textContent);
				$("#version").val(infoCotJson.version);
				$("#folio").val(infoCotJson.folio);
				$("#porcentaje").val(porcentajeFacultativo());
				$("#valorAseguradoFacultativo").val(valorAseguradoFacultativo());
				
				$("#formFacultativo").submit();
			}
			else {
				showMessageError('.navbar', response.msg + " Guardado Contratos", 0);
			}
		});
	}
});

$("#solVoboRea").click(function() {
	
	showLoader();
	$.post(solicitarVoboURL,{
		infCotizacion: JSON.stringify(infoCotJson),
		sitio: window.location.origin + window.location.pathname
	}).done(function(data) {
		hideLoader();
		console.log(data);
		var response = JSON.parse(data);
		
		if(response.code == 0) {
			$('#modalMensajeSolicitarVOBOREA').modal({
                show: true,
                backdrop: 'static',
                keyboard: false
            });
		}
		else {
			showMessageError('.navbar', response.msg, 0);
		}		
	});	
});

$("#voboRea").click(function() {
	
	showLoader();
	$.post(voboReaseguroURL, {
		infCotizacion: JSON.stringify(infoCotJson),
		estatus: 1,
		sitio: window.location.origin
	}).done(function(data) {
		hideLoader();		
		var response = JSON.parse(data);
	
		if(response.code == 0) {
			$('#modalMensajeVOBOREA').modal({
                show: true,
                backdrop: 'static',
                keyboard: false
            });
		}
		else {
			showMessageError('.navbar', response.msg, 0);
		}
	});
});

$("#rechazoRea").click(function() {
	$("#modalRechazo").modal('show');
});

$("#btnModalRechazo").click(function() {
	
	showLoader();
	$.post(voboReaseguroURL, {
		infCotizacion: JSON.stringify(infoCotJson),
		estatus: 0,
		sitio: window.location.origin,
		comentarios: $("#comentarioRechazo").val()
	}).done(function(data) {
		hideLoader();
		console.log(data);
		
		var response = JSON.parse(data);
		
		if(response.code == 0) {
			$('#modalMensajeRECHAZOREA').modal({
                show: true,
                backdrop: 'static',
                keyboard: false
            });
		}
		else {
			showMessageError('.navbar', response.msg, 0);
		}
	});	
});

function generaInfoFacultativo() {
	
	var facultativo = new Object();
	if($("#radio_rea").is(":checked")){
		facultativo.tipoReasegurador = 1;
	}else{
		facultativo.tipoReasegurador = 2;
	}
	
	facultativo.estado = 1;
	facultativo.compania = $("#reaseCompania").val();
	facultativo.conducto = $("#reaseConducto").val();
	facultativo.porcentajeParticipacion = $("#reasePorcentaje").val();
	facultativo.valorParticipacion = $("#reaseValor").val().replace(/[$,]/g, '');
	facultativo.tieneClausulaControl = $("#checkControl").is(':checked') ? 1 : 0;
	facultativo.garantiaPago = $("#checkGarantia").is(':checked') ? 1 : 0;
	facultativo.URF = $("#URF").val();
	facultativo.numeroNotaCesion = $("#noWings").val();
	facultativo.fechaPago = $("#dc_date").val();
	facultativo.avisoSiniestro = $("#siniestro").val();
	facultativo.siniestrosContado = $("#contado").val(); 
	facultativo.tipoGuardado = banderaModifica ? "Modificacion" : "";
	facultativo.ramos = [];
	
	var ramos = new Object();
	ramos.ramo = ramo;
	ramos.nombreRamo = nombreRamo;
	ramos.comisionPorcen = $("#comPorcentaje").val();
	ramos.fuertePorcentaje = $("#fuerPorcentaje").val();
	ramos.tasaPormil = $("#taza").val();
	
	facultativo.ramos.push(ramos);
	
	return JSON.stringify(facultativo);
	
}

$("#saveRea").click(function() {
	
	var comboCompania = document.getElementById("reaseCompania");
	var codigoCompañia = comboCompania.options[comboCompania.selectedIndex].title;
	
	
	var msjReasegurador = "Los campos Reasegurador, Comisión Porcentaje y Porcentaje de Participación son obligatorios.";
	var msjIntermediario= "Los campos Intermediario, Reasegurador, Comisión Porcentaje y Porcentaje de Participación son obligatorios.";
	
	if(codigoCompañia == "TMFF" && (valIsNullOrEmpty($("#URF").val()) || valIsNullOrEmpty($("#noWings").val()))) {
		showMessageError('.navbar',
				"Hace falta llenar los campos obligatorios URF/Número Nota de Cesión", 0);
		
		$("#URF").addClass('invalid');
		$("#noWings").addClass('invalid');
	} else if($("#radio_rea").is(":checked") && (valIsNullOrEmpty($("#reasePorcentaje").val()) || $("#reaseCompania").val()== -1 || valIsNullOrEmpty($("#comPorcentaje").val()) )) {
			showMessageError('.navbar',msjReasegurador, 0);
						
			if(valIsNullOrEmpty($("#reasePorcentaje").val()))
				$("#reasePorcentaje").addClass('invalid');
			else
				$("#reasePorcentaje").removeClass('invalid');
			
			if($("#reaseCompania").val()== -1)
				$("#reaseCompania").siblings("input").addClass('invalid');
			else
				$("#reaseCompania").siblings("input").removeClass('invalid');
			
			if(valIsNullOrEmpty($("#comPorcentaje").val()))
				$("#comPorcentaje").addClass('invalid');
			else
				$("#comPorcentaje").removeClass('invalid');
			
			
	} else if($("#radio_int").is(":checked") && (valIsNullOrEmpty($("#reasePorcentaje").val()) || $("#reaseCompania").val()== -1 || $("#reaseConducto").val() == -1 || valIsNullOrEmpty($("#comPorcentaje").val()) )){
			showMessageError('.navbar',msjIntermediario, 0);
			
			if(valIsNullOrEmpty($("#reasePorcentaje").val()))
				$("#reasePorcentaje").addClass('invalid');
			else
				$("#reasePorcentaje").removeClass('invalid');
			
			if($("#reaseCompania").val()== -1)
				$("#reaseCompania").siblings("input").addClass('invalid');
			else
				$("#reaseCompania").siblings("input").removeClass('invalid');
			
			if(valIsNullOrEmpty($("#comPorcentaje").val()))
				$("#comPorcentaje").addClass('invalid');
			else
				$("#comPorcentaje").removeClass('invalid');
			
			if($("#reaseConducto").val() == -1)
				$("#reaseConducto").siblings("input").addClass('invalid');
			else
				$("#reaseConducto").siblings("input").removeClass('invalid');
	}else if ($("#reasePorcentaje").val()== 0){
			showMessageError('.navbar','El porcentaje de partición debe ser mayor a 0');
			$("#reasePorcentaje").addClass('invalid');
	}else if (codigoCompañia == "TMFF" && $("#fileURF").val() == ""){
		showMessageError('.navbar','El documento URF es obligatorio para la reaseguradora');
		$("#fileExaURF").addClass('invalid');
	}else if (codigoCompañia != "TMFF" && $("#fileCobertura").val() == ""){
		showMessageError('.navbar','El documento Carta Cobertura es obligatorio para la reaseguradora');
		$("#fileExaCobertura").addClass('invalid');
	}else {
			showLoader();
			$.post(guardaInfoFacultativoURL,{
				responseMigracion: JSON.stringify(reaseguroResponse),
				ramo: reaseguroResponse.ramosReaseguro[0].ramo,
				folio: infoCotJson.folio,
				cotizacion: infoCotJson.cotizacion,
				version: infoCotJson.version,
				reaseguroFacultativo: generaInfoFacultativo()
			})
			.done(function(data) {
				hideLoader();
				console.log(data);
				
				var response = JSON.parse(data);
				
				if(response.code == 0) {
					// Ahora guardamos los documentos
					var data = new FormData();
					
					var enviar= false;
					
					//URF
					if($("#fileURF").val() != "") {
						var fileURF = $("#fileURF")[0].files[0];
						data.append("fileURF", fileURF);
						data.append("fileURFExt", fileURF.name.split('.')[1]);
						data.append("savefileURF", true);
						enviar=true;
					}else{
						data.append("savefileURF", false);
					}
					
					//Carta Cobertura
					if($("#fileCobertura").val() != "") {
						var fileCobertura = $("#fileCobertura")[0].files[0];
						data.append("fileCobertura", fileCobertura);
						data.append("fileCoberturaExt", fileCobertura.name.split('.')[1]);
						data.append("savefileCobertura", true);
						enviar=true;
					}else{
						data.append("savefileCobertura", false);
					}
					
					//Otro
					if($("#fileOtro").val() != "") {
						var fileOtro = $("#fileOtro")[0].files[0];
						data.append("fileOtro", fileOtro);
						data.append("fileOtroExt", fileOtro.name.split('.')[1]);
						data.append("savefileOtro", true);
						enviar=true;
					}else{
						data.append("savefileOtro", false);
					}

					if(enviar==true){
						data.append('folio', infoCotJson.folio);
						data.append('cotizacion',  infoCotJson.cotizacion);
						data.append('version', infoCotJson.version);
						data.append('ubicacion', 1);  // Falta pasar la ubicacion correcta
						data.append('ramo', reaseguroResponse.ramosReaseguro[0].ramo);
						data.append('reaseguradora', $("#reaseCompania").val());
						var url = new URL(window.location.href);
						data.append('urlOrigin', url.origin);
						
						showLoader();
						$.ajax({
							url: guardaDocReaseguroURL,
							data: data,
							processData: false,
							contentType: false,
							type: 'POST',
							success: function() {
								window.location.reload();
							},
							fail: function() {
								showMessageError('.nav', 'Error al cargar documentos ', 0);
								hideLoader();
							}
						});
					}else{
						window.location.reload();
					}
				}
				else {
					showMessageError('.navbar', response.msg + " Guardado Contratos", 0);
				}
			});
	}
});

$("#checkGarantia").on('change', function() {
	
	if($(this).is(':checked')) {
		$("#div_date").removeClass('d-none');
		$("#div_date").find('label').addClass('active');
	}
	else {
		$("#div_date").addClass('d-none');
	}
});

$("#reasePorcentaje").blur(function() {
	
	var auxValor = 0;
	
	auxValor = ($("#reasePorcentaje").val() * valorAsegurado)/100;
	
	$("#reaseValor").val(daFormatoMoneda(auxValor));
	$("#reaseValor").siblings('label').addClass('active');
});

$("#tableContratos").on('blur', '.porcentajePrima', function(){
	
	var fila = $(this).closest('tr');
	
	if($(fila).find("#nombreContrato")[0].textContent == "FACULTATIVO") {
		if(parseFloat($(this).val()) > 0) {
			$("#facultativo").attr('disabled', false);
			$("#aceptar").attr('disabled', true);
		}
	}
	
	console.log($(fila).find("#nombreContrato")[0].textContent);
});

function generaInfoContratos() {
	
	var arrayContratos = [];
	
	$.each($(".rowContrato"), function(key, value) {
		
		var contrato = new Object();
		
		$.each($(this).find('td').not('.prima'), function(keyTd, valueTd) {
			
			switch(keyTd) {
				case 0:
					contrato.contrato = $(this)[0].textContent;
					break;
				case 1:
					contrato.capacidad = parseFloat($(this)[0].textContent.replace(/[$,]/g, ''));
					break;
				case 2:
					contrato.prima = parseFloat($(this)[0].textContent.replace(/[$,]/g, ''));
					break;
				case 3:
					contrato.valorRiesgo = parseFloat($(this)[0].textContent.replace(/[$,]/g, ''));
					break;
			}
			
		});
		
		contrato.codigoContrato = $(this).find("#nombreContrato").attr('codigo') != 'undefined' ? $(this).find("#nombreContrato").attr('codigo') : null;
		contrato.porcentajePrima = parseFloat($(this).find('.porcentajePrima').val());
		contrato.porcentajeVA = parseFloat($(this).find('.porcentajePrima').val());
		
		arrayContratos.push(contrato);
	});
	
	console.log(arrayContratos);
	
	return JSON.stringify(arrayContratos);
}

$("#aceptar").click(function() {
	
	if($(".rowContrato").length > 0) {
	
		var auxRamo = $("tr.selected").find("#ramo")[0].textContent;
		
		var auxContratos = JSON.parse(generaInfoContratos());
		var auxPorcentaje = 0;
		
		$.each(auxContratos, function(key, value) {
			auxPorcentaje += value.porcentajePrima;
		});
		
		if(auxPorcentaje < 100) {
			showMessageError('.navbar', 'El porcentaje de distribución en contratos debe ser el 100%.', 0)

		}
		else  {
			showLoader();
			$.post(guardaContratosURL,{
				responseMigracion: JSON.stringify(reaseguroResponse),
				ramo: auxRamo,
				folio: infoCotJson.folio,
				cotizacion: infoCotJson.cotizacion,
				version: infoCotJson.version,
				contratosReaseguro: generaInfoContratos()
			}).done(function(data) {
				hideLoader();
				console.log(data);
			
				$.post(regresaCotizadorURL, {
					infCoti: infCotiURL
				}).done(function(data) {
					
					var response = JSON.parse(data);
					
					window.location = response.url + "=" + response.infoCot;
				});
			
			});
		}
	}
	else {
		
		$.post(regresaCotizadorURL, {
			infCoti: infCotiURL
		}).done(function(data) {
			
			var response = JSON.parse(data);
			
			window.location = response.url + "=" + response.infoCot;
		});
	}
});

$("#cancelar").click(function() {
	
	$.post(regresaCotizadorURL, {
		infCoti: infCotiURL
	}).done(function(data) {
		
		var response = JSON.parse(data);
		
		window.location = response.url + "=" + response.infoCot;
	});
});

$("#btnCancelaAll").click(function() {
	window.location = "rea-coa?infoCotizacion=" + infCotiURL;
});
$("#btnRegresarALl").click(function() {
	window.location = "rea-coa?infoCotizacion=" + infCotiURL;
});

$("#btnAceptaALl").click(function() {
	
	var porcentajeTotal = 0;
	
	$.each($(".rowReasegurador"), function(key, value) {
		
		porcentajeTotal += parseFloat($(value).find(".porcentajePart").text());		
	});
	
	if(porcentajeTotal < porcentaje) {
		showMessageError('.navbar', "El porcentaje de participación de Reaseguro Facultativo no se" +
				" ha cubierto completamente, aún falta un " + (porcentaje - porcentajeTotal) + 
				"% por asignar. Favor de capturarlo antes de continuar");
	}
	else {
		window.location = "rea-coa?infoCotizacion=" + infCotiURL;
	}
});

function valIsNullOrEmpty(value) {
	if (value === undefined) {
		return true;
	}
	value = value.trim();
	return (value == null || value == "null" || value === "");
}

$("#tConsulta").click(function(data) {
	$("#radio_rea").removeAttr("checked");
	$("#radio_int").removeAttr("checked");

	if($("tr.selected").length != 0 && $("tr.selected").find(".codigoReasegurador").attr('id')) {
		showLoader();
		$.post(getInfoReaseguradorURL, {
			responseMigracion: JSON.stringify(reaseguroResponse),
			folio: infoCotJson.folio,
			cotizacion: infoCotJson.cotizacion,
			version: infoCotJson.version,
			codigoReasegurador: $("tr.selected").find(".codigoReasegurador").attr('id'),
			ramo: ramo
		}).done(function(data) {
			hideLoader();
			$("#formulario").removeClass('d-none');
			
			var response = JSON.parse(data);
			
			if(response.code == 0) {
				console.log(response);
				
				$("#saveRea").addClass('d-none');
				
				var infoReasegurador = response.registro;
				
				$("#reaseCompania").val(infoReasegurador.compania);
				$("#reaseConducto").val(infoReasegurador.conducto);
				
				if($("#reaseCompania").val() && $("#reaseConducto").val() == -1) {
					$("#reaseCompania").val(infoReasegurador.compania);
					$("#reaseConducto").val(infoReasegurador.conducto);
					
					muestraCampos( ".in_rea" );
					ocultaCampos( ".in_inter" );
					
					$("#radio_rea").prop("checked", true);
					$("#radio_int").prop("checked", false);
				}
				else if($("#reaseCompania").val() && $("#reaseConducto").val()) {
					$("#reaseCompania").val(infoReasegurador.compania);
					$("#reaseConducto").val(infoReasegurador.conducto);
					
					muestraCampos( ".in_inter" );
					muestraCampos( ".in_rea" );	
					
					$("#radio_int").prop("checked", true);
					$("#radio_rea").prop("checked", false);
				}
				
				$("select").change();
				
				$("#reasePorcentaje").val(infoReasegurador.porcentajeParticipacion);
				$("#reaseValor").val(daFormatoMoneda(infoReasegurador.valorParticipacion));//$("#reaseValor").val(infoReasegurador.valorParticipacion);
				$("#contado").val(daFormatoMoneda(infoReasegurador.siniestrosContado));
				
				if(infoReasegurador.tieneClausulaControl == 1) {
					$("#checkControl").click();
				}
				if(infoReasegurador.garantiaPago == 1) {
					$("#checkGarantia").click();
					$("#dc_date").val();					
				}
				
				$("#URF").val(infoReasegurador.URF);
				$('#dc_date').val(infoReasegurador.fechaPago);
				$("#noWings").val(infoReasegurador.numeroNotaCesion);
				
				$("#siniestro").val(infoReasegurador.avisoSiniestro); 
				$("#contado").val(daFormatoMoneda(infoReasegurador.siniestrosContado)); //$("#contado").val(infoReasegurador.siniestrosContado);
				
				$("input").attr('disabled', true);
				$("label").attr('active',true).addClass('active');
				
				//Ramo
				$("#ramo").val(infoReasegurador.ramos[0].nombreRamo);
				$("#comPorcentaje").val(infoReasegurador.ramos[0].comisionPorcen);
				$("#fuerPorcentaje").val(infoReasegurador.ramos[0].fuertePorcentaje);
				$("#taza").val(infoReasegurador.ramos[0].tasaPormil);
				
				$("#reaseCompania").attr('active',false);
				$("#reaseCompania").removeClass('active');
				$("label").addClass('top-label');

				//Ocultar la carga de archivos:
				$("#upldDivURF").addClass('d-none');
				$("#upldDivCobertura").addClass('d-none');
				$("#upldDivOtro").addClass('d-none');
				$("#strashDivURF").addClass('d-none');
				$("#strashDivCobertura").addClass('d-none');
				$("#strashDivOtro").addClass('d-none');
				
				//Descarga de archivos:
				if(response.fileURFName){
                    var fileAux = 'data:application/octet-stream;base64,' + response.fileURF;
                    var dlnk = document.getElementById('dwnldLnkURF');
                    dlnk.href = fileAux;
                    dlnk.download = response.fileURFName;
                    $("#divURF").removeClass('d-none');
                    $("#dwnldDivURF").removeClass('d-none');
				}else{
					$("#dwnldDivURF").addClass('d-none');
					$("#divURF").addClass('d-none');
				}
				if(response.fileCoberturaName){
                    var fileAux = 'data:application/octet-stream;base64,' + response.fileCobertura;
                    var dlnk = document.getElementById('dwnldLnkCobertura');
                    dlnk.href = fileAux;
                    dlnk.download = response.fileCoberturaName;
                    $("#dwnldDivCobertura").removeClass('d-none');
                    $("#divCobertura").removeClass('d-none');
				}else{
					$("#dwnldDivCobertura").addClass('d-none');
					$("#divCobertura").addClass('d-none');
				}
				if(response.fileOtroName){
                    var fileAux = 'data:application/octet-stream;base64,' + response.fileOtro;
                    var dlnk = document.getElementById('dwnldLnkOtro');
                    dlnk.href = fileAux;
                    dlnk.download = response.fileOtroName;
                    $("#dwnldDivOtro").removeClass('d-none');
                    $("#divOtro").removeClass('d-none');
				}else{
					$("#dwnldDivOtro").addClass('d-none');
					$("#divOtro").addClass('d-none');
				}
			}
			else {
				showMessageError('.navbar', response.msg, 0);
			}
		});
	}
	else {
		showMessageError('.navbar', "Selecciona primero un Reasegurador de la tabla para consultar.",
				0);
	}
});

function daFormatoMoneda(value){	
		return formatter.format(value);
}

$('#btnCerrar').click(function() {
	$('#modalMensajeSolicitarVOBOREA').modal( 'hide' );
	$("#cancelar").click();
});
$('#btnCerrar2').click(function() {
	$('#modalMensajeVOBOREA').modal( 'hide' );
	window.location = window.location.origin + "/group/portal-agentes/consulta-cotizaciones";
});
$('#btnCerrar3').click(function() {
	$('#modalMensajeRECHAZOREA').modal( 'hide' );
	window.location = window.location.origin + "/group/portal-agentes/consulta-cotizaciones";
});
