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
</head>
<body>
<div style="padding:5px;">
  <h4 class="title">Discount Set</h4>
</div>
<br/>
<div style="padding:0 5px 5px 5px;">
<div class="clearboth"></div>
	<div style="height:60px;width:96%;overflow:auto;">
		<span>Pls field the discount follow.</span>&nbsp;&nbsp;&nbsp;
		<br/>
		<input type="text" id="discount" size=12/>
    </div>
    <br>  
    <div style="text-align:center;">
  			<input type="button" id="Confirm" value="Confirm"  class="cls-button" "/> 
   			&nbsp;&nbsp;&nbsp;<input type="button" value="Close" id="Close" class="cls-button" onclick="window.returnValue = ''; window.close();"/>
   </div>
 </div>
<script type="text/javascript">
    $(function(){
        $("#Confirm").click(function(){
        	var checkValue = $('#discount').val();
        	if(checkValue==''){
        		alert('value can not be null!');
        	}
        	window.returnValue = checkValue; 
         	window.close();
        });
    });

</script>
</body>
</html>