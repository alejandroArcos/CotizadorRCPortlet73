var optionsXlsButton = {dom: 'fBrltip', buttons: [{
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
}]};

$( document ).ready(function() {
	$( '.datepicker' ).pickadate( {
		format : 'yyyy-mm-dd',
		formatSubmit : 'yyyy-mm-dd'
	} );
	
	countSeguidor = $('.rowCoaseguradorSeguidor').length;
	countDocs = $('#divFiles .rowAuxDoc').length;
	
	$( ".contentCoasegurador" ).on("keyup blur", ".auxPorcen",function() {
		validaCampoPorcentaje();
		fSumaPorcentaje();
    });
	
	var stockTable = $('#table1').dataTable(optionsXlsButton);
	
	stockTable.on('click', 'tbody tr' ,function() {
		$('#table1 tbody tr').removeClass('selected');
		$(this).toggleClass('selected');
	});
	
	
	$("select.coasegurador").trigger('change', [true]);
	
	$.each($(".contentCoasegurador .auxPorcen"), function(key, value) {
	    $(value).focus();
	    $(value).blur();
	});
});

function validaCampoPorcentaje(){
	$(event.target).val(function(index, value) {
		 var aux = value.replace(/[$,]/g, '');
		 aux = aux.replace(/\D/g, "")
		 .replace(/([0-9])([0-9]{2})$/, '$1.$2')
		 .replace(/\B(?=(\d{2})+(?!\d)\.?)/g, ",");
	        
		 aux = aux.replace(/\,/g, '');
		 if (parseFloat(aux) > 99.99) {
			 showMessageError('.navbar', 'El porcentaje no puede superar el 99.99% ', 0);
			 return '99.99';
		 }
	        
		 var res = aux.split(".");
		 if(res.length > 1) {
			 if( res[0].length >= 2) {
				 if(parseFloat(aux) == 0) {
					 showMessageError('.navbar', 'El porcentaje mínimo es 00.01% ', 0);
					 return '00.01'
				 }
			 }
		 }
	        
		 return aux;
	 });
}

function fSumaPorcentaje(){
	var auxSuma = 0.0;
	$( ".auxPorcen" ).each(function( index ) {
		
		if(  ! $( this ).val() == "" ){
			auxSuma = auxSuma + parseFloat($( this ).val());
		}
	});
	$('#porcenPartiTot').val(auxSuma);
}

function addSeguidor(){
	if( $('.rowCoaseguradorSeguidor').length < 9 ) {
		
		$.post(getCoaseguradoresURL, {})
			.done(
				function(data) {
					var response = JSON.parse(data);
					
					console.log(response);
					
					if(response.code == 0) {
						
						countSeguidor++;
						
						$( ".contentCoasegurador" ).append( `
								<div  class="row rowCoaseguradorSeguidor rowCoasegurador`+countSeguidor+`">
									<div class="col-md-3 text-center">
										<p class="mt-4">`+label.es.seguidor+`</p>
									</div>
									<div class="col-md-6">
									<div class="md-form form-group">
											<select name="coaseSeguidor`+countSeguidor+`" id="coaseSeguidor`+countSeguidor+`" class="mdb-select form-control-sel colorful-select dropdown-primary coasegurador" searchable="Buscar...">
												<option value="-1" selected>Seleccione una opción</option>
											</select>
											<label for="coaseSeguidor`+countSeguidor+`">
												Nombre
											</label>
										</div>
									</div>
									<div class="col-md-2">
										<div class="md-form form-group">
											<input id="porcenParti`+countSeguidor+`" type="text" name="porcenParti`+countSeguidor+`" class="form-control auxPorcen">
											<label for="porcenParti`+countSeguidor+`">
												% de participación
											</label>
										</div>
									</div>
									<div class="col-md-1 mt-4">
										<a onclick="deleteCoaseg(`+countSeguidor+`)" class="btn-floating blue-gradient mt-0 waves-effect waves-light"><i class="fas fa-trash-alt"></i></a>
									</div>
								</div>
							` );
							
							
							$.each(response.personas, function(key, value) {
								$("#coaseSeguidor" + countSeguidor).append('<option value="'+ value.idPersona + '" codigo="' + value.codigo + '">' + value.nombre + '</option>')
							});
							
							$("#coaseSeguidor" + countSeguidor).materialSelect();
							
							$("select.coasegurador").change();
							
							
					}
					else{
						showMessageError('.navbar', 'Hubo un error al obtener el listado de Coaseguradores. ' + response.msg, 0);
					}
			});
		
	}
	else {
		showMessageError('.navbar', 'Máximo 10 Coaseguradores', 0);
	}
}

