package com.altron.workflowmanager.web.rest;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.Company;
import com.altron.workflowmanager.repository.CompanyRepository;
import com.altron.workflowmanager.service.dto.CompanyStatisticsDTO;

/**
 * DashboardResource controller
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

	private final Logger log = LoggerFactory.getLogger(DashboardResource.class);
	private final CompanyRepository companyRepository;

	@Autowired
	public DashboardResource(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	/**
	 * GET getCompanies
	 * 
	 * @return A list of all companies and their fields from the companies table
	 *         including their project and milestone counts.
	 */
	@GetMapping("/get-companies")
	public ResponseEntity<List<CompanyStatisticsDTO>> getCompanies() {
		log.debug("REST request to get a list of all Companies");
		List<Company> allCompanies = companyRepository.findAll();
		List<CompanyStatisticsDTO> companyStats = new LinkedList<CompanyStatisticsDTO>();
		for (Company comp : allCompanies) {
			CompanyStatisticsDTO stats = new CompanyStatisticsDTO();
			stats.setName(comp.getCompanyName());
			stats.setDescription(comp.getDescription());
			stats.setProjectCount(companyRepository.getNumberOfProjectsForCompany(comp.getId()));
			stats.setMilestoneCount(companyRepository.getNumberOfMilestonesForCompany(comp.getId()));
			stats.setProjectRevenue(companyRepository.getProjectRevenue(comp.getId()));
			companyStats.add(stats);
		}

		return new ResponseEntity<List<CompanyStatisticsDTO>>(companyStats, null, HttpStatus.OK);
	}

}
