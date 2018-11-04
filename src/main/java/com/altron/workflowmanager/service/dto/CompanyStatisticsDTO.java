package com.altron.workflowmanager.service.dto;

import java.io.Serializable;

public class CompanyStatisticsDTO implements Serializable {

	private static final long serialVersionUID = -6162899973415719245L;
	private String name;
	private String description;
	private int projectCount;
	private int milestoneCount;
	private int projectRevenue;

	public CompanyStatisticsDTO() {
		this.setName("");
		this.setDescription("");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProjectCount() {
		return projectCount;
	}

	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}

	public int getMilestoneCount() {
		return milestoneCount;
	}

	public void setMilestoneCount(int milestoneCount) {
		this.milestoneCount = milestoneCount;
	}

	public int getProjectRevenue() {
		return projectRevenue;
	}

	public void setProjectRevenue(int projectRevenue) {
		this.projectRevenue = projectRevenue;
	}

}
