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
</head>
<body>
<form action="#" method="post" id="busCus">
	<div style="padding:5px;">
  <h4 class="title">New PT-Customer Description</h4>
  <table class="modify">
    <tr>
      <th>Application Date:</th>
      <td><span id="appDate"></span></td>
      <th>Depot:</th>
      <td>SHA<input  type="hidden" name="business.depotCode" value="SHA"/></td>
    </tr>
    <tr>
      <th>A new PT Customer?</th>
      <td><select name="business.isNewCus" value="${business.isNewCus}">
      	<option value="YES">YES</option>
      	<option value="NO">NO</option>
      	</select>	
      </td>
      <th>Territory:</th>
      <td><input type="text"  name="business.territory" value="${business.territory}"/></td>
    </tr>
    <tr>
      <th>Account #:</th>
      <td><input type="text"  name="customer.account" value="${customer.account}"/></td>
      <td colspan="2" style="color:#FF9900">("7777777"refer to new customer)</td>
    </tr>
    <tr>
      <th>Customer Name</th>
      <td><input type="text" name="customer.cusName" value="${customer.cusName}"/></td>
      <th>Channel:</th>
      <td><select name="customer.channel" value="${customer.channel}">
          <option value="TSM">TSM</option>
        </select></td>
    </tr>
    <tr>
      <th>Industry</th>
      <td colspan="1">
      	<select name="customer.industry" >
          <option value="ServiceIndustry(Advertising,Media,Agency,Laws)">ServiceIndustry(Advertising,Media,Agency,Laws)</option>
        </select></td>
    </tr>
    <tr>
      <th>Current Service Provider</th>
      <td colspan="3"><input type="text" size="100" name="customer.serviceProvider" value="${customer.serviceProvider}"/></td>
    </tr>
   
  </table>
  <table class="modify">
    <tr>
      <th rowspan="2">Is the customer on fuel surcharge exemption or deduction now?</th>
      <td rowspan="2"><select name="customer.isFuleDeduction" value="${customer.isFuleDeduction}">
         <option value="YES">YES</option>
      	<option value="NO">NO</option>
        </select></td>
      <th>the Current fuel surcharge</th>
      <td>Per FS Index
      <input type="hidden" name="customer.fuelSurcharge" value="Per FS Index"/>
      <input type="hidden" name="customer.reqFuelSurcharge" value="Per FS Index"/>
      <input type="hidden" name="customer.isReq" value="NO"/></td>
    </tr>
    <tr>
     
      <th>the fuel surcharge requested</th>
      <td>Per FS Index</td>
    </tr>
    </tr>
    
  </table>
  <table class="modify">
    <tr>
      <th style="background-color:yellow"><span><B> Terms  of  Payments:</B></span></th>
      <td colspan="3">
	  <select id="tp" name="customer.payment" value="${customer.payment}">
           <option value="SenderPays">SenderPays</option>
		   <option value="ReceivePay">ReceivePay</option>
		   <option value="Both">Both</option>
       </select></td>
    </tr>
    <tr>
      <th>Is customer a mainly document sender?</th>
      <td><select  name="business.isDocumentSender" value="${business.isDocumentSender}">
          <option value="NO">No</option>
          <option value="YES">YES</option>
        </select></td>
      <th>Cons/Stop</th>
      <td><input type="text"  name="business.consStop" value="${business.consStop}"></td>
    </tr>
     <tr>
      <th>Prdouct Description(eg:digital cameral)</th>
      <td colspan="1"><input type="text" value="Test DC"  name="business.description" value="${business.description}"></td>
      <th>Weight Range</th>
      <td ><select name="business.weightRange" value="${business.weightRange}">
      	<option value="1">0.5-5kg</option>
      	<option value="2">5-10kg</option>
      	</select></td>
    </tr>
     <tr>
      <th>Reason for the PT:</th>
      <td colspan="3">
      	<textarea style="width:700px; height:100px" name="business.reson" value="${business.reson}">test only</textarea>
      </td>
    </tr>
  </table>
  <span style="color:blue">*Frames highlighted by yellow must be filled in</span>
  <hr />
  <br>
  <div style="text-align: center">
  <input type="button" value="Submit" class="cls-button" onclick="tothenext(this)" /> 
   	&nbsp;&nbsp;&nbsp;<input type="button" value="Copy" class="cls-button"  id="copy" />
   	 &nbsp;&nbsp;&nbsp;
   	
   </div>
</form>
</div>

</body>
<script type="text/javascript">

$(function(){
    $("#copy").click(function(){
     	$("#busCus").attr('action',"${ctx}/ptQuery/copy");
 		$("#busCus").submit();
    });
   
});

</script>
</html>
