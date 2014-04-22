<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>New PT-Summary Info</title>
<link href="${ctx}/static/styles/main.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/styles/tab.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="${ctx}/uploadify/uploadify.css" type="text/css"></link>
<script type="text/javascript" src="${ctx}/uploadify/jquery.uploadify-3.1.min.js"></script>
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
<body>
<form action="#">
<div style="padding:5px;">
  <h4 class="title"> PT-Summary Info</h4>
  <table class="table_A">
  <tr>
  <td width="20%" align="left"><b>PT Approval Ref NO. &nbsp; :</b></td>
  <td align="left">${business.applicationReference}</td>
  </tr>
  <tr></tr>
  <tr> <td colspan="2"><b>Customer & Shipping Profile</b></td></tr>
  </table>
  <table class="tab">
    <tr>
      <td>Customer Name :</td>
      <td width="40%">${customer.cusName}</td>
	  <td width="20%">Account # :</td>
      <td>${customer.account}</td>
    </tr>
    <tr>
      <td>Origin Depot :</td>
      <td>SHA	</td>
      <td>Average Weight per Consignment :</td>
      <td></td>
    </tr>
    <tr>
	  <td width="20%">Projected Monthly Revenue in CNY: </td>
      <td colspan="3">220</td>
    </tr>
  </table>
  <b>Loading Details</b>
  <table>
    <tr>
      <td  width="20%">Division :</td>
      <td  width="40%">G</td>
      <td  width="20%">RateCategory :</td>
      <td>CN4PT</td>
    </tr>
    <tr>
      <td>Approval Date :</td>
      <td>30-Oct-08</td>
      <td>Effective Date :</td>
      <td>${business.effectiveDate}</td>
    </tr>
    <tr>
      <td>End Date :</td>
      <td>Open</td>
      <td>Review Date :</td>
      <td>Open</td>
    </tr>
    <tr>
      <td>Product :</td>
      <td>15D/12D/10D/09D</td>
      <td>Terms of Payment :</td>
      <td>${customer.payment}</td>
    </tr>
	<tr>
      <td>Option :</td>
      <td>No discount for potions!</td>
      <td>Currency :</td>
      <td>CNY</td>
    </tr>
    <tr>
      <td>Zoning :</td>
      <td>Set Default</td>
      <td>Fuel Surchange :</td>
      <td>FSI according to public fuel surchange</td>
    </tr>
    </table>
  </div>
 <br />
 <div style="padding:5px;">
 <c:if test="${flag=='Both'}">
	<div style="padding:0;" id="tabsI">
	  <ul>
	    <li><a href="#" onclick="tabPageControl(0)" title="Sender Pay" id="sp" class="activeBack"><span id="sps" class="activeSpan">Sender Pay</span></a></li>
	    <li><a href="#" onclick="tabPageControl(1)" title="Recieve Pay" id="rp"><span id="rps">Recieve Pay</span></a></li>
	  </ul>
	</div>
</c:if>
</div>
<div style="padding:5px;" id="tabPagesContainer">
<div class="tabPageSelected" id="sedDIV">
 <table>
 <tr>
 <td style="background-color:#F5C07B">Summary </td><td style="background-color:#EFEFEF">Rev(CNY)</td><td>${zoneSummary.revM}/M</td>
 <td>${zoneSummary.revY}/Y</td><td style="background-color:#EFEFEF">Cons</td><td>${zoneSummary.consM}/M</td><td>${zoneSummary.consY}/Y</td>
  <td style="background-color:#EFEFEF">Kilo(kg) </td><td>${zoneSummary.kiloM}/M</td><td>${zoneSummary.kiloY}/Y</td>
 </tr>
 </table>
