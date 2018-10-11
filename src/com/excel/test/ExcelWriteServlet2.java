package com.excel.test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lnson.util.PoiExcelUtils;

import com.excel.testcode.UnitTest;

public class ExcelWriteServlet2 extends HttpServlet {

	private static final long serialVersionUID = -2821985935176429901L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doGet被访问");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		//写入客户端浏览器
		resp.getWriter().write("<font color='red'>doGet被访问</font>");

		//获取Servlet容器
		ServletContext sc = getServletContext();
		
		/**
		 * 读取Excel文件内容后解析数据批量生成T-SQL
		 */
		String filepath = sc.getRealPath("xls".equals(sc.getInitParameter("extension"))
				? "FILE-DIR/资产系统大中小类规格型号一览表.xls"
				: "FILE-DIR/资产系统大中小类规格型号一览表.xlsx");
		PoiExcelUtils eup = PoiExcelUtils.getPoi();
		List<TreeMap<String, String>> dataGrid = eup.readExcel(filepath, "Sheet1", "B6");
		eup.closeWorkbook();
		dataGrid.forEach(item -> {
			System.out.println(PoiExcelUtils.replaceBlank(String.valueOf(item)));
		});
		String sqlFilePath = sc.getRealPath(MessageFormat.format("WEB-INF/预生产环境T-SQL导入_{0}.txt",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		// 批量生成T-SQL
		UnitTest.createSqlStatement(sqlFilePath, dataGrid);
		
		/**
		 * 使用数据集创建Excel
		 */
		String fromFilePath = sc.getRealPath("FILE-DIR/trade.txt");
		String toFilePath = sc.getRealPath(MessageFormat.format("WEB-INF/trade_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		UnitTest.subject(fromFilePath, toFilePath);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doPost被访问");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		//向客户端写入响应内容
		resp.getWriter().write("<font color='red'>doPost被访问</font>");
		
		
		// 获取Servlet容器
		ServletContext sc = getServletContext();
		String tmpFilePath = sc.getRealPath("FILE-DIR/员工信息模板.xlsx");
		PoiExcelUtils eup = PoiExcelUtils.getPoi(tmpFilePath);
		
		/**
		 * 读取模板，根据单元格坐标写入数据后保存新文件到指定位置
		 */
		TreeMap<String, String> dataGrid = new TreeMap<String, String>();
		dataGrid.put("B2", "我是一枚程序猿");
		dataGrid.put("D4", "流年公子");
		dataGrid.put("H4", "29");
		dataGrid.put("D6", "666666");
		dataGrid.put("H6", "银河系");
		dataGrid.put("G14", "出界了");
		eup.writeTemplate(dataGrid);
		String savePath = sc.getRealPath(MessageFormat.format("WEB-INF/程序猿的思维_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		eup.saveWorkbookAs(savePath);
		
		dataGrid.put("G14", "once more");
		eup.writeTemplate(dataGrid);
		savePath = sc.getRealPath(MessageFormat.format("WEB-INF/程序猿的思维_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		eup.saveWorkbookAs(savePath);
		
		// 销毁wb对象
		eup.closeWorkbook();

	}
}
