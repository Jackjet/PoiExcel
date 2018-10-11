package com.excel.testcode;

/**
 * 
 * 交易总览
 *
 */
public class TradePreview {
	
	public TradePreview() {
		super();
	}
	public TradePreview(String cardno, double singleMaxAmount, double totalAmount) {
		super();
		this.cardno = cardno;
		this.singleMaxAmount = singleMaxAmount;
		this.totalAmount = totalAmount;
	}
	
	/**
	 * 卡号
	 */
	private String cardno;
	/**
	 * 单笔最大交易金额
	 */
	private double singleMaxAmount;
	/**
	 * 交易总额
	 */
	private double totalAmount;
	
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public double getSingleMaxAmount() {
		return singleMaxAmount;
	}
	public void setSingleMaxAmount(double singleMaxAmount) {
		this.singleMaxAmount = singleMaxAmount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Override
	public String toString() {
		return "{\"cardno\":\"" + cardno + "\", \"singleMaxAmount\":" + singleMaxAmount + ", \"totalAmount\":"
				+ totalAmount + "}";
	}
	
}
