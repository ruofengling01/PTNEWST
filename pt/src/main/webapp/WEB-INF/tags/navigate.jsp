<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

a{
	text-decoration : none;
}
.icon-fast-backward{
	background:url(${ctx}/static/images/first.gif) no-repeat;
	width:8px;
	height:15px;
	text-algin:center;
}
.icon-chevron-left{
	background:url(${ctx}/static/images/previous.gif) no-repeat;
	width:8px;
	height:15px;
	text-algin:center;
}
.icon-chevron-right{
	background:url(${ctx}/static/images/next.gif) no-repeat;
	width:8px;
	height:15px;
	text-algin:center;
}
.icon-fast-forward{
	background:url(${ctx}/static/images/last.gif) no-repeat;
	width:8px;
	height:15px;
	text-algin:center;
}
-->
</style>
<%
	String path1 = request.getContextPath();
	String basePath1 = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path1 + "/";
	
	org.tnt.pt.vo.BaseVO model =  (org.tnt.pt.vo.BaseVO)request.getAttribute("model");
	//com.opensymphony.xwork2.util.ValueStack vs = (com.opensymphony.xwork2.util.ValueStack)request.getAttribute("struts.valueStack");
    org.tnt.pt.util.Navigate navigate = (org.tnt.pt.util.Navigate)model.getNavigate();
    int left,right,prev,next;

    int pageId = 0;
        pageId=navigate.getPageId();
    int pageCount =0;
        pageCount= navigate.getPageCount();
    int rowCount =0;
        rowCount= navigate.getRowCount();
    int curPageSize =0;
        curPageSize= rowCount - navigate.getPageOffset();

    if(curPageSize > navigate.getPageSize()) {
        curPageSize = navigate.getPageSize();
    }
    else if(curPageSize<0) {
        curPageSize = 0;
    }
    if(pageCount == 0) {
        pageCount = 1;
    }

    left  = (pageId - 3 > 0) ? (pageId - 3):1;
    right = (pageId + 3 < pageCount)?(pageId + 3):pageCount;
    prev  = (pageId - 1 > 0)?(pageId - 1):1;
    next  = (pageId + 1 < pageCount)?(pageId + 1):pageCount;
%>
<script language="javascript">

	function doNavigate(inPageId) { 
		$("input[name='navigate.pageId']").val(inPageId);
        document.forms[0].submit();
    }
	function getRowCount(){
		var rowCount = "<%=rowCount%>";
		return rowCount;
	}
	function toPage(){
		var pageId = document.getElementById("pageId").value;
		var pageCount = "<%=pageCount%>";
		
		if (pageId.length > 1) {
			if (pageId.substring(0, 1) == '0') {
				alert("输入的页数不合法.");
				return;
			}
		}
		
		if (pageId!=null&&(pageId<=0 || pageId!=Number(pageId))) {
			alert("输入的页数不合法.");
			return;
		}
		
		if (Number(pageId) > pageCount) {
			alert("输入的页数大于总页数.");
			return;
		} else {
			doNavigate(pageId);
		}
	}
</script>
<input type="hidden" name="navigate.pageId" value="${model.navigate.pageId}">
<input type="hidden" name="navigate.orderField" value="${model.navigate.orderField}">
<input type="hidden" name="navigate.orderDirection" value="${model.navigate.orderDirection}">
<table border="0"  cellspacing="0" cellpadding="0" align="right" height="35" width="100%">
  <tr>
    <!--  <td width="29%" align="left" valign="middle">&nbsp; 
    </td>-->
    <td width="70%" align="left" style="border:0px" ><font style=" color:#383838" >Page:<%=pageId%>/<%=pageCount%> &nbsp;Record:<%=rowCount%> &nbsp;Goto:<input type="text" id="pageId" size=1><a href="javascript:toPage()" class="btn btn-large" style="color:#91A1B1">GO</a></font>
    </td>
    <td width="20%" align="right" style="border:0px">
    <a href="javascript:doNavigate(1)" class="icon-fast-backward">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:doNavigate(<%=prev%>)" class="icon-chevron-left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>

	<%
    for(int i = left ; i <= right ; i++)  {
        if(i == pageId)
            out.println("<B><A HREF=\"javascript:doNavigate(" + i + ")\" title=\""+i+"\" style=\"text-decoration:underline;color:#91A1B1\">[&nbsp;" + i + "&nbsp;]</FONT></A></B>");
        else
            out.println("<B><A HREF=\"javascript:doNavigate(" + i + ")\" title=\""+i+"\" style=\"color:#91A1B1\">" + i + "</A></B>");
    }
%>
&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:doNavigate(<%=next%>)" class="icon-chevron-right">&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="javascript:doNavigate(<%=pageCount%>)" class=" icon-fast-forward">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </font>
    </td> 
    
  </tr>
</table>