package com.excel.testcode;

/**
 * 
 * 交易历史记录
 *
 */
public class TradeHistory {
	
	public TradeHistory() {
		super();
	}
	
	public TradeHistory(String cardno, String name, double tradeAmount) {
		super();
		this.cardno = cardno;
		this.name = name;
		this.tradeAmount = tradeAmount;
	}

	/**
	 * 卡号
	 */
	private String cardno;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 历史交易金额
	 */
	private double tradeAmount;
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(float tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	@Override
	public String toString() {
		return "{\"cardno\":\"" + this.cardno + "\", \"name\":\"" + this.name + "\", \"tradeAmount\":"
				+ this.tradeAmount + "}";
	}
	
}
