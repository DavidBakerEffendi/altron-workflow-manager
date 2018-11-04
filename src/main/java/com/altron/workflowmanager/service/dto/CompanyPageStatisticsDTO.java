package com.altron.workflowmanager.service.dto;

import java.io.Serializable;

public class CompanyPageStatisticsDTO implements Serializable {

	private static final long serialVersionUID = 2915635435294354334L;
	private int dacCount;
	private long totalRevenue;
	private int totalInvoiced;
	private int poCount;

	public int getDacCount() {
		return dacCount;
	}

	public void setDacCount(int dacCount) {
		this.dacCount = dacCount;
	}

	public int getPOCount() {
		return poCount;
	}

	public void setPOCount(int poCount) {
		this.poCount = poCount;
	}

	public long getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(long totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public int getTotalInvoiced() {
		return totalInvoiced;
	}

	public void setTotalInvoiced(int totalInvoiced) {
		this.totalInvoiced = totalInvoiced;
	}

}
