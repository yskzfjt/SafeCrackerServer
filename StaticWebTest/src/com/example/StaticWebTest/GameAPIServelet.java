/**
 * 
 */
package com.example.StaticWebTest;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;

//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;

/**
 * @author Kaz
 *
 */
public class GameAPIServelet extends HttpServlet {
//     public void doGet(HttpServletRequest request, HttpServletResponse response)
// 	throws IOException {
//         response.setContentType("text/html");
//         request.setCharacterEncoding("utf8");
//         response.setCharacterEncoding("utf8");
//         PrintWriter out = response.getWriter();
//         out.println("<html><head></head><body><h1>Hello, world</h1><p>this is sample servlet.</p></body></html>");
//     }


//     void SQLWrite( CommonData cd ){
// 	String url = null;
// 	if (SystemProperty.environment.value() ==
// 	    SystemProperty.Environment.Value.Production) {
// 	    // Connecting from App Engine.
// 	    // Load the class that provides the "jdbc:google:mysql://"
// 	    // prefix.
// 	    Class.forName("com.mysql.jdbc.GoogleDriver");
// 	    url ="jdbc:google:mysql://safecracker-1096:safecracker-1096-sql?user=root";
// 	} else {
// 	    // You may also assign an IP Address from the access control
// 	    // page and use it to connect from an external network.
// 	    Class.forName("com.mysql.jdbc.Driver");
// 	    url = "jdbc:mysql://127.0.0.1:3306/guestbook?user=root";
// 	}
	
// 	Connection conn = DriverManager.getConnection(url);
// 	ResultSet rs = conn.createStatement().executeQuery( "SELECT 1 + 1");
//     }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
	response.setContentType("application/xml");
	request.setCharacterEncoding("utf8");
	response.setCharacterEncoding("utf8");
	
	//formの値を取り出す。
	String xmlString = request.getParameter( "commonData" );
// 	System.out.println( "---------xml----------\n" + xmlString + "\n");
	
	CommonData cd = new CommonData();
	try {
		if( cd.Parse(xmlString) ){
		    cd.DoTransaction();

		    PrintWriter pw = response.getWriter();
		    JAXB.marshal(cd, pw);
		    
// 		    System.out.println( "---------obj----------\n");
// 		    JAXB.marshal(cd, System.out);
		}
	} catch (XPathExpressionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    }


    
}
