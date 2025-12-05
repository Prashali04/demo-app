package com.example;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/*")
public class DemoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Demo App - Day 4</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 40px; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".container { border: 1px solid #ccc; padding: 20px; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        
        if (path != null && path.contains("health")) {
            out.println("<h1>Health Check</h1>");
            out.println("<p class='success'>✅ OK - Tomcat Deployment Working</p>");
        } else {
            out.println("<h1>🚀 Demo Application - Day 4</h1>");
            out.println("<p class='success'>✅ Successfully deployed to Tomcat 10</p>");
            out.println("<hr>");
            out.println("<h3>Tasks Completed:</h3>");
            out.println("<ol>");
            out.println("<li>Continuous Deployment to Tomcat</li>");
            out.println("<li>Automated Rollback Mechanism</li>");
            out.println("<li>Notification System</li>");
            out.println("</ol>");
            out.println("<hr>");
            out.println("<p><strong>Tomcat Port:</strong> 8081</p>");
            out.println("<p><strong>Application Path:</strong> /demo-app</p>");
            out.println("<p><strong>Health Check:</strong> <a href='health'>/health</a></p>");
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
