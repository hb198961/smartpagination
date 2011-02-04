<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp" %>
<tr>
	<td colspan="5"><a href="<c:url value="/listHibernate.htm"/>">Simple query[Hibernate]</a>|</td>
</tr>
<tr>
	<td colspan="5"><a href="<c:url value="/queryHibernate.htm"/>">Advanced query[Hibernate]</a>|</td>
</tr>
<br/>
<tr>
	<td colspan="5"><a href="<c:url value="/listJdbc.htm"/>">Simple query[JDBC]</a>|</td>
</tr>
<tr>
	<td colspan="5"><a href="<c:url value="/queryJdbc.htm"/>">Advanced query[JDBC]</a>|</td>
</tr>
<br/>
<tr>
	<td colspan="5"><a href="<c:url value="/listIbatis.htm"/>">Simple query[iBATIS]</a>|</td>
</tr>
<tr>
	<td colspan="5"><a href="<c:url value="/queryIbatis.htm"/>">Advanced query[iBATIS]</a>|</td>
</tr>
<br/>
<tr>
	<td colspan="5"><a href="<c:url value="/initData.htm"/>">Init data</a></td>
</tr>