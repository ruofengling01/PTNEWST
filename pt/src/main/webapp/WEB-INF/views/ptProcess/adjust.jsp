<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="org.tnt.pt.util.PTPARAMETERS"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
function setCustomerDis(zonegroupId,tableId){
	var returnValue = window.showModalDialog("${ctx}/ptApprove/discountSet","","dialogWidth:240px; dialogHeight:150px;");
	if(returnValue==''){
		return;
	}else{
		if(tableId=='tableb'){//15N
			for(var i=1;i<11;i++){
				var wdId = 'zg_'+i+'_'+zonegroupId+'_${business.id}';
				$("#"+wdId).val(returnValue);
			}
		}else if(tableId=='tablec'){//48N
			for(var i=12;i<23;i++){
				var wdId = 'zg_'+i+'_'+zonegroupId+'_${business.id}';
				$("#"+wdId).val(returnValue);
			}
		}
	}
}	
</script>
<body>
<form action="#" id="adjustForm">
	<div style="padding:5px;">
  <h4 class="title">Commercial PT Adjust</h4>
 <br />
<div style="padding:0 5px 5px 5px;">
<div class="clearboth"> </div>
<h4>Terms  of  Payments:SenderPay</h4>
<table class="table_B" width="100%" id="talbea">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Requested-15D/12D/10D/09D</th>
            </tr>
            <tr>
                <th>WeightBand</th>
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th>${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${documentList}" var="weightBand">
				<tr>
					<td>${weightBand.weightbandGroup}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td><input id="zg_${key}_${business.id}" name="zg_${key}_${business.id}" type="text" value="${discountMap[key]}" size="3"/></td>
				   </c:forEach>
			   </tr>
			</c:forEach>
        </tbody>
    </table>
	<br />
<table class="table_B" width="100%" id="tableb">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-15N/12N/10N/09N</th>
            </tr>
            <tr>
                <th>Weightband</th>
				<th  width="8%"> Chargeable Weight(kg)</th>
                 <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th ondblclick="setCustomerDis(${zoneGroup.id},'tableb');">${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ndocumentList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<td>${weightBand.chargeableWeight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td><input id="zg_${key}_${business.id}" name="zg_${key}_${business.id}" type="text" value="${discountMap[key]}" size="3"/></td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
<br />
<table class="table_B" width="100%" id="tablec">
        <thead>
			<tr align="center">
                <th colspan="15" style="text-align:left;">Discounts Profile-48N</th>
            </tr>
            <tr>
                <th>Weightband</th>
				<th  width="8%"> Chargeable Weight(kg)</th>
                <c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<th ondblclick="setCustomerDis(${zoneGroup.id},'tablec');">${zoneGroup.zone}</th>
				</c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${eonomyList}" var="weightBand">
				<tr>
					<td>${weightBand.name}</td>
					<td>${weightBand.chargeableWeight}</td>
					<c:forEach items="${zoneGroupList}" var="zoneGroup" begin="0">
						<c:set var="key">${weightBand.id}_${zoneGroup.id}</c:set>
						<td><input id="zg_${key}_${business.id}" name="zg_${key}_${business.id}" type="text" value="${discountMap[key]}" size="3"/></td>
				   </c:forEach>
			   </tr>
			  </c:forEach>
          </tbody>
</table>
   <input type="hidden" id="isFollow" value="${isFollow}" name="isFollow">
   <input type="hidden" id="payment" value="${payment}" name="payment">
   <input type="hidden" id="hwFlag" value="${hwFlag}" name="hwFlag">
   <input type="hidden" id="businessId" value="${businessId}" name="businessId">
<br />
  <div style="text-align: center">
  <input type="button" value="Next" class="cls-button" id="next"/> 
   	&nbsp;&nbsp;&nbsp;<input type="button" value="Rates" class="cls-button"  id="rateDetail" />
   	&nbsp;&nbsp;&nbsp;<input type="button" value="Return" class="cls-button" onclick="javascript:window.history.back();"/>
   </div>
</form>
</div>
<script type="text/javascript">
    $(function(){
        $("#next").click(function(){
        	//序列化表单元素，返回json数据
        	var params = $(".table_B").find("input").serializeArray();
            var jsonString = O2String(params);
            var businessId = $('#businessId').val();
            $.ajax({
                type:"POST",
                url:"${ctx}/ptApprove/updateConsignment/${payment}",
                dataType:"json",      
                contentType:"application/json",
                data:jsonString,
                success:function(data){
                	if($('#isFollow').val()=='NO'){
                		window.location.href ="${ctx}/ptApprove/adjustRec/"+businessId;
                	}else{
                		if($('#hwFlag').val()=='hw'){
                			window.location.href="${ctx}/ptApprove/hwRateProfile/"+businessId+"/no";
                		}else{
                			window.location.href="${ctx}/ptApprove/ptAnalysingAdjust/"+businessId;
                		}
                		
                	}
                },
                error:function(e) {
                    alert("error："+e);
                }
            });
        });
        
        $("#rateDetail").click(function(){
        	//序列化表单元素，返回json数据
        	var params = $(".table_B").find("input").serializeArray();
            var jsonString = O2String(params);
            $.ajax({
                type:"POST",
                url:"${ctx}/ptApprove/updateConsignment/${payment}",
                dataType:"json",      
                contentType:"application/json",   
                data:jsonString,
                success:function(data){
                	window.showModalDialog('${ctx}/ptQuery/rates/${business.id}',"","dialogWidth=800px;dialogHeight=450px");
                },
                error:function(e) {
                    alert("error："+e);
                }
            });
        	
        });
    });

    var O2String = function (O) {
        //return JSON.stringify(jsonobj);
        var S = [];
        var J = "";
        if (Object.prototype.toString.apply(O) === '[object Array]') {
            for (var i = 0; i < O.length; i++)
                S.push(O2String(O[i]));
            J = '[' + S.join(',') + ']';
        }
        else if (Object.prototype.toString.apply(O) === '[object Date]') {
            J = "new Date(" + O.getTime() + ")";
        }
        else if (Object.prototype.toString.apply(O) === '[object RegExp]' || Object.prototype.toString.apply(O) === '[object Function]') {
            J = O.toString();
        }
        else if (Object.prototype.toString.apply(O) === '[object Object]') {
            for (var i in O) {
                O[i] = typeof (O[i]) == 'string' ? '"' + O[i] + '"' : (typeof (O[i]) === 'object' ? O2String(O[i]) : O[i]);
                S.push(i + ':' + O[i]);
            }
            J = '{' + S.join(',') + '}';
        }
        return J;
 };
</script>
</body>
</html>
