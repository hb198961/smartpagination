<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/smart_pagination.tld" prefix="paging"%>
<head>
	<title>简单查询</title>
</head>

<body>
	<table width="100%" height="100%" border="0" align="center" valign="top" cellpadding="0" cellspacing="0">
		
		<tr valign="top"><td align="left">
			<table width="50%" border="1" cellpadding="1" cellspacing="0" align="center">
				<tr>
					<td width="15%"></td>
					<td width="20%"></td>
					<td width="20%"></td>
					<td width="35%"></td>
					<td width="10%"></td>
				</tr>
				<tr>
					<td>
						<paging:pagehead url="/list.htm" orderBy="userName" enableHdiv="true">userName</paging:pagehead>
					</td>
					<td>
						<paging:pagehead url="/list.htm" orderBy="realName" enableHdiv="true">realName</paging:pagehead>
					</td>	
					<td>
						<paging:pagehead  align="center" url="/list.htm" orderBy="email" enableHdiv="true">email</paging:pagehead>
					</td>	
					<td>
						<paging:pagehead align="center" url="/list.htm" orderBy="birth" enableHdiv="true">birth</paging:pagehead>
					</td>
					<td>
						<paging:pagehead align="center" url="/list.htm" orderBy="sex" enableHdiv="true">sex</paging:pagehead>
					</td>
				</tr>
				
				<c:if test="${userList !=null}">
				<c:forEach var="cItem" items="${userList}" varStatus="status">
					<tr>
						<td>${cItem.userName}</td>
						<td>${cItem.realName}</td>
						<td>${cItem.email}</td>
						<td align="left"><fmt:formatDate pattern="yyyy-MM-dd hh:MM:ss,S" value="${cItem.birth}"/></td>
						<td >${cItem.sex}</td>		
					</tr>
					<tr><td class="listViewHRS1" colSpan="6"></td></tr>
				</c:forEach>
				<tr>
					<td colspan="5"><paging:pagebar url="/list.htm" styleClass="pagebar" enableHdiv="true"/></td>
				</tr>
				</c:if>
				
			</table>
		</td></tr>
	</table>
</body>