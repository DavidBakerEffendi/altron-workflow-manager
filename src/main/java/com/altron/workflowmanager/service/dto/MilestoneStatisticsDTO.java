package com.altron.workflowmanager.service.dto;

import java.io.Serializable;
import java.time.Instant;

import com.altron.workflowmanager.domain.enumeration.MilestoneStatus;

public class MilestoneStatisticsDTO implements Serializable {

	private static final long serialVersionUID = -6162899973415719245L;
	private Instant dueDate;
	private String isprNumber;
	private int previousRevenue;
	private int prereceiptedIncome;
	private int revenueHoldBack;
	private int revenueInNextFinYear;
	private int poAmount;
	private MilestoneStatus status;
	private String managerName;
	private String managerEmail;
	private String contactName;
	private String contactEmail;

	public int getPreviousRevenue() {
		return previousRevenue;
	}

	public void setPreviousRevenue(int previousRevenue) {
		this.previousRevenue = previousRevenue;
	}

	public Instant getDueDate() {
		return dueDate;
	}

	public void setDueDate(Instant dueDate) {
		this.dueDate = dueDate;
	}

	public int getPrereceiptedIncome() {
		return prereceiptedIncome;
	}

	public void setPrereceiptedIncome(int prereceiptedIncome) {
		this.prereceiptedIncome = prereceiptedIncome;
	}

	public int getRevenueHoldBack() {
		return revenueHoldBack;
	}

	public void setRevenueHoldBack(int revenueHoldBack) {
		this.revenueHoldBack = revenueHoldBack;
	}

	public int getRevenueInNextFinYear() {
		return revenueInNextFinYear;
	}

	public void setRevenueInNextFinYear(int revenueInNextFinYear) {
		this.revenueInNextFinYear = revenueInNextFinYear;
	}

	public MilestoneStatus getStatus() {
		return status;
	}

	public void setStatus(MilestoneStatus status) {
		this.status = status;
	}

	public String getIsprNumber() {
		return isprNumber;
	}

	public void setIsprNumber(String isprNumber) {
		this.isprNumber = isprNumber;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public int getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(int poAmount) {
		this.poAmount = poAmount;
	}

}
