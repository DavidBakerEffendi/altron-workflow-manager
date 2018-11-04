package com.altron.workflowmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altron.workflowmanager.domain.Company;
import com.altron.workflowmanager.domain.Project;
import com.altron.workflowmanager.repository.CompanyRepository;

@Service
@Transactional
public class StatisticsService {
	private final Logger log = LoggerFactory.getLogger(EntityRelationService.class);
	private final CompanyRepository companyRepository;

	@Autowired
	public StatisticsService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	/**
	 * Returns the number of DACs linked to the company via its projects.
	 * 
	 * @param name
	 *            Company name
	 * @return Number of DACs linked to company.
	 */
	public int getDacCountByCompany(String name) {
		Company company = companyRepository.getCompanyByName(name);
		log.debug("Retrieving number of DACS for {}", company.getCompanyName());
		int dacCount = 0;
		for (Project p : company.getProjects()) {
			dacCount += companyRepository.getNumberOfDacsByProjectId(p.getId());
		}
		return dacCount;
	}

	/**
	 * Returns the sum of project revenue by projects under the given company name.
	 * 
	 * @param name
	 *            Company name
	 * @return Sum of project revenue.
	 */
	public long getSumOfProjectRevenueByCompany(String name) {
		return companyRepository.getSumOfProjectRevenueByCompany(name);
	}

	/**
	 * Returns the number of POs under the milestones under the projects of the
	 * given company name.
	 * 
	 * @param name
	 *            Company name
	 * @return Number of POs
	 */
	public int getPOsByCompany(String name) {
		return companyRepository.getPOsByCompany(name);
	}

}
