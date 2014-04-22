<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.tnt.pt.util.PTPARAMETERS"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%
response.setHeader("Pragma","No-cache");    
response.setHeader("Cache-Control","no-cache");    
response.setDateHeader("Expires", -10);   
%> 
<title>New PT-Summary Info</title>
<link href="${ctx}/static/styles/main.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
</head>

<style type="text/css">
<!--
	.modify tr th, .modify tr td {
	text-align:left;
	font-size:14px;
}
	.modify tr th {
	background:#EFEFEF;
}
-->
</style>
<script language="JavaScript">
	var date = new Date();
	document.getElementById('appDate').value=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();	
</script>
<body>
<form action="#" id="summaryInfo" method="post">
	<div style="padding:5px;">
  <h4 class="title">New PT-Summary Info</h4>
  <%--***************隐藏域********** --%>
    <input type="hidden" value="${business.customerId}" name="business.customerId">
    <input type="hidden" value="${business.id}" name="business.id">
    <input type="hidden" value="${business.zoneType}" name="business.zoneType">
    <%--***************隐藏域********** --%>
  <table class="modify">
    <tr>
      <th width="20%">Application Date:</th>
      <td width="40%"><fmt:formatDate value="${business.applicationDate}" pattern="yyyy-MM-dd" /></td>
      <th width="20%">Depot:</th>
      <td>${business.depotCode}</td>
    </tr>
    <tr>
      <th>A new PT Customer?</th>
      <td>${business.isNewCus}</td>
      <th>Territory:</th>
      <td>${business.territory}</td>
    </tr>
    <tr>
	  <th>PT Application Reference #: </th>
      <td>${business.applicationReference}</td>
      <th>Account #:</th>
      <td>${customer.account}</td>
    </tr>
    <tr>
      <th>Customer Name</th>
      <td>${customer.cusName}</td>
      <th>Channel:</th>
      <td>${customer.channel}</td>
    </tr>
    <tr>
      <th>Industry</th>
      <td colspan="3">${customer.industry}</td>
        
    </tr>
    <tr>
      <th>Current Service Provider</th>
      <td colspan="3">${customer.serviceProvider}</td>
    </tr>
   
  </table>
  <table class="modify">
    <tr>
      <th width="45%">Is the customer on fuel surcharge exemption or deduction now?</th>
      <td width="15%">${customer.isFuleDeduction }</td>
      <th width="20%">The Current fuel surcharge</th>
      <td>${customer.fuelSurcharge }</td>
    </tr>
    
    
  </table>
  <table class="modify">
    <tr>
      <th width="45%">Term of Payment:</th>
      <td colspan="3" >
      <span style="background-color:yellow"><B>${payment}</B></span>
      </td>
    </tr>
    <tr>
      <th width="45%">Is customer a mainly document sender?</th>
      <td width="15%">${business.isDocumentSender}</td>
      <th width="20%">Cons/Stop</th>
      <td>${business.consStop}</td>
    </tr>
     <tr>
      <th >Prdouct Description(eg:digital cameral)</th>
      <td colspan="3">${business.description}</td>
      
    </tr>
     <tr>
      <th width="20%">Reason for the PT:</th>
      <td colspan="3">${business.reson}</td>
    </tr>
  </table>
  </div>
 <br />
 <div style="padding:5px;">
 <table>
 <tr>
 <td style="background-color:#F5C07B">Summary </td><td style="background-color:#EFEFEF">Rev(CNY)</td><td>${zoneSummary.revM}/M</td>
 <td>${zoneSummary.revY}/Y</td><td style="background-color:#EFEFEF">Cons</td><td>${zoneSummary.consM}/M</td><td>${zoneSummary.consY}/Y</td>
  <td style="background-color:#EFEFEF">Kilo(kg) </td><td>${zoneSummary.kiloM}/M</td><td>${zoneSummary.kiloY}/Y</td>
 </tr>
 </table>
 </div>
 <br />
