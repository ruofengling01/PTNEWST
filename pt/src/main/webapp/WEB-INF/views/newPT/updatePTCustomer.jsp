<%@ page language="java" import="java.util.*,org.springside.modules.utils.DateProvider" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>New PT-Customer Description</title>
<link href="${ctx}/static/styles/main.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/1.7.2/jquery.min.js" type="text/javascript"></script>
 <script type="text/javascript" src="${ctx}/static/js/calendar.js"></script>
 <script src="${ctx}/static/jquery-validation/1.10.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.10.0/messages_bs_en.js" type="text/javascript"></script>
<link href="${ctx}/static/jquery-validation/1.10.0/validate.css" type="text/css" rel="stylesheet" />
<script>
		$(document).ready(function() {
			//为inputForm注册validate函数
			$("#busCus").validate();
		});
		window.confirm = function(str)   
	{   
	      execScript("n = (msgbox('"+str+"',vbYesNo, '提示')=vbYes)", "vbscript");   
	      return(n);   
	}
</script>
</head>
<body>
<form action="#" method="post" id="busCus">
	<div style="padding:5px;">
  <h4 class="title">New PT-Customer Description</h4>
  <table class="modify">
    <tr>
   	 <input  type="hidden" name="business.id" value="${business.id}"/>
   	 <input  type="hidden" name="customer.id" value="${customer.id}"/>
   	 <input  type="hidden" name="business.zoneType" value="${business.zoneType}"/>
   	
      <th width="20%">Application Date:</th>
      <td width="44%"><span id="appDate"><fmt:formatDate value="${business.applicationDate}" pattern="yyyy-MM-dd" /></span></td>
      <th width="25%">Depot:</th>
      <td>${business.depotCode}<input  id="depotCode" type="hidden" name="business.depotCode" value="${business.depotCode}"/></td>
    </tr>
    <tr>
      <th>A new PT Customer?</th>
      <td><select name="business.isNewCus" >
      		<option value="YES" <c:if test="${business.isNewCus eq 'YES'}">selected</c:if> >YES</option>
      		<option value="NO"  <c:if test="${business.isNewCus eq 'NO'}">selected</c:if> >NO</option>
      	</select>	
      </td>
      <th>Territory:</th>
      <td><input type="text"  name="business.territory"  value="${business.territory}" class="required"/></td>
    </tr>
    <tr>
      <th>Account #:</th>
      <td><input type="text"  id="account" name="customer.account" value="${customer.account}" class="required"/><span style="background-color:yellow">("777777777"refer to new customer)</span></td>
       <th>Telephone:</th>
      <td><input type="text"  name="business.telPhone" class="required" value="${business.telPhone}"/></td>
    </tr>
    <tr>
      <th>Customer Name</th>
      <td><input type="text" id="cusName" name="customer.cusName"  value="${customer.cusName}" class="required" size="75"/></td>
      <th>Channel:</th>
      <td><select name="customer.channel" >
          <option value="TSM" <c:if test="${customer.channel eq 'TSM'}">selected</c:if>>TSM</option>
        </select></td>
    </tr>
    <tr>
      <th>Industry</th>
      <td colspan="1">
      	<select name="customer.industry" >
		    <option value="Logistics/Transportations/Freight Forwarders"  <c:if test="${customer.industry eq 'Logistics/Transportations/Freight Forwarders'}">selected</c:if>>
			Logistics/Transportations/Freight Forwarders</option>
			<option value="Computers/Internet/Telecommunications/Electronic Technology"  <c:if test="${customer.industry eq 'Computers/Internet/Telecommunications/Electronic Technology'}">selected</c:if>>
			Computers/Internet/Telecommunications/Electronic Technology</option>
			<option value="Biotechnology/Pharmacy/Medical Equipment"  <c:if test="${customer.industry eq 'Biotechnology/Pharmacy/Medical Equipment'}">selected</c:if>>
			Biotechnology/Pharmacy/Medical Equipment</option>
			<option value="FMCG(Foods, Beverages, Cosmetics, etc.)"  <c:if test="${customer.industry eq 'FMCG(Foods, Beverages, Cosmetics, etc.)'}">selected</c:if>>
			FMCG(Foods, Beverages, Cosmetics, etc.)</option>
			<option value="DCG(Furnitures, Household appliances, Garments, Textiles)"  <c:if test="${customer.industry eq 'DCG(Furnitures, Household appliances, Garments, Textiles)'}">selected</c:if>>
			DCG(Furnitures, Household appliances, Garments, Textiles)</option>
			<option value="Auto and Accessories/Manufacturing(Engine/Mechanics)/Chemical"  <c:if test="${customer.industry eq 'Auto and Accessories/Manufacturing(Engine/Mechanics)/Chemical'}">selected</c:if>>
			Auto and Accessories/Manufacturing(Engine/Mechanics)/Chemical</option>
			<option value="Trade/Imp&amp;Exp/Wholesales/Retails"  <c:if test="${customer.industry eq 'Trade/Imp&amp;Exp/Wholesales/Retails'}">selected</c:if>>
			Trade/Imp&amp;Exp/Wholesales/Retails</option>
			<option value="Service Industry(Advertising, Media, Agency, Laws, Consultation, etc.)"  <c:if test="${customer.industry eq 'Service Industry(Advertising, Media, Agency, Laws, Consultation, etc.)'}">selected</c:if>>
			Service Industry(Advertising, Media, Agency, Laws, Consultation, etc.)</option>
			<option value="Others"<c:if test="${customer.industry eq 'Others'}">selected</c:if>>Others</option>
        </select></td>
    </tr>
    <tr>
      <th>Current Service Provider</th>
      <td colspan="3"><input type="text" size="91" id="serviceProvider" name="customer.serviceProvider" value="${customer.serviceProvider}" class="required"/></td>
    </tr>
   
  </table>
  <table class="modify">
    <tr>
      <th rowspan="2" width="40%">Is the customer on fuel surcharge exemption or deduction now?</th>
      <td rowspan="2" width="23.5%">
      	<select name="customer.isFuleDeduction">
         <option value="YES" <c:if test="${customer.isFuleDeduction eq 'YES'}">selected</c:if>>YES</option>
      	 <option value="NO" <c:if test="${customer.isFuleDeduction eq 'NO'}">selected</c:if>>NO</option>
        </select></td>
      <th width="24.5%">the Current fuel surcharge</th>
      <td id="fsi">${customer.fuelSurcharge}</td>
      <input id="fsi_hidden" type="hidden" name="customer.fuelSurcharge" value="${customer.fuelSurcharge}"/>
      <input type="hidden" name="customer.reqFuelSurcharge" value="${customer.reqFuelSurcharge}"/>
      <input type="hidden" name="customer.isReq" value="${customer.isReq}"/>
    </tr><%--
    <tr>
     
      <th>the fuel surcharge requested</th>
      <td>Per FS Index</td>
    </tr>
    --%></tr>
    
  </table>
  <table class="modify">
    <tr>
      <th style="background-color:yellow"><span><B> Terms  of  Payments:</B></span></th>
      <td colspan="3">
	  <select id="tp" name="customer.payment">
           <option value="SenderPay" <c:if test="${customer.payment eq 'SenderPay'}">selected</c:if>>SenderPay</option>
		   <option value="ReceivePay" <c:if test="${customer.payment eq 'ReceivePay'}">selected</c:if> >ReceivePay</option>
		   <option value="Both" <c:if test="${customer.payment eq 'Both'}">selected</c:if> >Both</option>
       </select></td>
    </tr>
    <tr>
      <th width="40%">Is customer a mainly document sender?</th>
      <td width="27%"><select  name="business.isDocumentSender">
          <option value="NO"  <c:if test="${business.isDocumentSender eq 'NO'}">selected</c:if>>No</option>
          <option value="YES"  <c:if test="${business.isDocumentSender eq 'YES'}">selected</c:if>>YES</option>
        </select></td>
      <th>Cons/Stop</th>
      <td><input type="text"  name="business.consStop" value="${business.consStop}" class="required number"></td>
    </tr>
     <tr>
      <th>Prdouct Description(eg:digital cameral)</th>
      <td colspan="1"><input type="text" name="business.description" value="${business.description}" class="required"></td>
      <th style="display:none">Weight Range</th>
      <td style="display:none"><select name="business.weightRange">
      	<option value="1" <c:if test="${business.weightRange eq '1'}">selected</c:if>>0.5-5kg</option>
      	<option value="2" <c:if test="${business.weightRange eq '2'}">selected</c:if>>5-10kg</option>
      	</select></td>
    </tr>
     <tr>
      <th width="40%" class="modify">Reason for the PT:</th>
      <td width="80%">
      	
		<textarea style="width:100%; height:100px" name="business.reson" class="required">${business.reson}</textarea>
      </td>
    </tr>
  </table>
  <span style="color:blue">*Frames highlighted by yellow must be filled in</span>
  <hr />
  <br>
  <div style="text-align: center">
  <input type="button" value="Submit" class="cls-button" onclick="tothenext(this)" id="sub"/> 
   	&nbsp;&nbsp;&nbsp;<input type="button" value="Copy" class="cls-button"  id="copy"/>
   	 &nbsp;&nbsp;&nbsp;<input type="button" value="Close" class="cls-button" onclick="window.location.href='${ctx}/ptQuery/ptModifyInit';"/>
   	<input type="hidden" id="isFollow" name="isFollow">
	<input type="hidden" id="isModify" name="isModify">
   </div>
