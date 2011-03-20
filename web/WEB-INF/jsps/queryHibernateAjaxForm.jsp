<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsps/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<head>
	<title>SmartPagination——高级查询AJAX</title>
	<link type="text/css" href="${ctxPath}/css/smoothness/jquery-ui-1.8.9.custom.css" rel="Stylesheet" />  
    <script type="text/javascript" src="${ctxPath}/js/jquery-1.5.min.js"></script>
    <script type="text/javascript" src="${ctxPath}/js/jquery-ui-1.8.9.custom.min.js"></script>
    <script type="text/javascript" src="${ctxPath}/js/jquery.form.js"></script>
	<script>
	$(function() {
	    $("#query").click(function(){
	        $("#queryForm").ajaxSubmit({
		        success:showResponse,
		        type:"POST",
		        url:"${ctxPath}/sample/queryHibernateAjax.htm"
		        });
		});

	    function showResponse(result){
		    $("#dataDiv").html(result);
	    } 
	});
	</script>
</head>
<body>
	<table width="100%" height="100%" border="0" align="center" valign="top" cellpadding="0" cellspacing="0">
		<form id="queryForm">
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
					<td><input name="userName" maxlength="50"/>like
                        <input type="checkbox" name="userNameLike"/>
					</td>
					<td align="right">Email:</td>
					<td><input name="email" maxlength="50"/>(LIKE)
					</td>
				</tr>
				<tr>
					<td align="right">RealName:</td>
					<td><input name="realName" maxlength="50"/>(LIKE)
					</td>
					<td align="right">Sex:</td>
					<td><select path="sex">
                   			<option value="m" label="男"/>
                   			<option value="f" label="女"/>
               			</select>    <input type="button" id="query" value="查询" />
					</td>
				</tr>
			</table>
		</td></tr>
		</form>
		<tr>
		<td id="dataDiv">xx</td></tr>
	</table>
</body>