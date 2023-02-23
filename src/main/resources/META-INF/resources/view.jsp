<%@ include file="./init.jsp" %>

<c:choose>
	
	<c:when test="${modoVista == 0}">
		<jsp:include page="pantallas/paso3.jsp" />
	</c:when>
	
	
	<c:when test="${modoVista == 1}">
		<jsp:include page="pantallas/colocacionCoaseguro.jsp" />
	</c:when>
	
	<c:when test="${modoVista == 2}">
		<jsp:include page="pantallas/colocacionReaseguro.jsp" />
	</c:when>
	
	<c:when test="${modoVista == 3}">
		<jsp:include page="pantallas/reaseguroFacultativo.jsp" />
	</c:when>
	
	<c:otherwise>
		<jsp:include page="pantallas/btns.jsp" />
	</c:otherwise>

</c:choose>