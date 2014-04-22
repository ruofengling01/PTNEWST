package org.tnt.pt.web.serverlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tnt.pt.entity.BusinessFile;
import org.tnt.pt.service.ptProcess.ExamService;

@SuppressWarnings("serial")
public class UploadFile extends HttpServlet {
	
    
	
    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
    	ExamService examService = (ExamService) ctx.getBean("ExamServiceImpl"); 
        String savePath = this.getServletConfig().getServletContext()
                .getRealPath("/attached/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
        savePath = savePath + "/uploads/"+ ymd + "/";
        File f1 = new File(savePath);
        System.out.println(savePath);
        if (!f1.exists()) {
            f1.mkdirs();
        }
        String businessId = request.getParameter("businessId");
        DiskFileItemFactory fac = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setHeaderEncoding("utf-8");
        List fileList = null;
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            return;
        }
        Iterator<FileItem> it = fileList.iterator();
        String name = "";
        String extName = "";
        while (it.hasNext()) {
            FileItem item = it.next();
            if (!item.isFormField()) {
                name = item.getName();
                long size = item.getSize();
                String type = item.getContentType();
                System.out.println(size + " " + type);
                if (name == null || name.trim().equals("")) {
                    continue;
                }
                //扩展名格式：  
                if (name.lastIndexOf(".") >= 0) {
                    extName = name.substring(name.lastIndexOf("."));
                }
                File file = null;
                do {
                    //生成文件名：
//                    name = UUID.randomUUID().toString();
                    file = new File(savePath + name + extName);
                } while (file.exists());
                File saveFile = new File(savePath + name + extName);
                try {
                    item.write(saveFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BusinessFile bFile = new BusinessFile();
                bFile.setBusinessId(Long.valueOf(businessId));
    			bFile.setFileName(name);
    			bFile.setFilePath(savePath + name + extName);
    			bFile.setUploadDate(new Date());
    			String filePathString = ""+examService.getFilePath(Long.valueOf(businessId), name);
    			if(!filePathString.equals(bFile.getFilePath())){
    				examService.insertFile(bFile);
    			}
            }
        }
        response.getWriter().print(name + extName);
    }
}
