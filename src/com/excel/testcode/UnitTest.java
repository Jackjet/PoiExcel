package com.excel.testcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.lnson.util.PoiExcelUtils;
import org.lnson.util.PoiExcelUtils.WorkbookTypeEnum;

public class UnitTest {

	public static void createSqlStatement(String projPath, List<TreeMap<String, String>> dataGrid) {
		Set<String> bigSqlColl = new LinkedHashSet<String>();
		Set<String> midSqlColl = new LinkedHashSet<String>();
		Set<String> smallSqlColl = new LinkedHashSet<String>();
		Set<String> nameSqlColl = new LinkedHashSet<String>();
		Set<String> assetsSqlColl = new LinkedHashSet<String>();
		
		
		int rowsCount = dataGrid.size();
		for (int i = 0; i < rowsCount; i++) {
			//确认行号
//			int rowNum = i + 1;
			TreeMap<String, String> columns = dataGrid.get(i);
//			if (rowNum < 2) {
//				continue;
//			}
			
			Collection<String> coll = columns.values();
			String[] values = new String[coll.size()];
			coll.toArray(values);
			
			// 逐列读取
			// String companyCode = values[0];//columns.get("A" + rowNum);//集团编码A
			String subCompanyCode = values[1];//columns.get("B" + rowNum);// 子集团编码B
			String assetBigType = values[2];//columns.get("C" + rowNum);// 资产大类C
			String assetBigTypeCode = values[3];//columns.get("D" + rowNum);// 对应代码D
			String assetMidType = values[4];//columns.get("E" + rowNum);// 资产中类E
			String assetMidTypeCode = values[5];//columns.get("F" + rowNum);// 对应代码F
			String assetSmallType = values[6];//columns.get("G" + rowNum);// 资产小类G
			String assetSmallTypeCode = values[7];//columns.get("H" + rowNum);// 对应代码H
			String assetName = values[8];//columns.get("I" + rowNum);// 资产名称I
			String assetNameCode = values[9];//columns.get("J" + rowNum);// 对应代码J
			String unit = values[10];//columns.get("K" + rowNum);// 单位K
			String assetCategory = values[11];//columns.get("L" + rowNum).trim();// 资产分类L
			assetCategory = assetCategory.equals("固定资产") ? "GU_DING_AT" : "DI_ZHI_AT";
			// String assetSubject = values[12];//columns.get("M" + rowNum);//财务入账科目M
			String assetCode = values[13];//columns.get("N" + rowNum);// 资产编码N

			/**
			 * 根据以上字段生成T-SQL语句
			 */
			// 插入语句
			String baseSql = "INSERT INTO T_ASSETS_TYPE(ID,ASSETS_CODE,ASSETS_NAME,UNIQUE_CODE,SUP_ASSETS_CODE,ASSETS_TYPE,MODEL,CREATE_TIME,CREATE_NAME_CODE,MODIFY_NAME_CODE,MODIFY_TIME,REMARK,UNIT,STATUS)VALUES";
			String basicSql = "INSERT INTO T_ASSETS_ALLOCATION (ID,MAIN_CODE,MAIN_CODE_STATUS,UNIQUE_CODE,ASSETS_QUALITY,BEL_GROUP_CODE,CREATE_TIME,CREATE_USER_CODE,SCAN_STATUS,IF_ALLOCATION,IF_PRINT)VALUES";

			// 资产大类T-SQL：ASSET_BIG_TYPE
			String bigSql = "(SEQ_TAB_ASSETSTYPE_ID.NEXTVAL,'" + assetBigTypeCode + "','" + assetBigType + "','"
					+ assetBigTypeCode
					+ "','NO_UP_CODE','ASSET_BIG_TYPE',NULL,SYSDATE,'000000',NULL,NULL,NULL,NULL,'enable');";

			// 资产中类T-SQL：ASSET_MID_TYPE
			String midSql = "(SEQ_TAB_ASSETSTYPE_ID.NEXTVAL,'" + assetMidTypeCode + "','" + assetMidType + "','"
					+ (assetBigTypeCode + "-" + assetMidTypeCode) + "','" + assetBigTypeCode
					+ "','ASSET_MID_TYPE',NULL,SYSDATE,'000000',NULL,NULL,NULL,NULL,'enable');";

			// 资产小类T-SQL：ASSET_SMALL_TYPE
			String smallSql = "(SEQ_TAB_ASSETSTYPE_ID.NEXTVAL,'" + assetSmallTypeCode + "','" + assetSmallType + "','"
					+ (assetBigTypeCode + "-" + assetMidTypeCode + "-" + assetSmallTypeCode) + "','"
					+ (assetBigTypeCode + "-" + assetMidTypeCode)
					+ "','ASSET_SMALL_TYPE',NULL,SYSDATE,'000000',NULL,NULL,NULL,NULL,'enable');";

			// 资产类型T-SQL：ASSET_NAME_TYPE
			String nameSql = "(SEQ_TAB_ASSETSTYPE_ID.NEXTVAL,'" + assetNameCode + "','" + assetName + "','"
					+ (assetBigTypeCode + "-" + assetMidTypeCode + "-" + assetSmallTypeCode + "-" + assetNameCode)
					+ "','" + (assetBigTypeCode + "-" + assetMidTypeCode + "-" + assetSmallTypeCode)
					+ "','ASSET_NAME_TYPE',NULL,SYSDATE,'000000',NULL,NULL,NULL,'" + unit + "','enable');";

			// 资产品牌分类T-SQL：ASSET_BRAND_TYPE
			// String brandSql =
			// "(SEQ_TAB_ASSETSTYPE_ID.NEXTVAL,V_ASSETS_CODE,V_ASSETS_NAME,V_UNIQUE_CODE,V_SUP_ASSETS_CODE,'ASSET_BRAND_TYPE',NULL,SYSDATE,'000000',NULL,NULL,NULL,'"+unit+"','enable');";

			// 资产信息
			String assetsSql = "(SEQ_TAB_ALLOCATION_ID.NEXTVAL,'" + assetCode + "',1,'"
					+ (assetBigTypeCode + "-" + assetMidTypeCode + "-" + assetSmallTypeCode + "-" + assetNameCode)
					+ "','" + assetCategory + "','" + subCompanyCode + "',SYSDATE,'000000',0,0,0);";

			bigSqlColl.add(baseSql + bigSql);
			midSqlColl.add(baseSql + midSql);
			smallSqlColl.add(baseSql + smallSql);
			nameSqlColl.add(baseSql + nameSql);
			assetsSqlColl.add(basicSql + assetsSql);
		}

		// 输出到文本文件中
		writeToTxt(projPath, bigSqlColl, "--资产大类");
		writeToTxt(projPath, midSqlColl, "--资产中类");
		writeToTxt(projPath, smallSqlColl, "--资产小类");
		writeToTxt(projPath, nameSqlColl, "--资产名称");
		writeToTxt(projPath, assetsSqlColl, "--新增资产");
	}