function deleteCoaseg(count){
	$('.rowCoasegurador'+count).remove();
}

function goTofacultativo(){
	$('#btn3').click();
}

function btnAddFile(){
	if( $('#divFiles .rowAuxDoc').length < 10 ){
		$( "#divFiles" ).append( `
				<div class="row mt-3 d-flex justify-content-center rowAuxDoc countDoc`+countDocs+`">
				<div class="col-md-3">
				<div class="md-form">
				<div class="file-field">
				<a class="btn-floating blue-gradient mt-0 pull-left">
				<i class="fas fa-cloud-upload-alt" aria-hidden="true"></i>
				<input type="file" class="inFile"
					accept="application/msword, application/pdf, application/vnd.openxmlformats-officedocument.wordprocessingml.document" />
				</a>
				<div class="file-path-wrapper">
				<input class="file-path validate" type="text" placeholder="Examinar">
				</div>
				</div>
				</div>
				</div>
				<div class="col-md-1">
				<div class="md-form">
				<a onclick="deleteDivFile(`+countDocs+`);" class="btn-floating blue-gradient mt-0 pull-right">
				<i class="fas fa-trash-alt" aria-hidden="true"></i>
				</a>
				</div>
				</div>
				</div>
		` );
		countDocs++;		
	}
	else{
		showMessageError('.navbar', 'Máximo 10 archivos', 0);
	}
}

function deleteDivFile(count){
	$('#divFiles .countDoc'+count).remove();
}

