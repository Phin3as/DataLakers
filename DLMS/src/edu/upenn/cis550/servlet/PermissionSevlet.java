package edu.upenn.cis550.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.utils.Constants;

public class PermissionSevlet extends HttpServlet{
	static final long serialVersionUID = 455555004;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		System.out.println("Assign Permission Request Received!!");
		String docID = request.getParameter("docID");
		String docName = request.getParameter("docName");
		String userID = request.getParameter("userID");
		String user = request.getParameter("user");
		System.out.println(user);
		System.out.println(userID);
		System.out.println(docID);
		
		/** open the database **/
		StorageAPI store = new StorageAPI(new File(Constants.PATH_STORAGE));
		store.putDocument(Integer.parseInt(docID), "PRIVATE", docName, userID);
		store.putUserDoc(user, Integer.parseInt(docID));
		store.closeDB();
	}
}
