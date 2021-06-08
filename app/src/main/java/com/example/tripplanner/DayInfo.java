// 달력에 들어갈 데이터 클래스
package com.example.tripplanner;

public class DayInfo {
	private String day;
	private String mMonth;
	private String mYear;
	private boolean inMonth;
	private String sc_type;

	public DayInfo(String day, String mMonth, String mYear, boolean inMonth,
			String sc_type) {
		super();
		this.day = day;  //날짜
		this.mMonth = mMonth;//월
		this.mYear = mYear;//년
		this.inMonth = inMonth;//이번달에 들어가는 날짜인지 판단
		this.sc_type = sc_type;//내용
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getmMonth() {
		return mMonth;
	}

	public void setmMonth(String mMonth) {
		this.mMonth = mMonth;
	}

	public String getmYear() {
		return mYear;
	}

	public void setmYear(String mYear) {
		this.mYear = mYear;
	}

	public boolean isInMonth() {
		return inMonth;
	}

	public void setInMonth(boolean inMonth) {
		this.inMonth = inMonth;
	}

	public String getSc_type() {
		return sc_type;
	}

	public void setSc_type(String sc_type) {
		this.sc_type = sc_type;
	}

}
