package com.excel.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExcelWriteServlet extends HttpServlet {

	private static final long serialVersionUID = -2821985935176429901L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doGet被访问");
		//HttpServletRequest获取请求头信息
		//req.getHeaderNames()
		//req.getHeaders(arg0)
		//req.getHeader(arg0)
		
		
		
		//在doGet中转码
		//1、new String(req.getParameter("").getBytes("ISO-8859-1"), "UTF-8")
		//2、设置Server.xml文件中的Service节点下的Connector节点属性URIEncoding="UTF-8"
		//3、将url参数在客户端进行编码，在这里进行解码即可
		//Request请求转发服务器端跳转
		//req.getRequestDispatcher("").forward(req, resp);
		
		//客户端跳转
		//resp.sendRedirect(arg0);
		//设置响应头，刷新或跳转
		//resp.setHeader(arg0, arg1);
		
		//设置客户端浏览器的解码类型
		//resp.setCharacterEncoding(arg0);
		//设置响应类型，默认text/html
		//resp.setContentType(arg0);
		//写入客户端浏览器
		resp.getWriter().write("doGet被访问");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//设置服务端获取客户端参数的编码类型
		//req.setCharacterEncoding(arg0);//后期可在拦截器中实现
		//req.getParameterValues(arg0)
		
		System.out.println("doPost被访问");
		resp.getWriter().write("doPost被访问");
	}
}
