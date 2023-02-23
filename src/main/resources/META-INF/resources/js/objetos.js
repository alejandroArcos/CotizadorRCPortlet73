var countSeguidor = 0;

const msj = {
		es : {
			errorInformacion : "Error al  cargar la información",
			catSinInfo: "Catalogo sin información",
	        campoRequerido: "El campo es requerido",
	        faltaInfo: "Hace falta información requerida",
	        errorGuardar: "Error al guardar su información"
		}
};

const label = {
		es: {
			seguidor : "Coasegurador Seguidor"
		}
}

const formatter = new Intl.NumberFormat('en-US', {
	  style: 'currency',
	  currency: 'USD',
	  minimumFractionDigits: 2
});