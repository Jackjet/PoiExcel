package com.excel.test;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lnson.util.PoiExcelUtils;

import com.excel.testcode.Contacts;

public class ExcelWriteServlet3 extends HttpServlet {

	private static final long serialVersionUID = -2821985935176429901L;

	/**
	 * 读取Excel模板保存列表数据，实现行复制
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doGet被访问");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		//写入客户端浏览器
		resp.getWriter().write("<font color='red'>doGet被访问</font>");

		
		ServletConfig config = getServletConfig();
		ServletContext sc = config.getServletContext();
		
		// 载入Excel模板
		String fileTempPath = sc.getRealPath("FILE-DIR/联系人列表_模板.xlsx");
		PoiExcelUtils eup = PoiExcelUtils.getPoi(fileTempPath);
		//创建用于写入的数据集
		List<Contacts> users = getDataSource();
		List<TreeMap<Integer, String>> dataset = new ArrayList<TreeMap<Integer, String>>();
		TreeMap<Integer, String> columns = null;
		for (Contacts user : users) {
			columns = new TreeMap<Integer, String>();
			columns.put(0, user.getUserId());
			columns.put(1, user.getName());
			columns.put(2, user.getUserType());
			columns.put(3, user.getMobile());
			columns.put(4, user.getEmail());
			dataset.add(columns);
		}
		//写入数据
		eup.writeTemplateList(dataset, null, "E9");
		//导出Excel文件
		String fileSavePath = sc.getRealPath(MessageFormat.format("WEB-INF/联系人列表_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		eup.saveWorkbookAs(fileSavePath);
		//销毁对象
		eup.closeWorkbook();
	}
	
	/**
	 * 读取Excel模板保存列表数据并分Sheet页展示列表明细，实现行复制以及Sheet页复制
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doPost被访问");
		
		ServletContext sc = getServletContext();
		// 载入模板
		String fileTempPath = sc.getRealPath("FILE-DIR/联系人列表_模板.xlsx");
		PoiExcelUtils eup = PoiExcelUtils.getPoi(fileTempPath);
		// 创建用于导入的数据集
		List<Contacts> users = getDataSource();
		List<TreeMap<String, String>> dataset = new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> columns = null;
		for (int i = 0; i < users.size(); i++) {
			int rowNum = i + 9;
			Contacts user = users.get(i);
			columns = new TreeMap<String, String>();
			columns.put("E" + rowNum, user.getUserId());
			columns.put("F" + rowNum, user.getName());
			columns.put("G" + rowNum, user.getUserType());
			columns.put("H" + rowNum, user.getMobile());
			columns.put("I" + rowNum, user.getEmail());
			dataset.add(columns);
		}
		// 写入数据
		eup.writeTemplateList(dataset, null, "E9");
		
		// 每个Sheet页应该填充的数据
		TreeMap<String, String> sheetData = null;
		// 逐行将明细写入Sheet页
		for (int i = 0; i < dataset.size(); i++) {
			int rowNum = i + 9;
			TreeMap<String, String> grid = dataset.get(i);
			// 将userName作为Sheet页的命名
			String userName = grid.get("F" + rowNum);
			// 根据userId进行查询
			String userId = grid.get("E" + rowNum);
			// 此处得到的集合取第一个元素
			Contacts user = users.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList()).get(0);
			// 准备填充的数据
			sheetData = new TreeMap<String, String>();
			sheetData.put("D7", userId);// 联系人ID
			sheetData.put("D3", userName);// 姓名
			sheetData.put("D8", user.getUserType());// 联系人类型
			sheetData.put("D9", user.getMobile());// 联系电话
			sheetData.put("D10", user.getEmail());// 电子邮件
			sheetData.put("D11", user.getAddress());// 地址
			sheetData.put("D12", user.getCity());// 城市
			sheetData.put("D13", user.getPostalCode());// 邮政编码
			sheetData.put("D14", user.getCountry());// 国家/地区
			sheetData.put("D15", user.getProfession());// 联系人职务
			sheetData.put("D16", user.getRemark());// 备注
			
			// 封装一个方法，可以复制Sheet页
			eup.copySheet("联系人详细", userName);

			// 向Sheet页写入数据
			eup.writeTemplate(sheetData, userName);
		}
		
		// 当所有数据都写入Sheet页之后，将模板Sheet页删除
		eup.removeSheet("联系人详细");

		// 另存为
		String fileSavePath = sc.getRealPath(MessageFormat.format("WEB-INF/联系人列表_{0}.xlsx",
				new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())));
		eup.saveWorkbookAs(fileSavePath);
		
		// 导出
		eup.exportWorkbookAs(fileSavePath.substring(fileSavePath.lastIndexOf("\\") + 1), resp);
		
		// 销毁workbook对象
		eup.closeWorkbook();

		// 同一个HttpServletResponse对象只能调用getOutputStream()或getWriter()一次
		// resp.setCharacterEncoding("UTF-8");
		// resp.setContentType("text/html; charset=UTF-8");
		// 向客户端写入响应内容
		// resp.getWriter().write("<font color='red'>doPost被访问</font>");
	}
	
	private List<Contacts> getDataSource() {
		/**
		 * 读取Excel模板保存列表数据，实现行复制
		 */
		ServletConfig config = getServletConfig();
		ServletContext sc = config.getServletContext();
		
		PoiExcelUtils eup = PoiExcelUtils.getPoi();
		// 初始化数据源
		String fileSourcePath = sc.getRealPath("FILE-DIR/DatabaseTest.xlsx");
		List<TreeMap<String, String>> dataGrid = eup.readExcel(fileSourcePath);
		eup.closeWorkbook();
		
		dataGrid.forEach(System.out::println);
		List<Contacts> users = new ArrayList<Contacts>();
		for (int index = 0; index < dataGrid.size(); index++) {
			int rownum = index + 2;
			if (rownum < 3)
				continue;
			TreeMap<String, String> columns = dataGrid.get(index);
			String userId = columns.get("B" + rownum);
			String name = columns.get("C" + rownum);
			String userType = columns.get("D" + rownum);
			String mobile = columns.get("E" + rownum);
			String email = columns.get("F" + rownum);
			String address = columns.get("G" + rownum);
			String city = columns.get("H" + rownum);
			String postalCode = columns.get("I" + rownum);
			String country = columns.get("J" + rownum);
			String profession = columns.get("K" + rownum);
			String remark = columns.get("L" + rownum);
			Contacts user = new Contacts(userId, name, userType, mobile, email, address, city, postalCode, country,
					profession, remark);
			users.add(user);
		}
		Contacts[] userArray = new Contacts[users.size()];
		users.toArray(userArray);
		System.out.println(Arrays.toString(userArray));

		return users;
	}
}