	public static void writeToTxt(String projPath, Set<String> coll, String desc) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projPath, true), "UTF-8"));
			writer.write("\r\n");
			writer.write(desc);
			writer.write("\r\n");
			writer.flush();
			for (String str : coll) {
				writer.write(str);
				writer.write("\r\n");
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void subject(String fromFilePath, String toFilePath) {

		// 创建表头
		List<String> columns = new ArrayList<String>();
		
		// 创建表数据集合
		List<TradeHistory> resultData = new LinkedList<TradeHistory>();
		
		// 读取文本文件向表头和表数据集合添加元素
		getTradeData(fromFilePath, columns, resultData);

		// 新增打印表头"交易总额"
		columns.add("交易总额");

		// 分组统计所有卡号的交易总额
		Map<String, Double> groupAmountSum = resultData.stream().collect(
				Collectors.groupingBy(TradeHistory::getCardno, Collectors.summingDouble(TradeHistory::getTradeAmount)));

		// 分组统计所有卡号的历史交易单笔最大金额
		Map<String, Optional<TradeHistory>> groupAmountMax = resultData.stream()
				.collect(Collectors.groupingBy(TradeHistory::getCardno,
						Collectors.maxBy((t1, t2) -> t1.getTradeAmount() - t2.getTradeAmount() > 0 ? 1 : -1)));

		// 创建用于排序的集合
		List<TradePreview> tradeData = new ArrayList<TradePreview>();
		groupAmountSum.entrySet().forEach(item -> {
			double singleMaxAmount = groupAmountMax.get(item.getKey()).get().getTradeAmount();
			tradeData.add(new TradePreview(item.getKey(), singleMaxAmount, item.getValue()));
		});

		// 按照交易总额进行排名，若交易总额相同，则按照最大单笔交易金额排名
		tradeData.sort((t1, t2) -> {
			double val = t2.getTotalAmount() - t1.getTotalAmount();
			if (val == 0) {
				val = t2.getSingleMaxAmount() - t1.getSingleMaxAmount();
			}
			return val > 0 ? 1 : -1;
		});

		
		// 格式化要写入到Excel的数据集
		List<TreeMap<Integer, String>> dataResult = writeTradeData(columns, resultData, tradeData);
		// 写入Excel中
		PoiExcelUtils eup = PoiExcelUtils.getPoi(WorkbookTypeEnum.EXCEL_XLSX);
		eup.writeExcel(dataResult, null, "D5");
		eup.saveWorkbookAs(toFilePath);
		eup.closeWorkbook();
	}
	
	/**
	 * 读取文本文件获取表头以及表数据
	 * 
	 * @param columns
	 *            表头
	 * @param resultData
	 *            表数据
	 */
	public static void getTradeData(String filepath, List<String> columns, List<TradeHistory> resultData) {
		// 创建高效缓冲区字符输入流对象
		BufferedReader reader = null;
		try {
			// 读取文本文件
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// TXT文本文件以单字符空格分割内容
				String[] buf = line.split(" ");
				// 先向表头添加元素
				if (columns.size() <= 0) {
					Arrays.asList(buf).forEach(title -> columns.add(title));
					continue;
				}
				// 解析TXT文本行数据
				String cardno = buf[0];
				String name = buf[1];
				double tradeAmount1 = Double.parseDouble(buf[2]);
				double tradeAmount2 = Double.parseDouble(buf[3]);
				double tradeAmount3 = Double.parseDouble(buf[4]);
				// 再向表数据集合添加元素
				resultData.add(new TradeHistory(cardno, name, tradeAmount1));
				resultData.add(new TradeHistory(cardno, name, tradeAmount2));
				resultData.add(new TradeHistory(cardno, name, tradeAmount3));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭输入流对象
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将表头以及统计后的数据写入Excel中
	 * 
	 * @param columns
	 *            表头数据
	 * @param resultData
	 *            历史交易数据
	 * @param tradeData
	 *            历史交易统计数据
	 */
	public static List<TreeMap<Integer, String>> writeTradeData(List<String> columns, List<TradeHistory> resultData, List<TradePreview> tradeData) {
		List<TreeMap<Integer, String>> dataGrid = new ArrayList<TreeMap<Integer, String>>();
		
		// 写入Excel头信息
		TreeMap<Integer, String> thead = new TreeMap<Integer, String>();
		for (int i = 0; i < columns.size(); i++) {
			thead.put(i, columns.get(i));
		}
		dataGrid.add(thead);
		
		
		TreeMap<Integer, String> tbody = null;
		// 逐行写入历史交易数据和统计数据
		for (TradePreview trade : tradeData) {
			List<TradeHistory> accountTradeData = resultData.stream()
					.filter(t -> t.getCardno().equals(trade.getCardno())).collect(Collectors.toList());

			tbody = new TreeMap<Integer, String>();
			tbody.put(0, trade.getCardno());// 卡号
			tbody.put(1, accountTradeData.get(0).getName());// 姓名
			tbody.put(2, String.valueOf(accountTradeData.get(0).getTradeAmount()));// 交易金额1
			tbody.put(3, String.valueOf(accountTradeData.get(1).getTradeAmount()));// 交易金额2
			tbody.put(4, String.valueOf(accountTradeData.get(2).getTradeAmount()));// 交易金额3
			tbody.put(5, String.valueOf(trade.getTotalAmount()));// 交易总额

			dataGrid.add(tbody);
		}
		
		return dataGrid;
	}
}
