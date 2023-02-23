$( document ).ready(function() {
	
	var stockTable2 = $('#table2').dataTable(optionsXlsButton);
	
	stockTable2.on('click', 'tbody tr' ,function() {
		$('#table2 tbody tr').removeClass('selected');
		$(this).toggleClass('selected');
	});
});

$("#tModifica").click(function(data) {
	
	$(".in_rea").addClass("disabledbutton"); //$("#reaseCompania").attr('disabled',true);

	banderaModifica = true;
	
	if($("tr.selected").length != 0) {
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
			var response = JSON.parse(data);
			
			if(response.code == 0) {
				console.log(response);
				
				var infoReasegurador = response.registro;
				
				$("#reaseCompania").val(infoReasegurador.compania);
				$("#reaseConducto").val(infoReasegurador.conducto);
					
				
				$("select").change();
				
				$("#reasePorcentaje").val(infoReasegurador.porcentajeParticipacion);
				$("#reaseValor").val(daFormatoMoneda(infoReasegurador.valorParticipacion));//$("#reaseValor").val(infoReasegurador.valorParticipacion);
				$("#contado").val(daFormatoMoneda(infoReasegurador.siniestrosContado));
				
				if(infoReasegurador.tieneClausulaControl == 1) {
					$("#checkControl").click();
				}
				if(infoReasegurador.garantiaPago == 1) {
					$("#checkGarantia").click();
				}
				
				$("#URF").val(infoReasegurador.URF);				
				$('#dc_date').val(infoReasegurador.fechaPago);//$("#dc_date").val(infoReasegurador.fechaPago);
				$("#noWings").val(infoReasegurador.numeroNotaCesion);
				
				$("#siniestro").val(infoReasegurador.avisoSiniestro);
				$("#contado").val(daFormatoMoneda(infoReasegurador.siniestrosContado));//$("#contado").val(infoReasegurador.siniestrosContado);
				
				$("input").attr('disabled', true);
				$("#reaseCompania").attr('disabled', true);
				$("#reaseConducto").attr('disabled', true);
				
				$("label").attr('active',true).addClass('active');
				
				//Ramo
				$("#ramo").val(infoReasegurador.ramos[0].nombreRamo);
				$("#comPorcentaje").val(infoReasegurador.ramos[0].comisionPorcen);
				$("#fuerPorcentaje").val(infoReasegurador.ramos[0].fuertePorcentaje);
				$("#taza").val(infoReasegurador.ramos[0].tasaPormil);
				
				$("#saveRea").removeClass('d-none');
				
				$("input").attr('disabled', false);
				$("#reaseCompania").attr('disabled', false);
				$("#reaseConducto").attr('disabled', false);
				
				$("#reaseCompania").attr('active',false);
				$("#reaseCompania").siblings("label").addClass('top-label');
				
				//Mostrar carga de archivos
				$("#divURF").removeClass('d-none');
				$("#divCobertura").removeClass('d-none');
				$("#divOtro").removeClass('d-none');
				$("#upldDivURF").removeClass('d-none');
				$("#upldDivCobertura").removeClass('d-none');
				$("#upldDivOtro").removeClass('d-none');
				$("#strashDivURF").removeClass('d-none');
				$("#strashDivCobertura").removeClass('d-none');
				$("#strashDivOtro").removeClass('d-none');
				// Ocultar la descarga de archivos
				$("#dwnldDivURF").addClass('d-none');
				$("#dwnldDivCobertura").addClass('d-none');
				$("#dwnldDivOtro").addClass('d-none');
				
				
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

$("#tElimina").click(function(data) {
	
	if($("tr.selected").length != 0) {
		showLoader();
		$.post(eliminaReaseguradorURL, {
			responseMigracion: JSON.stringify(reaseguroResponse),
			folio: infoCotJson.folio,
			cotizacion: infoCotJson.cotizacion,
			version: infoCotJson.version,
			codigoReasegurador: $("tr.selected").find(".codigoReasegurador").attr('id'),
			ramo: ramo
		}).done(function(data) {
			hideLoader();
			var response = JSON.parse(data);
			
			if(response.code == 0) {
				console.log(response);
				
				window.location.reload();
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