package com.altron.workflowmanager.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.Company;
import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.domain.PO;
import com.altron.workflowmanager.repository.CompanyRepository;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.repository.PORepository;
import com.altron.workflowmanager.service.StatisticsService;
import com.altron.workflowmanager.service.dto.CompanyPageStatisticsDTO;
import com.altron.workflowmanager.service.dto.DashboardStatisticsDTO;
import com.altron.workflowmanager.service.dto.MilestoneStatisticsDTO;

/**
 * StatisticsResource controller
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsResource {

	private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

	private final MilestoneRepository milestoneRepository;
	private final CompanyRepository companyRepository;
	private final PORepository poRepository;
	private final StatisticsService statisticsService;

	@Autowired
	public StatisticsResource(MilestoneRepository milestoneRepository, CompanyRepository companyRepository,
			PORepository poRepository, StatisticsService statisticsService) {
		this.milestoneRepository = milestoneRepository;
		this.companyRepository = companyRepository;
		this.poRepository = poRepository;
		this.statisticsService = statisticsService;
	}

	/**
	 * GET getMilestoneStats
	 * 
	 * @return General statistics about a milestone.
	 */
	@GetMapping("/milestone-stats/{id}")
	public ResponseEntity<MilestoneStatisticsDTO> getMilestoneStats(@PathVariable Long id) {
		log.debug("Request for statistics on milestone : {}", id);
		MilestoneStatisticsDTO stats = new MilestoneStatisticsDTO();
		Milestone ms = milestoneRepository.getMilestone(id);

		stats.setIsprNumber(milestoneRepository.getMilestoneISPR(id));
		stats.setDueDate(ms.getDueDate());
		stats.setPrereceiptedIncome(ms.getPrereceiptedIncome());
		stats.setRevenueHoldBack(ms.getRevenueHoldBack());
		stats.setRevenueInNextFinYear(ms.getRevenueInNextFinYear());
		stats.setStatus(ms.getStatus());
		PO po = poRepository.getPoByMilestoneID(id);
		if (po != null) {
			stats.setPoAmount(po.getPoAmount());
		} else {
			stats.setPoAmount(0);
		}
		stats.setContactName(
				milestoneRepository.getContactFirstName(id) + " " + milestoneRepository.getContactLastName(id));
		stats.setContactEmail(milestoneRepository.getContactEmail(id));
		stats.setManagerName(
				milestoneRepository.getManagerFirstName(id) + " " + milestoneRepository.getManagerLastName(id));
		stats.setManagerEmail(milestoneRepository.getManagerEmail(id));

		return new ResponseEntity<MilestoneStatisticsDTO>(stats, null, HttpStatus.OK);
	}

	/**
	 * GET getStatistics
	 * 
	 * @return General statistics over all the data in the DB.
	 */
	@GetMapping("/dashboard-stats")
	public ResponseEntity<DashboardStatisticsDTO> getDashboardStats() {
		log.debug("Request for dashboard statistics");
		DashboardStatisticsDTO stats = new DashboardStatisticsDTO();

		stats.setCompanyCount(companyRepository.getCompanyCount());
		stats.setDacCount(companyRepository.getDacCount());
		stats.setPoCount(companyRepository.getPoCount());
		stats.setTotalRevenue(companyRepository.getTotalProjectRevenue());

		return new ResponseEntity<DashboardStatisticsDTO>(stats, null, HttpStatus.OK);
	}

	/**
	 * GET getCompanyStats
	 * 
	 * @param name
	 *            Company name
	 * @return Returns selected statistics about the given company.
	 */
	@GetMapping("/company-stats/{name}")
	public ResponseEntity<CompanyPageStatisticsDTO> getCompanyStats(@PathVariable String name) {
		log.debug("Request for statistics for company : {}", name);
		CompanyPageStatisticsDTO stats = new CompanyPageStatisticsDTO();
		stats.setTotalRevenue(statisticsService.getSumOfProjectRevenueByCompany(name));
		stats.setDacCount(statisticsService.getDacCountByCompany(name));
		stats.setPOCount(statisticsService.getPOsByCompany(name));
		return new ResponseEntity<CompanyPageStatisticsDTO>(stats, null, HttpStatus.OK);
	}

}
