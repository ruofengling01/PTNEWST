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
  <h4 class="title">New PT-Summary Info</h4>
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
	<div style="padding:0;" id="tabsI">
	  <ul id="nav">
	    <li><a href="#" onclick="tabControl(0)" title="Discount Profile"  id="a0" class="activeBack"><span id="s0" class="activeSpan">Discount Profile</span></a></li>
	    <li><a href="#" onclick="tabControl(1)" title="Heavy Weight Rate" id="a1"><span id="s1">Heavy Weight Rate</span></a></li>
	    <li><a href="#" onclick="tabControl(2)" title="Consignment Profile" id="a2"><span id="s2">Consignment Profile</span></a></li>
	  </ul>
	</div>
<div class="tabPageSelected" id="disDIV">
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
<div class="tabPageUnSelected" id="conDIV">
<table class="table_B" width="100%">
        <thead>
						<tr align="center">
                <th colspan="14" style="text-align:left;">Consignmengt Profile- 15D/12D/10D/09D</th>
            </tr>
            <tr>
                <th>Weightband</th>
                 <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th ondblclick="setSpecificConsignmentSet(${zoneGroup.id},${zoneType.document});">${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${documentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td>${consignmentMap[key]}</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
          </tbody>
</table>
<br>
<table class="table_B" width="100%">
        <thead>
						<tr align="center">
                <th colspan="14" style="text-align:left;">Consignmengt Profile- 15N/12N/10N/09N</th>
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
						<td>${consignmentMap[key]}</td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
<table class="table_B" width="100%">
        <thead>
						<tr align="center">
                <th colspan="14" style="text-align:left;">Consignmengt Profile- 48N</th>
            </tr>
            <tr>
                <th >Weightband</th>
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${economyList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td>${consignmentMap[key]}</td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
</br>
</div>
<div class="tabPageUnSelected" id="hwDIV">
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
</div>
</div>
<div class="tabPageUnSelected" id="recDIV">
	<div style="padding:0;" id="tabsI">
	  <ul>
	    <li><a href="#" onclick="tabControlRec(0)" title="Discount Profile" id="aa0"><span id="ss0">Discount Profile</span></a></li>
	    <li><a href="#" onclick="tabControlRec(1)" title="Heavy Weight Rate" id="aa1"><span id="ss1">Heavy Weight Rate</span></a></li>
	    <li><a href="#" onclick="tabControlRec(2)" title="Consignment Profile" id="aa2"><span id="ss2">Consignment Profile</span></a></li>
	  </ul>
	</div>
<div class="tabPageSelected" id="disDIVrec">
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
<br />
</div>
<div class="tabPageUnSelected" id="conDIVrec">
<table class="table_B" width="100%">
        <thead>
						<tr align="center">
                <th colspan="14" style="text-align:left;">Consignmengt Profile- 15D/12D/10D/09D</th>
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
						<td>${recConsignmentMap[key]}</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
          </tbody>
</table>
<br>
<table class="table_B" width="100%">
        <thead>
						<tr align="center">
                <th colspan="14" style="text-align:left;">Consignmengt Profile- 15N/12N/10N/09N</th>
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
						<td>${recConsignmentMap[key]}</td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
<br>
<table class="table_B" width="100%">
        <thead>
						<tr align="center">
                <th colspan="14" style="text-align:left;">Consignmengt Profile- 48N</th>
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
						<td>${recConsignmentMap[key]}</td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
</br>
</div>
<div class="tabPageUnSelected" id="hwDIVrec">
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
						    <td>${recHwRateMap[key]}</td>
				   </c:forEach>
				   <td></td>
			   </tr>
			</c:forEach>
        </tbody>
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
						    <td>${recHwRateMap[key]}</td>
				   </c:forEach>
			   </tr>
			</c:forEach>
        </tbody>
</table>
</div>
</div>
</div>
</div>
<br/>
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
<br />
  <div style="text-align: center">
  <br>
<table>
	<tr>
		<td rowspan="2"style="background-color:#EFEFEF;width:10%">Reason for Approval or Rejection</td>
		<td rowspan="2" width="35%">
		<div>
			<textarea id="examOppion" style="width:100%; height:100px"></textarea>
		</div>
		</td>
	</tr>
</table>
<br />
  <div style="text-align: center">
  <input type="button" value="Approve" class="cls-button" id="Approve"  /> 
   	&nbsp;&nbsp;&nbsp;<input type="button" id="Reject" value="Reject" class="cls-button" />
   	 &nbsp;&nbsp;&nbsp;<input type="button" value="Return to PT List" class="cls-button" id="Return" onclick="javascript:history.go(-1);"/>
   </div>
   <input type="hidden" id="hiddenID" value="${business.id}" name="business.id"/>
</form>
</div>
<script type="text/javascript">

    $(function(){
        $("#Approve").click(function(){
        	var examOppion = document.getElementById("examOppion").value;
            $.ajax({
                type:"POST",
                url:"${ctx}/ptApprove/salesAdminApprove",
                data:"id="+document.getElementById("hiddenID").value+"&examOppion="+examOppion,
                success:function(data){
                	alert('Approve Success！');
                	window.location.href="${ctx}/ptQuery/ptApproveInit";
                },error:function(e) {
                    alert("error："+e);
                }
            });
        });
        
        $("#Reject").click(function(){
	        var examOppion = document.getElementById("examOppion").value;
	        if(examOppion==""){
	    		alert('Please field the reason of rejection!');
	    		document.getElementById("examOppion").focus();
	    		return;
	    	}
           $.ajax({
               type:"POST",
               url:"${ctx}/ptApprove/reject",
               data:"id="+document.getElementById("hiddenID").value+"&state=billingFail"+"&examOppion="+examOppion,
               success:function(data){
               	alert('Reject success !');
               },error:function(e) {
                   alert("error："+e);
               }
           });
        });
        
        $("#deleteFile").click(function(){
        	var fileName = document.getElementById("fileName").innerText;
            $.ajax({
                type:"POST",
                url:"${ctx}/ptApprove/deleteFile",
                data:"id="+document.getElementById("hiddenID").value+"&fileName="+fileName,
                success:function(data){
                	alert("delete success!");
                	document.getElementById("fileName").innerText="";
                },error:function(e) {
                    alert("error："+e);
                }
            });
        });
    });

	$(function() {
    	var businessId = document.getElementById("hiddenID").value;
	    $("#file_upload").uploadify({
	    	'height'        : 27, 
	    	'width'         : 80,  
	    	'buttonText'    : 'browse',
	        'swf'           : '<%=path%>/uploadify/uploadify.swf',
	        'uploader'      : '${ctx}/ptApprove/uploadFile/'+businessId,
	        'auto'          : false,
	        //'fileTypeExts'  : '*.xls',
	        'formData'      : {'userName':'','qq':''},
	        'onUploadSuccess': function(file, data)
	        { }
	    });
	});
    
	function tabPageControl(n) {
		if(n==1){
			document.getElementById("recDIV").style.cssText = "display:block;";
			document.getElementById("sedDIV").style.cssText = "display:none;";
			document.getElementById("rp").style.cssText = "background-position:0% -42px;";
			document.getElementById("rps").style.cssText = "background-position:100% -42px;";
			document.getElementById("sp").style.cssText = "background-position:";
			document.getElementById("sps").style.cssText = "background-position:";
			for(var i=0;i<=2;i++){
				if(i==0){
					var links= "aa"+i;var sp= "ss"+i;
					document.getElementById(links).style.cssText = "background-position:0% -42px;";
					document.getElementById(sp).style.cssText = "background-position:100% -42px;";
				}else{
					var links= "aa"+i;var sp= "ss"+i;
					document.getElementById(links).style.cssText = "background-position:";
					document.getElementById(sp).style.cssText = "background-position:";
				}
			}
		}else{
			document.getElementById("sedDIV").style.cssText = "display:block;";
			document.getElementById("recDIV").style.cssText = "display:none;";
			document.getElementById("rp").style.cssText = "background-position:";
			document.getElementById("rps").style.cssText = "background-position:";
			document.getElementById("sp").style.cssText = "background-position:0% -42px;";
			document.getElementById("sps").style.cssText = "background-position:100% -42px;";
			for(var i=0;i<=2;i++){
				if(i==0){
					var links= "a"+i;var sp= "s"+i;
					document.getElementById(links).style.cssText = "background-position:0% -42px;";
					document.getElementById(sp).style.cssText = "background-position:100% -42px;";
				}else{
					var links= "a"+i;var sp= "s"+i;
					document.getElementById(links).style.cssText = "background-position:";
					document.getElementById(sp).style.cssText = "background-position:";
				}
			}
		}
		
	}
	function tabControl(n) {
		for(var i=0;i<=2;i++){
			if(i==n){
				var links= "a"+i;var sp= "s"+i;
				document.getElementById(links).style.cssText = "background-position:0% -42px;";
				document.getElementById(sp).style.cssText = "background-position:100% -42px;";
			}else{
				var links= "a"+i;var sp= "s"+i;
				document.getElementById(links).style.cssText = "background-position:";
				document.getElementById(sp).style.cssText = "background-position:";
			}
		}
		if(n==1){
			document.getElementById("hwDIV").style.cssText = "display:block;";
			document.getElementById("disDIV").style.cssText = "display:none;";
			document.getElementById("conDIV").style.cssText = "display:none;";
			
		}else if(n==2){
			document.getElementById("conDIV").style.cssText = "display:block;";
			document.getElementById("hwDIV").style.cssText = "display:none;";
			document.getElementById("disDIV").style.cssText = "display:none;";
		}else{
			document.getElementById("disDIV").style.cssText = "display:block;";
			document.getElementById("hwDIV").style.cssText = "display:none;";
			document.getElementById("conDIV").style.cssText = "display:none;";
		}
	}

	function tabControlRec(n) {
		for(var i=0;i<=2;i++){
			if(i==n){
				var links= "aa"+i;var sp= "ss"+i;
				document.getElementById(links).style.cssText = "background-position:0% -42px;";
				document.getElementById(sp).style.cssText = "background-position:100% -42px;";
			}else{
				var links= "aa"+i;var sp= "ss"+i;
				document.getElementById(links).style.cssText = "background-position:";
				document.getElementById(sp).style.cssText = "background-position:";
			}
		}
		if(n==1){
			document.getElementById("hwDIVrec").style.cssText = "display:block;";
			document.getElementById("disDIVrec").style.cssText = "display:none;";
			document.getElementById("conDIVrec").style.cssText = "display:none;";
		}else if(n==2){
			document.getElementById("conDIVrec").style.cssText = "display:block;";
			document.getElementById("hwDIVrec").style.cssText = "display:none;";
			document.getElementById("disDIVrec").style.cssText = "display:none;";
		}else{
			document.getElementById("disDIVrec").style.cssText = "display:block;";
			document.getElementById("hwDIVrec").style.cssText = "display:none;";
			document.getElementById("conDIVrec").style.cssText = "display:none;";
		}
	}
</script>
</body>
</html>
