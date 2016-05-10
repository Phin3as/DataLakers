package edu.upenn.cis550.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upenn.cis550.storage.StorageAPI;
import edu.upenn.cis550.utils.Constants;

public class ListDocServlet extends HttpServlet{
	static final long serialVersionUID = 455555005;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		System.out.println("List Documents Request Received!!");
		String user = request.getParameter("user");
		System.out.println(user);
		
		/** open the database **/
		StorageAPI store = new StorageAPI(new File(Constants.PATH_DIR));
		HashSet<Integer> userDocs = store.getUserDocs(user);
		Iterator<Integer> itr = userDocs.iterator();
		List<ListDocuments> docList = new ArrayList<ListDocuments>();
		while (itr.hasNext()){
			int docID = itr.next();
			String docName = store.getDocName(docID);
			docList.add(new ListDocuments(docID, docName));
			System.out.println(docName);
		}
		store.closeDB();
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		try {
			String responseJSON = mapper.writeValueAsString(docList);
			System.out.println(responseJSON);
			response.getWriter().write(responseJSON);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