<br/>
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
					<td width="16%">${zoneSummary.zoneType}</td>
					<td width="14%">${zoneSummary.revM}</td>
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
                <th colspan="25" style="text-align:left;">Discounts Profile-15D/12D/10D/09D</th>
            </tr>
            <tr>
                <th>Weightband</th>
               <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
           
        </thead>
        <tbody>
            <c:forEach items="${documentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
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
                <th colspan="25" style="text-align:left;">Discounts Profile-15N/12N/10N/09N</th>
            </tr>
            <tr>
                <th >Weightband</th>
               <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th >${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
           
        </thead>
        <tbody>
            <c:forEach items="${ndocumentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						
							<td>${discountMap[key]}%</td>
						
				   </c:forEach>
			   </tr>
			</c:forEach>
          </tbody>
</table>
<br>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="25" style="text-align:left;">Discounts Profile-48N</th>
            </tr>
            <tr>
                <th >Weightband</th>
               <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th >${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
            
        </thead>
        <tbody>
           <c:forEach items="${economyList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						
							<td>${discountMap[key]}%</td>
							
				   </c:forEach>
			   </tr>
			</c:forEach>
        </tbody>
    </table>
</br>
</div>
<div class="tabPageUnSelected" id="recDIV">
 <table>
 <tr>
 <td style="background-color:#F5C07B">Summary </td><td style="background-color:#EFEFEF">Rev(CNY)</td><td>${recZoneSummary.revM}/M</td>
 <td>${recZoneSummary.revY}/Y</td><td style="background-color:#EFEFEF">Cons</td><td>${recZoneSummary.consM}/M</td><td>${recZoneSummary.consY}/Y</td>
  <td style="background-color:#EFEFEF">Kilo(kg) </td><td>${recZoneSummary.kiloM}/M</td><td>${recZoneSummary.kiloY}/Y</td>
 </tr>
 </table>
<br/>
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
            <c:forEach items="${recGeoSummaryList}" var="geoSummary">
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
        <c:forEach items="${recZoneSummaryList}" var="zoneSummary">
				<tr>
					<td width="16%">${zoneSummary.zoneType}</td>
					<td width="14%">${zoneSummary.revM}</td>
					<td width="14%">${zoneSummary.revY}</td>
					<td width="14%">${zoneSummary.consM}</td>
					<td width="14%">${zoneSummary.consY}</td>
					<td width="14%">${zoneSummary.kiloM}</td>
					<td width="14%">${zoneSummary.kiloY}</td>
			   </tr>
		 </c:forEach>
          </tbody>
</table>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="25" style="text-align:left;">Discounts Profile-15D/12D/10D/09D</th>
            </tr>
            <tr>
               <th>Weightband</th>
               <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${documentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
							<td>${recDiscountMap[key]}%</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
          </tbody>
</table>
<br />
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="25" style="text-align:left;">Discounts Profile-15N/12N/10N/09N</th>
            </tr>
            <tr>
                <th>Weightband</th>
               <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ndocumentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
							<td>${recDiscountMap[key]}%</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
          </tbody>
</table>
<br/>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="25" style="text-align:left;">Discounts Profile-48N</th>
            </tr>
            <tr>
               <th >Weightband</th>
               <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th >${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
           <c:forEach items="${economyList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td>${recDiscountMap[key]}%</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
        </tbody>
    </table>
<br/>
</div>
</div>
</div>
<br/>
  <div style="text-align: center">
 	<input type="button" value="Close" class="cls-button" id="Close" onclick="window.close();"/>
  </div>
</form>
</div>
<script type="text/javascript">

	function tabPageControl(n) {
		if(n==1){
			document.getElementById("recDIV").style.cssText = "display:block;";
			document.getElementById("sedDIV").style.cssText = "display:none;";
			document.getElementById("rp").style.cssText = "background-position:0% -42px;";
			document.getElementById("rps").style.cssText = "background-position:100% -42px;";
			document.getElementById("sp").style.cssText = "background-position:";
			document.getElementById("sps").style.cssText = "background-position:";
		}else{
			document.getElementById("sedDIV").style.cssText = "display:block;";
			document.getElementById("recDIV").style.cssText = "display:none;";
			document.getElementById("rp").style.cssText = "background-position:";
			document.getElementById("rps").style.cssText = "background-position:";
			document.getElementById("sp").style.cssText = "background-position:0% -42px;";
			document.getElementById("sps").style.cssText = "background-position:100% -42px;";
		}
		
	}
</script>
</body>
</html>