</form>
</div>

</body>
<script type="text/javascript">
function tothenext(obj){
		if($('#tp').val()=='Both'){
			if(confirm("Term of Payment is BOTH?")){
				if(confirm("is ReceivePay follow SenderPay?")){
					   $("#isModify").val("YES");
					   $("#isFollow").val("YES");
					   //$("#busCus").attr('action','${ctx}/ptCreate/disConfirm/add');
					   $("#busCus").attr('action','${ctx}/ptCreate/disConProfile');
					   $("#busCus").submit();
					}else{
					   $("#isModify").val("YES");
					   $("#isFollow").val("NO");
					   //$("#busCus").attr('action','${ctx}/ptCreate/disConfirm/add');
					   $("#busCus").attr('action','${ctx}/ptCreate/disConProfile');
					   $("#busCus").submit();
				}
			}
		}else{
			$("#isModify").val("YES");
			$("#busCus").attr('action','${ctx}/ptCreate/disConProfile');
			//$("#busCus").attr('action','${ctx}/ptCreate/disConfirm/add');
			$("#busCus").submit();
		}
}

$(function(){
    $("#copy").click(function(){
    	var cusstr = window.showModalDialog("${ctx}/ptQuery/copy","","dialogWidth=800px;dialogHeight=450px");
    	if(cusstr ==null || cusstr ==undefined){
	    	alert("pls check one customer on the dialog");
	    	return;
    	}
    	cusstr = jQuery.parseJSON(cusstr);
   		$("#account").val(cusstr.account);
    	$("#cusName").val(cusstr.cusName);
    	$("#serviceProvider").val(cusstr.serviceProvider);
     	//$("#busCus").attr('action',"${ctx}/ptQuery/copy");
 		//$("#busCus").submit();
    });
});

