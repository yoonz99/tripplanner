//������ ������
package com.example.tripplanner;

public class ScheduleData {
	private String day;// ��¥
	private String memo;// ������ �޸�

	public ScheduleData(String day, String memo) {
		super();
		this.day = day;
		this.memo = memo;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
