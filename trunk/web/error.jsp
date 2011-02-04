<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp" %>
<tr>
	<td colspan="5"><a href="<c:url value="/index.jsp"/>">Index</a></td>
</tr>
<form:form commandName="userModel" method="POST">
<form:errors path="*" ></form:errors>
</form:form>