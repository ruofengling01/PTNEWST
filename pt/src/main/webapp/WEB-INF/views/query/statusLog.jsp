<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PT Query</title>
<link href="${ctx}/static/styles/main.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/static/js/calendar.js"></script>
</head>
<body>
<div style="padding:5px;">
  <h4 class="title">Role List</h4>
</div>
<br/>
<div style="padding:0 5px 5px 5px;">
<div class="clearboth"></div>
	<div style="height:200px;width:96%;overflow:auto;">
	<table class="table_B" width="100%">
        <thead>
			<tr>
                <th>Time</th>
                <th>User Name</th>
                <th>Oppion</th>
                <th>File</th>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${statusList}" var="exam">
				<tr>
				<td>${exam.showTime}</td>
				<td>${exam.userName}</td>
				<td>${exam.examOppion}</td>
				<td><a href="${exam.filePath}">${exam.fileName}</a></td>
			   </tr>
			</c:forEach>
        </tbody>
    </table>
    </div>
    <br>  
    <div style="text-align:center;">
  			<input type="button" id="Print" value="Print"  class="cls-button" onclick="window.print();"/> 
   			&nbsp;&nbsp;&nbsp;<input type="button" value="Close" id="Close" class="cls-button" onclick="window.close();"/>
   </div>
 </div>
</body>
</html>