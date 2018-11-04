package com.altron.workflowmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altron.workflowmanager.domain.Company;
import com.altron.workflowmanager.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {

	private final Logger log = LoggerFactory.getLogger(CompanyService.class);

	private CompanyRepository companyRepository;

	@Autowired
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	/**
	 * Returns the company entity by company name.
	 * 
	 * @param name
	 *            Name of the company.
	 * @return Company entity if found, null otherwise.
	 */
	public Company getCompanyByName(String name) {
		return companyRepository.getCompanyByName(name);
	}

}
