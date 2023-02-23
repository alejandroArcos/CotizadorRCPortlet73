<%@ include file="../init.jsp"%>

<div class="row">
	<div class="col-sm-12 text-center">
		<form action="rea-coa" method="POST">
			<input type="hidden" id="modoVista" name="modoVista" value="1">
			<button class="btn btn-pink" id="btn1" type="submit">Colocación de Coaseguro</button>
		</form>
	</div>
	<div class="col-sm-12 text-center">
		<form action="rea-coa" method="POST">
			<input type="hidden" id="modoVista" name="modoVista" value="2">
			<button class="btn btn-pink" id="btn2" type="submit">Colocación de Reaseguro</button>
		</form>
	</div>
	<div class="col-sm-12 text-center">
		<form action="rea-coa" method="POST">
			<input type="hidden" id="modoVista" name="modoVista" value="3">
			<button class="btn btn-pink" id="btn3"type="submit">Reaseguro Facultativo</button>
		</form>
	</div>
</div>