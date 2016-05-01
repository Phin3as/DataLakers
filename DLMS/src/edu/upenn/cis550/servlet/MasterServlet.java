package edu.upenn.cis550.servlet;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MasterServlet extends HttpServlet{
	static final long serialVersionUID = 455555001;

	public void init (ServletConfig config) throws ServletException{
		super.init(config);
		System.out.println("INFO: Master Servlet Initialized!");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws java.io.IOException{
		PrintWriter out = response.getWriter();
		  out.println("<!DOCTYPE html>"); // HTML 5
		  out.println("<html><head>");
		  out.println("</head>");
		  out.println("<body>");
		  out.println("Request Received!");
		  out.println("</body></html>");
		  out.close();
	}
}
