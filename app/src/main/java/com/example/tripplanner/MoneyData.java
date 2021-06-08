//물 섭취량 데이터
package com.example.tripplanner;

public class MoneyData {
	private String day;// 날짜
	private String money;
	private String cate;

	public MoneyData(String day, String money, String cate) {
		super();
		this.day = day;
		this.money = money;
		this.cate = cate;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

}