<div style="padding:0 5px 5px 5px;">
<div class="clearboth"> </div>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="7" style="text-align:center;">Summary by GEO Zone</th>
            </tr>
			<tr align="center" >
                <th rowspan="2" style="text-align:center;">GEO Zone</th>
				<th colspan="2" style="text-align:center;">Rev(CNY)</th>
				<th colspan="2" style="text-align:center;">Cons</th>
				<th colspan="2" style="text-align:center;">Kilo(kg)</th>
            </tr>
            <tr>
                <th>Per Month</th>
                <th>Per Year</th>
                <th>Per Month</th>
                <th>Per Year</th>
				<th>Per Month</th>
                <th>Per Year</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${geoSummaryList}" var="geoSummary">
				<tr>
					<td width="16%">${geoSummary.geoZone}</td>
					<td width="14%">${geoSummary.revM}</td>
					<td width="14%">${geoSummary.revY}</td>
					<td width="14%">${geoSummary.consM}</td>
					<td width="14%">${geoSummary.consY}</td>
					<td width="14%">${geoSummary.kiloM}</td>
					<td width="14%">${geoSummary.kiloY}</td>
			   </tr>
		 </c:forEach>
          </tbody>
</table>
<br>
<table class="table_B" width="100%">
       <thead>
			<tr align="center">
                <th colspan="7" style="text-align:center;">Summary by PT Zone</th>
            </tr>
			<tr align="center" >
                <th rowspan="2" style="text-align:center;">PT Zone</th>
				<th colspan="2" style="text-align:center;">Rev(CNY)</th>
				<th colspan="2" style="text-align:center;">Cons</th>
				<th colspan="2" style="text-align:center;">Kilo(kg)</th>
            </tr>
            <tr>
                <th>Per Month</th>
                <th>Per Year</th>
                <th>Per Month</th>
                <th>Per Year</th>
				<th>Per Month</th>
                <th>Per Year</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${zoneSummaryList}" var="zoneSummary">
				<tr>
					<td  width="16%">${zoneSummary.zoneType}</td>
					<td  width="14%">${zoneSummary.revM}</td>
					<td width="14%">${zoneSummary.revY}</td>
					<td width="14%">${zoneSummary.consM}</td>
					<td width="14%">${zoneSummary.consY}</td>
					<td width="14%">${zoneSummary.kiloM}</td>
					<td width="14%">${zoneSummary.kiloY}</td>
			   </tr>
		 </c:forEach>
          </tbody>
