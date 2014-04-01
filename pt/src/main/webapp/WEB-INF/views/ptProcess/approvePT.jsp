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
<form action="${ctx}/ptQuery/ptApprove" method="post" id="ptQueryForm">
	
<div>
<div style="padding:0 5px 5px 5px;">
<h4 class="title">Approve a PT</h4>
<br/>
<div style="padding:5px;">
  <h4 class="title">PT Query</h4>
<table class="table_A">
        <thead>
             <tr>
		      <td>Account :</td>
		      <td><input type="text" name="account" value="${model.account}"></td>
		      <td>depot:</td>
		      <td><selec name="depot" value="${model.depot}">
		      <option value="">请选择</option> 
		      <c:forEach items="${depotList}" var="depot">
		      		<option value="${depots}" <c:if test="${depots==model.depot}">selected</c:if>>${depots}</option> 
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
		      <td><input type="button" value="Query" id="query" class="cls-button"/></td>
		     </tr>
        </thead>
    </table>
    
</div>
<h4 style="float:left">PT List</h4>
<div class="clearboth"></div>
<table class="table_B" width="100%">
        <thead>
			<tr>
                <th>PT Application Reference#</th>
                <th>Application Date</th>
                <th>Account</th>
                <th>Customer Name</th>
                <th>Channel</th>
                <th>Territory</th>
                <th>Operate</th>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${businessList}" var="business">
				<tr>
					<td>
						<a id="showDetail" href="#" onclick="showDetailTariff('${business.id}')">${business.applicationReference}</a>
					</td>
					<td><fmt:formatDate  value="${business.applicationDate}" pattern="yyyy-MM-dd"></fmt:formatDate ></td>
					<td>${business.account}</td>
					<td>${business.cusName}</td>
					<td>${business.channel}</td>
					<td>${business.territory}</td>
					<td align="left">
					<a id="showDetail" href="#" onclick="showDetail('${business.id}')">Detail</a>
					&nbsp;&nbsp;
					<a id="appeal" href="#" onclick="stateLog('${business.id}')">State Log</a>
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
       
    });

    function stateLog(val){
   	  window.showModalDialog('${ctx}/ptQuery/stateLog/'+val,'','dialogHeight:300px; dialogWidth: 400px;');
     }
    function showDetailTariff(val){
    	window.location.href="${ctx}/ptQuery/tariffPT/"+val;
	}
    
	function showDetail(val){
		window.location.href="${ctx}/ptQuery/summaryInfoProcess/"+val;
	}
	
</script>
</body>
</html>