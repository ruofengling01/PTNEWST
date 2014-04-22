<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.tnt.pt.util.PTPARAMETERS"%>
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
<script type="text/javascript">
$(document).ready(function(){

	/*$(".datepicker").datePicker({
		inline:true,
		selectMultiple:false
	});
	
	$("#datepicker").datePicker({
		clickInput:true
	});*/
	
});
</script>
<form action="${ctx}/ptQuery/queryPT" method="post" id="ptQueryForm">

<div style="padding:5px;">
  <h4 class="title">PT Query</h4>
<table class="table_A">
        <thead>
             <tr>
		      <td>Account :</td>
		      <td><input type="text" name="account" value="${model.account}"></td>
		      <td>Depot:</td>
		      <td><select name="depot" value="${model.depot}">
		      <option value="">请选择</option> 
		      <c:forEach items="${depotList}" var="depots">
		      	<option value="${depots}" <c:if test="${depots==model.depot}">selected</c:if>>${depots}</option> 
		      </c:forEach>
		      </select></td>
		     </tr>
		     <tr>
		      <td>Channel :</td>
		      <td><input type="text" name="channel" value="${model.channel}"></td>
		      <td>Status:</td>
		      <td><select name="state" value="${model.state}">
		      <c:forEach items="${statusList}" var="status">
		      	<option value="${status}" <c:if test="${status==model.state}">selected</c:if> >${status}</option>
		      </c:forEach>
		      </select></td>
		     </tr>
		     <tr>
		      <td>Application Date:</td>
		      <td>From： <input type="text" name="startDate" value="${model.startDate}" onclick="new Calendar().show(this,this)" >&nbsp;&nbsp; 
		      To：<input type="text" name="endDate" value="${model.endDate}" onclick="new Calendar().show(this,this)" ></td>
		      <td>PT Application Reference : </td>
		      <td><input type="text" name="applicationReference" value="${model.applicationReference}"></td>
		     </tr>
		     <tr>
		      <td>Rev/Mon: </td>
		      <td>From： <input type="text" name="revMonStart" value="${model.revMonStart}" >&nbsp;&nbsp; To：<input type="text" name="revMonEnd" value="${model.revMonEnd}"></td>
		      <td></td>
		      <td>
		      <input type="button" value="Query" id="query" class="cls-button"/>&nbsp;&nbsp;&nbsp;
		      <input type="button" value="export" id="export" class="cls-button"/>
		      </td>
		     </tr>
        </thead>
    </table>
</div>
<div>
<div style="padding:0 5px 5px 5px;">
<h4 style="float:left">PT List</h4>
<div class="clearboth"></div>
<table class="table_B" width="100%">
        <thead>
			<tr>
                <th>PT Application Reference</th>
                <th>Application Date</th>
                <th>Account</th>
                <th>Customer Name</th>
                <th>Depot</th>
                <th>Channel</th>
                <th>Top</th>
                <th>Territory</th>
                <th>Status</th>
                <th>Operation</th>
             </tr>
        </thead>
        <tbody>
             <c:forEach items="${businessList}" var="business">
				<tr>
					<td>
						<a id="showDetail" href="#" onclick="showDetail('${business.id}','${business.state}')">${business.applicationReference}</a>
					</td>
					<td><fmt:formatDate  value="${business.applicationDate}" pattern="yyyy-MM-dd"></fmt:formatDate ></td>
					<td>${business.account}</td>
					<td>${business.cusName}</td>
					<td>${business.depotCode}</td>
					<td>${business.channel}</td>
					<td>${business.consStop}</td>
					<td>${business.territory}</td>
					<td>${business.state}</td>
	                <td><a id="appeal" href="#" onclick="stateLog('${business.id}')">State Log</a></td>
			  
			   </tr>
			</c:forEach>
        </tbody>
    </table>
    <%@ include file="/WEB-INF/tags/navigate.jsp"%>
  <br>    
</form>
</div>
<script type="text/javascript">
    $(function(){
        $("#query").click(function(){
        	document.forms[0].action="${ctx}/ptQuery/queryPT";
        	document.forms[0].submit();
        });
        
        $("#export").click(function(){
        	document.forms[0].action="${ctx}/ptQuery/exportBatchExcel";
    		document.forms[0].submit();
    		document.forms[0].action="${ctx}/ptQuery/queryPT";
        });
    });

	function showDetail(val,state){
		if(state=='<%=PTPARAMETERS.PROCESS_SATE[9]%>'){
			alert('The state is '+'<%=PTPARAMETERS.PROCESS_SATE[9]%>'+',can not see detail info!');
			return;
		}
		window.location.href="${ctx}/ptQuery/tariffPT/"+val;
	}
    
    function stateLog(val){
      	//window.location.href="${ctx}/ptQuery/stateLog/"+val;
  	  	window.showModalDialog('${ctx}/ptQuery/stateLog/'+val,'','dialogHeight:300px; dialogWidth: 650px;');
    }
    
    function appeal(val){
    	alert('Email send success ! ');
		document.forms[0].action="${ctx}/ptQuery/appeal?applicationReference="+val;
		document.forms[0].submit();
	}
</script>
</body>
</html>