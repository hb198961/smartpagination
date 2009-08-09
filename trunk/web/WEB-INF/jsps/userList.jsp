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
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td width="10%"></td>
					<td width="15%"></td>
					<td width="18%"></td>
					<td width="12%"></td>
					<td></td>
					<td width="20%"></td>
				</tr>
				<tr>
					<td>
						<paging:pagehead url="/list.htm" orderBy="userName">userName</paging:pagehead>
					</td>
					<td>
						<paging:pagehead url="/list.htm" orderBy="realName">realName</paging:pagehead>
					</td>	
					<td>
						<paging:pagehead  align="center" url="/list.htm" orderBy="email">email</paging:pagehead>
					</td>	
					<td>
						<paging:pagehead url="/list.htm" orderBy="sex" >sex</paging:pagehead>
					</td>
					<td>
						<paging:pagehead align="center" url="/list.htm" orderBy="birth">birth</paging:pagehead>
					</td>
				</tr>
				
				<c:if test="${userList !=null}">
				<c:forEach var="cItem" items="${userList}" varStatus="status">
					<tr>
						<td><a href="userView.uaas?id=${cItem.id}">${cItem.userName}</a></td>
						<td><c:out value="${cItem.realName}" /></td>
						<td><c:out value="${cItem.email}" /></td>
						<td ><c:out value="${cItem.sex}" /></td>
						<td align="center"><fmt:formatDate pattern="yyyy-MM-dd" value="${cItem.birth}"/></td>		
					</tr>
					<tr><td class="listViewHRS1" colSpan="6"></td></tr>
				</c:forEach>
				<tr>
					<td colspan="6"><paging:pagebar url="/list.htm" styleClass="pagebar"/></td>
				</tr>
				</c:if>
				
			</table>
		</td></tr>
	</table>
</body>