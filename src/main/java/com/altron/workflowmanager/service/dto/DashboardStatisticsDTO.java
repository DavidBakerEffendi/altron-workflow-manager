package com.altron.workflowmanager.service.dto;

import java.io.Serializable;

public class DashboardStatisticsDTO implements Serializable {

	private static final long serialVersionUID = -3823607466054784113L;

	private int companyCount;
	private int dacCount;
	private int poCount;
	private int totalRevenue;

	public int getCompanyCount() {
		return companyCount;
	}

	public void setCompanyCount(int companyCount) {
		this.companyCount = companyCount;
	}

	public int getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(int totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public int getDacCount() {
		return dacCount;
	}

	public void setDacCount(int dacCount) {
		this.dacCount = dacCount;
	}

	public int getPoCount() {
		return poCount;
	}

	public void setPoCount(int poCount) {
		this.poCount = poCount;
	}
}