</table>
<br>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile- 15D/12D/10D/09D(% off full tariff)</th>
            </tr>
            <tr>
                <th>PRODUCT</th>
				<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${documentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<%--<td>${weightBand.chargeableWeight}</td>
					--%><c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td>${discountMap[key]}%</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
        </tbody>
    </table>
	<br />
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-15N/12N/10N/09N</th>
            </tr>
            <tr>
                <th>Weightband</th>
				<%--<th  width="8%"> Chargeable Weight(kg)</th>
                 --%><c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ndocumentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<%--<td>${weightBand.chargeableWeight}</td>
					--%><c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td>${discountMap[key]}%</td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
<br />
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-48N</th>
            </tr>
            <tr>
                <th>Weightband</th>
				<%--<th  width="8%"> Chargeable Weight(kg)</th>
                --%><c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${eonomyList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<%--<td>${weightBand.chargeableWeight}</td>
					--%><c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td>${discountMap[key]}%</td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>

<div style="padding:0 5px 5px 5px;">
<div class="clearboth"> </div>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="14" style="text-align:left;">New PT-HW rate Profile-15N(RMB per kilo)</th>
            </tr>
            <tr>
                <th >Country-Depot</th>
                 <c:forEach items="${ndocumentList_}" var="weightBand">
					<th>${weightBand.name}</th>
			     </c:forEach>
			     <th></th>
            </tr>
        </thead>
        <tbody id="tb1">
        	<c:forEach items="${ndocumentCountrys}" var="country" varStatus="co">
				<tr id='tb1_${co.index}' align="center">
					<td>
					${country.countryCode}
					</td>
					<c:forEach items="${ndocumentList_}" var="weightBand" begin="0">
							<c:set var="key">${business.id}_${ndocument}_${weightBand.id}_${country.id}</c:set>
						    <td>${hwRateMap[key]}</td>
				   </c:forEach>
				   <td></td>
			   </tr>
			</c:forEach>
        </tbody>
</table>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="14" style="text-align:left;">New PT-HW rate Profile-48N(RMB per kilo)</th>
            </tr>
            <tr>
                <th >Country-Depot</th>
                 <c:forEach items="${eonomyList_}" var="weightBand">
					<th>${weightBand.name}</th>
			     </c:forEach>
            </tr>
        </thead>
        <tbody id="tb2">
        	<c:forEach items="${eonomyCountrys}" var="country" varStatus="co">
				<tr id='tb2_${co.index}' align="center">
					<td>
					${country.countryCode}
					</td>
					<c:forEach items="${eonomyList_}" var="weightBand" begin="0">
							<c:set var="key">${business.id}_${eonomy}_${weightBand.id}_${country.id}</c:set>
						    <td>${hwRateMap[key]}</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
        </tbody>
</table>
<br />
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="2" style="text-align:left;">Reason For The PT</th>
            </tr>
			
        </thead>
        <tbody>
            <tr>
                <td width="20%">Reason For The PT</td>
                <td>${business.reson}</td>
            </tr>
          </tbody>
</table>
  <div style="text-align: center">
	  <input type="button" value="Previous" class="cls-button" id="Previous" /> 
	  <c:if test="${isFollow=='NO'}">
			<c:if test="${payment=='SenderPay'}">
				&nbsp;&nbsp;&nbsp;<input type="button" value="Continue to RP" class="cls-button" id="toRP"/>
		    </c:if>
		    <c:if test="${payment=='ReceivePay'}">
				&nbsp;&nbsp;&nbsp;<input type="button" value="Close" class="cls-button" id="Close"/>
				&nbsp;&nbsp;&nbsp;<input type="button" value="Submit" class="cls-button" id="Submit"/>
		    </c:if>
	  </c:if>
  	  <c:if test="${isFollow!='NO'}">
		&nbsp;&nbsp;&nbsp;<input type="button" value="Close" class="cls-button" id="Close"/>
		&nbsp;&nbsp;&nbsp;<input type="button" value="Submit" class="cls-button" id="Submit"/>
	  </c:if>
   </div>
   <input type="hidden" id="isFollow" value="${isFollow}" name="isFollow">
   <input type="hidden" id="payment" value="${payment}" name="payment">
</form>
</div>
<script type="text/javascript">
	$(function(){
		 $("#Previous").click(function(){
			 if($('#isFollow').val()=='NO'&&$('#payment').val()=='<%=PTPARAMETERS.PAYMENT[1]%>'){
				 $('#payment').val('');
				 $("#summaryInfo").attr('action',"${ctx}/ptModify/summaryInfo");
				 $("#summaryInfo").submit();
              }else{
            	  $("#summaryInfo").attr('action',"${ctx}/ptModify/consProfile/${isHighWight}");
     			 $("#summaryInfo").submit();
              }
			 
		});
	});

	$(function(){
		$("#toRP").click(function(){
           	$("#summaryInfo").attr('action',"${ctx}/ptCreate/summaryInfo");
           	$("#summaryInfo").submit();
		});
		
		 $("#Close").click(function(){
			 if($('#isFollow').val()=='NO'&&$('#payment').val()=='<%=PTPARAMETERS.PAYMENT[0]%>'){
              	$("#summaryInfo").attr('action',"${ctx}/ptCreate/summaryInfo");
              	$("#summaryInfo").submit();
              }else{
            	  $.ajax({
  	                type:"GET",
  	                url:"${ctx}/ptCreate/finishPT",
  	                dataType:"text",      
  	                contentType:"application/json",   
  	                data:{businessId:'${business.id}'},
  	                success:function(data){
  	                	alert(data);
						
  	                },
  	                error:function(e) {
  	                    alert("error："+e);
  	                }
  	            });
              }
		});
		 
		 $("#Submit").click(function(){
			 if($('#isFollow').val()=='NO'&&$('#payment').val()=='<%=PTPARAMETERS.PAYMENT[0]%>'){
              	alert("Please click Close to RP !");
              }else{
            	  $.ajax({
  	                type:"GET",
  	                url:"${ctx}/ptApprove/salesSubmit",
  	                dataType:"text",
  	                contentType:"application/json",
  	                data:{businessId:'${business.id}'},
  	                success:function(data){
  	                	alert(data);
  	                	window.parent.location.reload();
  	                },
  	                error:function(e) {
  	                    alert("error："+e);
  	                }
  	            });
              }
	            
		});
	});
</script>
</body>
</html>