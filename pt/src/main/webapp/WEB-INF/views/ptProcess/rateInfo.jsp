<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/static/styles/main.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/styles/tab.css" type="text/css" rel="stylesheet" />
<title>New PT-Summary Info</title>
</head>
<style type="text/css">	
		.tab tr td {font-size:12px;
					align:left;
			}	

		</style>

<body>
<form action="#">
<div style="padding:5px;">
  <h4 class="title">PT Tariff Info</h4>
  
  <table class="tab">
    <tr>
	  <td width="20%">Account # :</td>
      <td width="30%">${customer.account}</td>
      <td width="20%">Customer Name :</td>
      <td width="30%">${customer.cusName}</td>
    </tr>
    <tr>
      <td>PT Application Reference# :</td>
      <td>${business.applicationReference}</td>
      <td>Terms of Payment : </td>
	  <td>${customer.payment}</td>
    </tr>
  </table>
 </div>
 <br />
 <div style="padding:5px;" id="tabPagesContainer">
<c:if test="${flag=='Both'}">
	<div style="padding:0;" id="tabsI">
	  <ul>
	    <li><a href="#" onclick="tabPageControl(0)" title="Sender Pay" id="sp" class="activeBack"><span id="sps" class="activeSpan">Sender Pay</span></a></li>
	    <li><a href="#" onclick="tabPageControl(1)" title="Recieve Pay" id="rp"><span id="rps">Recieve Pay</span></a></li>
	  </ul>
	</div>
</c:if>
<div class="tabPageSelected" id="sedDIV">
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-15D/12D/10D/09D</th>
            </tr>
            <tr>
                <th>Weightband</th>
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${documentTGList}" var="tariffGroup">
				<tr>
					<td>${tariffGroup.weight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${tariffGroup.id}_${zoneGroup.id}</c:set>
						<td><fmt:formatNumber pattern="0.00">${traiffMapSP[key]}</fmt:formatNumber></td>	
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
                 <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${ndocumentTGList}" var="tariffGroup">
				<tr>
					<td>${tariffGroup.weight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${tariffGroup.id}_${zoneGroup.id}</c:set>
						<td><fmt:formatNumber pattern="0.00">${traiffMapSP[key]}</fmt:formatNumber></td>	
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
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${economyTGList}" var="tariffGroup">
				<tr>
					<td>${tariffGroup.weight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${tariffGroup.id}_${zoneGroup.id}</c:set>
						<td><fmt:formatNumber pattern="0.00">${traiffMapSP[key]}</fmt:formatNumber></td>	
				   </c:forEach>
			   </tr>
			</c:forEach>
	</tbody>
</table>
</div>
<div class="tabPageUnSelected" id="recDIV">
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-15D/12D/10D/09D</th>
            </tr>
            <tr>
                <th>Weightband</th>
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${documentTGListR}" var="tariffGroup">
				<tr>
					<td>${tariffGroup.weight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${tariffGroup.id}_${zoneGroup.id}</c:set>
						<td><fmt:formatNumber pattern="0.00">${traiffMapRP[key]}</fmt:formatNumber></td>	
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
                 <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${ndocumentTGListR}" var="tariffGroup">
				<tr>
					<td>${tariffGroup.weight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${tariffGroup.id}_${zoneGroup.id}</c:set>
						<td><fmt:formatNumber pattern="0.00">${traiffMapRP[key]}</fmt:formatNumber></td>	
				   </c:forEach>
			   </tr>
			</c:forEach>
          </tbody>
</table>
</br>
<table class="table_B" width="100%">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-48N</th>
            </tr>
            <tr>
                <th>Weightband</th>
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
             <c:forEach items="${economyTGListR}" var="tariffGroup">
				<tr>
					<td>${tariffGroup.weight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
					    <c:set var="key">${tariffGroup.id}_${zoneGroup.id}</c:set>
						<td><fmt:formatNumber pattern="0.00">${traiffMapRP[key]}</fmt:formatNumber></td>	
				   </c:forEach>
			   </tr>
			</c:forEach>
	</tbody>
</table>
<br />
</div>
</div>
<br>
<br />
  <div style="text-align: center">
  	<input type="button" value="Close" class="cls-button" onclick="window.close();"/>
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