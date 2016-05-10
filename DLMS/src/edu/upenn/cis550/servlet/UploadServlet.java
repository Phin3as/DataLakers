package edu.upenn.cis550.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import edu.upenn.cis550.extractor.Extract;
import edu.upenn.cis550.utils.Constants;

/**
 * Helper servlet to upload files to aws S3
 * @author Jitesh
 *
 */
public class UploadServlet extends HttpServlet{
	static final long serialVersionUID = 455555002;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		System.out.println("Upload Servlet GET Method");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
	    System.out.println("Upload Request Received!!");
		response.setContentType("text/html");
		String fileName = "";
		String accessType = "PRIVATE";
		String userName = "";
		PrintWriter out = response.getWriter();
		out.println("<html><head><title>File Uploader</title></head>");
		try {
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
	        for (FileItem item : items) {
	            if (!item.isFormField()) {
	                // Process form file field (input type="file").
	                //String fieldName = item.getFieldName();
	                //System.out.println(fieldName);
	                fileName = FilenameUtils.getName(item.getName());
	                System.out.println(fileName);

	                File uploadFile = new File(Constants.PATH_FILES,fileName);
	                if(uploadFile.exists()){
	                	uploadFile.delete();
	                }
	                try {
						item.write(uploadFile);
						//Run extractor
						//(new Extract()).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }else{
	                String fieldName = item.getFieldName();
	                if(fieldName.equals("user")){
		                userName = item.getString();
	                }
	            }
	        }
	        Extract extractor = new Extract(fileName,accessType,userName);
	        extractor.start();
            out.println("<body>File Successfully Uploaded to Workers!</body></html>");
            out.close();
	    } catch (FileUploadException e) {
	        throw new ServletException("Cannot parse multipart request.", e);
	    }
	}	
}
