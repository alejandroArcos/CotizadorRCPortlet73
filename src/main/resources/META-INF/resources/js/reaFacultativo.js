$( '.divRdoType .form-check-input' ).click( function(e) {
	if ($( this ).val() == "0") {
		muestraCampos( ".in_rea" );
		ocultaCampos( ".in_inter" );
		$(".in_inter").addClass("d-none");
	} else if($( this ).val() == "1") {
		muestraCampos( ".in_inter" );
		muestraCampos( ".in_rea" );
	}
} );

function llenaDatos(){
	if($("#radio_rea").is(":checked")){
		muestraCampos( ".in_rea" );
		ocultaCampos( ".in_inter" );
		$(".in_inter").addClass("d-none");
	}else if($("#radio_int").is(":checked")){		
		muestraCampos( ".in_inter" );
		muestraCampos( ".in_rea" );
	}
}

function bloqueaRadio(){
	deshabilitaRadio(".divRdoType", true);
}

$( '#btnIntermediario' ).click(function() {
	clickButton = true;
	$( '#radio_int' ).trigger( 'click' );		
	$(".in_rea .form-control:input:text").val("");
	$("#modalIntermediario").modal("hide");
} );


function ocultaCampos(selector){
	$(selector).addClass("d-none");
	$(selector).removeClass("d-block");
}

function muestraCampos(selector){
	$(selector).addClass("d-block");
	$(selector).removeClass("d-none");
}

function activaCampos(campo){
	if(valIsNullOrEmpty($(campo).val())){
		$(campo).siblings('label').removeClass('active');
	}else{
		$(campo).siblings('label').addClass('active');
	}
}
