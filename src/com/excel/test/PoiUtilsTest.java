package com.excel.test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lnson.util.PoiExcelUtils;
import org.lnson.util.PoiExcelUtils.WorkbookTypeEnum;

public class PoiUtilsTest extends HttpServlet {

	/**
	 * 版本号
	 */
	private static final long serialVersionUID = -8362196071629071852L;

	/**
	 * 根据坐标写入测试
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext sc = getServletContext();
		String filepath = sc.getRealPath(MessageFormat.format("WEB-INF/根据坐标写入测试_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));

		PoiExcelUtils poi = PoiExcelUtils.getPoi(WorkbookTypeEnum.EXCEL_XLSX);
		// 创建写入的数据集
		List<TreeMap<Integer, String>> dataGrid = getDataSource();
		poi.writeExcel(dataGrid, "根据坐标写入测试", "B3");
		poi.writeExcel(dataGrid, "根据坐标写入测试", "H3");
		poi.writeExcel(dataGrid, "根据坐标写入测试", "B19");
		poi.writeExcel(dataGrid, "根据坐标写入测试", "H19");
		poi.writeExcel(dataGrid, "根据坐标写入测试", "E11");

		poi.saveWorkbookAs(filepath);
		poi.exportWorkbookAs(filepath.substring(filepath.lastIndexOf("\\") + 1), response);
		poi.closeWorkbook();
	}

	/**
	 * 根据坐标读取测试
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext sc = getServletContext();
		String filepath = sc.getRealPath("FILE-DIR/根据坐标读取测试.xlsx");
		PoiExcelUtils poi = PoiExcelUtils.getPoi();
		List<TreeMap<String, String>> dataGrid1 = poi.readExcel(filepath, "根据坐标读取测试", "B3", "C4");
		List<TreeMap<String, String>> dataGrid2 = poi.readExcel(filepath, "根据坐标读取测试", "I3", "J4");
		List<TreeMap<String, String>> dataGrid3 = poi.readExcel(filepath, "根据坐标读取测试", "B25", "C26");
		List<TreeMap<String, String>> dataGrid4 = poi.readExcel(filepath, "根据坐标读取测试", "I25", "J26");
		List<TreeMap<String, String>> dataGrid5 = poi.readExcel(filepath, "根据坐标读取测试", "E13", "G16");
		List<TreeMap<String, String>> dataGrid6 = poi.readExcel(filepath, "根据坐标读取测试");
		
		/**
		 * 打印验证，不能有星号
		 */
		dataGrid1.forEach(System.out::println);
		System.out.println("===================================");
		dataGrid2.forEach(System.out::println);
		System.out.println("===================================");
		dataGrid3.forEach(System.out::println);
		System.out.println("===================================");
		dataGrid4.forEach(System.out::println);
		System.out.println("===================================");
		dataGrid5.forEach(System.out::println);
		System.out.println("===================================");
		dataGrid6.forEach(System.out::println);
		//写回Excel进行对比
		poi.writeExcel(dataGrid6, "这个只是测试", "B2");
		
		filepath = sc.getRealPath(MessageFormat.format("WEB-INF/根据坐标读取测试_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		poi.exportWorkbookAs(filepath.substring(filepath.lastIndexOf("\\") + 1), response);
		poi.closeWorkbook();
	}

	/**
	 * 创建用于测试的数据源
	 */
	private List<TreeMap<Integer, String>> getDataSource() {
		List<TreeMap<Integer, String>> dataGrid = new ArrayList<TreeMap<Integer, String>>();
		TreeMap<Integer, String> columns = null;
		int cellValue = 1;
		for (int i = 13; i < 21; i++) {
			columns = new TreeMap<Integer, String>();
			for (int j = 13; j < 16; j++) {
				columns.put(j, String.valueOf(cellValue++));
			}
			dataGrid.add(columns);
		}
		return dataGrid;
	}
}
