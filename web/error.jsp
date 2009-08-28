<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://www.hdiv.org/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.hdiv.org/spring/tags/form" %>
<tr>
	<td colspan="5"><a href="<c:url value="/index.jsp"/>">Index</a></td>
</tr>
<form:form commandName="userModel" method="POST">
<form:errors path="*" ></form:errors>
</form:form>