$(function(){
    $("#account").focusout(function(){
    	var accountNo = $("#account").val();
    	var depotCode = $("#depotCode").val();
    	var flag = false;//该depot下是否有客户
    	if(accountNo.length < 9){
    		while(accountNo.length < 9){
    			accountNo = "0"+accountNo;
    		}
    		$("#account").val(accountNo);
    	}
    	$.ajax({
            type:"GET",
            url:"${ctx}/tntCustomer/getCustomer/"+accountNo+"/"+depotCode,
            dataType:"json",      
            contentType:"application/json", 
            async:false,
            success:function(data){
            	if(data!=null){
	            	if(data.branch == depotCode){
	            		$("#cusName").val(data.name);
	                	$("#territory").val(data.territory);//channel
	                	$("#channel").val(data.channel);
	                	$("#sub").attr("disabled",false);
	            	}else{
	            		alert("sorry,u can not submit this pt!");
	            		$("#sub").attr("disabled",true);
	            		flag = true;
	            	}
            	}else if(accountNo!="777777777"){
            		alert("sorry,'777777777' refer to new customer");
            		$("#sub").attr("disabled",true);
            	}else{
            		$("#sub").attr("disabled",false);
            	}
            },
            error:function(e) {
                alert("error："+e);
            }
        });
    	$.ajax({
            type:"GET",
            url:"${ctx}/tntCustomer/getFsi/"+accountNo+"/"+depotCode,
            dataType:"json",      
            contentType:"application/json",   
            async:false,
            success:function(data){
            	//$("#account").val(data.accountNo);
            	if(!flag){
            		$("#fsi").html(toPercent(data));
                	$("#fsi_hidden").val(data);
            	}
            },
            error:function(e) {
                alert("error："+e);
            }
        });
    });

});

function toPercent(data){
    var strData = parseFloat(data)*10000;
    strData = Math.round(strData);
    strData/=100.00;
    strData = strData.toString();
    while (strData.length <= strData.indexOf(".") + 2) {
    	strData += '0';
    }
    var ret = strData.toString()+"%";
    return ret;
}

var date = new Date();
$('#appDate').html(date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate());	
if($("#fsi_hidden").val()!=""){
	$("#fsi").html(toPercent($("#fsi_hidden").val()));	
}else{
	$("#fsi").html("Per FS Index");	
}
</script>
</html>