$("#btnNext").click(function(){
	
	if(parseFloat($("#porcenPartiTot").val()) != 100) {
		showMessageError('.navbar', "La suma de los porcentajes de participación debe ser del 100%", 
				0);
		
		$(".auxPorcen").addClass('invalid');
	}
	else {
		var auxCoaseguradores = [];
		
		var auxEntrada = new Object();
		
		var error = false;
		
		var validaTMX = false;
		
		if($("#coaseLider").val() == -1) {
			
			$("#coaseLider").siblings("input").addClass('invalid');
			error = true;
		}
		
		if($("#coaseLider option:selected").attr('codigo') == '000000000001') {
			validaTMX = true;
		}
		
		if(valIsNullOrEmpty($("#porcenParti").val())) {
			$("#porcenParti").addClass('invalid');
			error = true;
		}
		
		auxEntrada.p_coasegurador = $("#coaseLider").val();
		auxEntrada.p_participacion = $("#porcenParti").val();
		auxEntrada.p_tipoReasegurador = 7288;
		auxEntrada.p_orden = 1;
		auxEntrada.p_habilitado = 0;
		
		auxCoaseguradores.push(auxEntrada);
		
		$.each($(".rowCoaseguradorSeguidor"), function(key, value) {
			
			auxEntrada = new Object();
			
			if($(value).find('select').val() == -1) {
				
				$(value).find('select').siblings("input").addClass('invalid');
				error = true;
			}
			
			if($(value).find('select option:selected').attr('codigo') == '000000000001') {
				validaTMX = true;
			}
			
			if(valIsNullOrEmpty($(value).find('.auxPorcen').val())) {
				$(value).find('.auxPorcen').addClass('invalid');
				error = true;
			}
			
			auxEntrada.p_coasegurador = $(value).find('select').val();
			auxEntrada.p_participacion = $(value).find('.auxPorcen').val();
			auxEntrada.p_tipoReasegurador = 7289;
			auxEntrada.p_orden = key + 2;
			auxEntrada.p_habilitado = 0;
			
			auxCoaseguradores.push(auxEntrada);
		});
		
		console.log(auxCoaseguradores);
		
		if(!validaTMX) {
			showMessageError('.navbar', 'Tokio Marine no está participando, favor de seleccionarlo como Coasegurador', 0);
			return;
		}
		
		if(!error) {
		
			$.post(guardaInfoCoaseguroURL, {
				cotizacion: infCotizacion.cotizacion,
				version: infCotizacion.version,
				coaseguradores: JSON.stringify(auxCoaseguradores)
			}).done(function(data) {
				
				console.log(data);
				
				var url = new URL(window.location.href);
				var data = new FormData();
				var auxiliarDoc = '{';
				var docCompletos = true;
				var totArchivos = 0;
				
				$.each($('.inFile'), function(k, f) {
					
					if($(f).val() != "") {
					
						var file = f.files[0];
						if (f.files.length > 0) {
							data.append("file-" + totArchivos, file);
							var nomAux = file.name.split('.');
							if (totArchivos == 0) {
								auxiliarDoc += '\"file-' + totArchivos + '\" : {';
							} else {
								auxiliarDoc += ', \"file-' + totArchivos + '\" : {';
							}
							auxiliarDoc += '\"nom\" : \"' + nomAux[0] + '\",';
							auxiliarDoc += '\"iddoc\" : \"' + $(f).attr("iddoc") + '\",';
							auxiliarDoc += '\"idcatdet\" : \"' + 7308 + '\",';
							auxiliarDoc += '\"ext\" : \"' + nomAux[1] + '\"}';
							$(f).parent().addClass('btn-green');
							$(f).parent().removeClass('btn-blue');
							totArchivos++;
						} else {
							$(f).parent().addClass('btn-orange');
							$(f).parent().removeClass('btn-blue');
							docCompletos = false;
						}
					}
				});
				auxiliarDoc += '}';
				
				console.log("totArchivos : " + totArchivos);
				
				data.append('auxiliarDoc', auxiliarDoc);
				data.append('comentarios', $('#mdlFileComentarios').val());
				data.append('folio', infCotizacion.folio);
				data.append('cotizacion', infCotizacion.cotizacion);
				data.append('version', infCotizacion.version);
				data.append('modo', infCotizacion.modo);
				data.append('url', url.origin + url.pathname);
				data.append('url2', url.origin);
				data.append('totArchivos', totArchivos);
				
				$.ajax({
					url: guardaDocCoaseguroURL,
					data: data,
					processData: false,
					contentType: false,
					type: 'POST',
					success: function() {
						$("#btnCancel").click();
					},
					fail: function() {
						showMessageError('#modalArchivos .modal-header', 'Error al enviar la información ', 0);
						hideLoader();
					}
				});
			});
		
		}
		else {
			showMessageError('.navbar', 'Complete la información faltante', 0);
		}
	}
});

$("#btnCancel").click(function() {
	
	$.post(regresaCotizadorURL, {
		infCoti: infCotiURL
	}).done(function(data) {
		
		var response = JSON.parse(data);
		
		window.location = response.url + "=" + response.infoCot;
	});
});

var listMimetypeValid = [
	"application/msword", "application/pdf",
	"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
	"image/jpeg",
	"image/png"
];

$( "#divFiles" ).on('change', '.inFile', function() {
	var tamano = $(this)[0].files[0].size;
	var tipo = $(this)[0].files[0].type;
	
	console.log("tamano: " + tamano)
	
	if(listMimetypeValid.indexOf(tipo) < 0) {
		
		if(($(this)[0].files[0].name.indexOf('.msg') < 0) && tipo == "") {
		
			showMessageError( '.navbar', "Error, el tipo del archivo no es valido", 0 );
			$(this).val("");
			
		}
	}
	
});

$(".contentCoasegurador").on('change', 'select.coasegurador', function(event, documentReady) {
	
	console.log($(this).val());
	
	var auxVal = $(this).val();
	var idFieldAux = $(this).attr('id');
	
	if(auxVal != -1) {
		$.each($("select.coasegurador"), function(key, value) {
			
			if(idFieldAux != $(this).attr('id')) {
				console.log($(this).attr('id'));
				
				$.each($(this).find('option'), function(key, value) {
					if($(this).val() == auxVal) {
						$(this).attr('disabled', true);
					}
				});
				
				if(!documentReady) {
				
					$(this).materialSelect();
				}
			}
		});
	}
	
});

function valIsNullOrEmpty(value) {
	if (value === undefined) {
		return true;
	}
	value = value.trim();
	return (value == null || value == "null" || value === "");
}