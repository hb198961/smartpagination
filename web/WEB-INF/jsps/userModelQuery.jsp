<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<head>
	<title>SmartPagination——高级查询</title>
</head>

<body>
	<table width="100%" height="100%" border="0" align="center" valign="top" cellpadding="0" cellspacing="0">

		<form:form commandName="userModel" method="POST">
		<form:errors path="*"></form:errors>
		<tr height="55px" valign="top"><td>
			<table width="50%" align="center" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="15%"></td>
					<td width="30%"></td>
					<td width="15%"></td>
					<td></td>
				</tr>
				<tr>
					<td align="right">UserName:</td>
					<td><form:input path="userName" id="userName" maxlength="50"/>like
                        <form:checkbox path="userNameLike" id="userNameLike"/>
						<form:errors path="userName"/>
					</td>
					<td align="right">Email:</td>
					<td><form:input path="email" id="email" maxlength="50"/>(LIKE)
						<form:errors path="email"/>
					</td>
				</tr>
				<tr>
					<td align="right">RealName:</td>
					<td><form:input path="realName" id="realName" maxlength="50"/>(LIKE)
						<form:errors path="realName"/>
					</td>
					<td align="right">Sex:</td>
					<td><form:select path="sex">
                   			<form:option value="m" label="男"/>
                   			<form:option value="f" label="女"/>
               			</form:select>    <input type="submit"/>
					</td>
				</tr>
			</table>
		</td></tr>
		</form:form>

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
						<paging:pageHead url="/sample/queryHibernate.htm" orderBy="userName" >userName</paging:pageHead>
					</td>
					<td>
						<paging:pageHead url="/sample/queryHibernate.htm" orderBy="realName" >realName</paging:pageHead>
					</td>	
					<td>
						<paging:pageHead url="/sample/queryHibernate.htm" orderBy="email" >email</paging:pageHead>
					</td>	
					<td>
						<paging:pageHead url="/sample/queryHibernate.htm" orderBy="birth" >birth</paging:pageHead>
					</td>
					<td>
						<paging:pageHead url="/sample/queryHibernate.htm" orderBy="sex" >sex</paging:pageHead>
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
					<tr><td class="listViewHRS1" colSpan="5"></td></tr>
				</c:forEach>
				<tr>
					<td colspan="5"><paging:pageBar url="/sample/queryHibernate.htm"/></td>
				</tr>
				</c:if>
				<tr>
					<td colspan="5"><a href="<c:url value="/sample/listHibernate.htm"/>">简单查询</a></td>
				</tr>
				<tr>
					<td colspan="5"><a href="<c:url value="/index.jsp"/>">首页</a></td>
				</tr>
			</table>
		</td></tr>
	</table>
</body>