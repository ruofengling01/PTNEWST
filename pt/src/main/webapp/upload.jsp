<%@ page language="java" contentType="text/html; charset=gb2312" %>
<%@ page import="com.jspsmart.upload.SmartUpload"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%
	String savePath = request.getSession().getServletContext().getRealPath("/attached/");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String ymd = sdf.format(new Date());
	savePath = savePath + "/uploads/"+ ymd + "/";
	File f1 = new File(savePath);
    System.out.println(savePath);
    if (!f1.exists()) {
        f1.mkdirs();
    }
	//�½�һ��SmartUpload����
	SmartUpload su = new SmartUpload();
	//�ϴ���ʼ��
	su.initialize(pageContext);
	// �趨�ϴ�����
	//1.����ÿ���ϴ��ļ�����󳤶ȡ�
	su.setMaxFileSize(10000000);
	//2.�������ϴ����ݵĳ��ȡ�
	su.setTotalMaxFileSize(20000000);
	//3.�趨�����ϴ����ļ���ͨ����չ�����ƣ�,������doc,txt�ļ���
	su.setAllowedFilesList("doc,txt,jpg,rar,mid,waw,mp3,gif,xlsx,xls");
	boolean sign = true;
	//4.�趨��ֹ�ϴ����ļ���ͨ����չ�����ƣ�,��ֹ�ϴ�����exe,bat,jsp,htm,html��չ�����ļ���û����չ�����ļ���
	try {
		su.setDeniedFilesList("exe,bat,jsp,htm,html");
		//�ϴ��ļ�
		su.upload();
		//���ϴ��ļ����浽ָ��Ŀ¼
		su.save(savePath);
	} catch (Exception e) {
		e.printStackTrace();
		sign = false;
	}
	if(sign==true)
	{
		out.println("<script>parent.callback('upload file success')</script>");
	}else
	{
		out.println("<script>parent.callback('upload file error')</script>");
	}
%>
