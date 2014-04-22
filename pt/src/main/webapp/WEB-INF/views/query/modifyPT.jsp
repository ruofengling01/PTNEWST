<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="org.tnt.pt.util.PTPARAMETERS"%>
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
<form action="${ctx}/ptQuery/ptModify" method="post" id="ptQueryForm">
	<div style="padding:5px;">
	<h4 class="title">Modify a PT</h4>
<table class="table_A">
        <thead>
             <tr>
		      <td>Account :</td>
		      <td><input type="text" name="account" value="${model.account}"></td>
		      <td>Depot:</td>
		      <td><input type="text" name="depot" value="${model.depot}" readOnly="true"></td>
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
		      <td><input type="button" value="Query" id="query" class="cls-button"/></td>
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
                <th>Application date</th>
                <th>Account</th>
                <th>Customer Name</th>
                <th>Depot</th>
                <th>Channel</th>
                <th>Top</th>
                <th>Territory</th>
                <th>Status</th>
                <th>Operate</th>
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
					<td align="left">
					<a id="appeal" href="#" onclick="stateLog('${business.id}')">State Log</a>&nbsp;&nbsp;&nbsp;
					<a id="updatePT" href="#" onclick="updatePT('${business.id}')">Modify</a>
					&nbsp;&nbsp;&nbsp;
					<a id="deletePT" href="#" keyid="${business.id}" onclick="deletePT('${business.id}')">Delete</a>	
					</td>
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
         $("#ptQueryForm").submit();
        });
        $("[id=deleteP]").each(function(){
			$(this).click(function(){
	        	var id = $(this).attr("keyid");
	            $.ajax({
	                type:"POST",
	                url:"${ctx}/ptApprove/deletePT",
	                data:"id="+id,
	                success:function(data){
	                	alert('删除成功！');
	                },error:function(e) {
	                	alert("error："+e);
	                }
	            });
	        });
		});
    });

	function showDetail(val,state){
		if(state=='<%=PTPARAMETERS.PROCESS_SATE[9]%>'){
			alert('The state is '+'<%=PTPARAMETERS.PROCESS_SATE[9]%>'+',can not see detail info!');
			return;
		}
		window.location.href="${ctx}/ptQuery/tariffPT/"+val;
	}
	
	function updatePT(val){
		window.location.href="${ctx}/ptModify/updateCustomer/"+val;
	}
	
	/*function appeal(val){
		document.forms[0].action="${ctx}/ptQuery/sendEmail?id="+val;
		document.forms[0].submit();
	}*/
	function stateLog(val){
  	  window.showModalDialog('${ctx}/ptQuery/stateLog/'+val,'','dialogHeight:300px; dialogWidth: 650px;');
    }
	function deletePT(val){
		document.forms[0].action="${ctx}/ptQuery/deletePT?id="+val;
		document.forms[0].submit();
	}
	
</script>
</body>
</html>