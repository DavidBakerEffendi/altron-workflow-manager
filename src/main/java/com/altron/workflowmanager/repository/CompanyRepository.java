package com.altron.workflowmanager.repository;

import com.altron.workflowmanager.domain.Company;
import com.altron.workflowmanager.domain.Project;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	@Query(value = "SELECT id FROM company WHERE company.company_name=?1", nativeQuery = true)
	public int getCompanyIdByCompanyName(String name);
	
	@Query(value = "SELECT COALESCE(count(cc_id), 0) " +
			"FROM project " + 
			"WHERE cc_id = ?1", nativeQuery = true)
	public int getNumberOfProjectsForCompany(Long cc_id);

	@Query(value = "SELECT COALESCE(count(milestone.ispr_number_id), 0) " + 
			"FROM milestone " +
			"INNER JOIN project ON project.id=milestone.ispr_number_id " + 
			"WHERE cc_id = ?1", nativeQuery = true)
	public int getNumberOfMilestonesForCompany(Long cc_id);

	@Query(value = "SELECT COALESCE(count(id), 0) " + 
			"FROM company", nativeQuery = true)
	public int getCompanyCount();

	@Query(value = "SELECT COALESCE(count(id), 0) " + 
			"FROM dac", nativeQuery = true)
	public int getDacCount();

	@Query(value = "SELECT COALESCE(count(id), 0) " + 
			"FROM po", nativeQuery = true)
	public int getPoCount();

	@Query(value = "SELECT COALESCE(sum(ispr_amount), 0) FROM project", nativeQuery = true)
	public int getTotalProjectRevenue();

	@Query(value = "SELECT COALESCE(sum(ispr_amount), 0) FROM project WHERE cc_id = ?1", nativeQuery = true)
	public int getProjectRevenue(Long cc_id);

	@Query(value = "SELECT * FROM company WHERE company_name = ?1", nativeQuery = true)
	public Company getCompanyByName(String name);

	@Query(value = "SELECT COALESCE(COUNT(dac.id), 0) FROM dac " + 
			"INNER JOIN milestone_dac ON milestone_dac.dacs_id=dac.id " + 
			"INNER JOIN milestone ON milestone_dac.milestones_id=milestone.id " + 
			"WHERE milestone.ispr_number_id = ?1", nativeQuery = true)
	public int getNumberOfDacsByProjectId(Long id);
	
	@Query(value = "SELECT SUM(ispr_amount) FROM project " + 
			"INNER JOIN company ON company.id=project.cc_id " +
			"WHERE company.company_name=?1", nativeQuery = true)
	public long getSumOfProjectRevenueByCompany(String name);

	@Query(value = "SELECT COALESCE(count(po.id), 0) FROM po " + 
			"INNER JOIN milestone ON milestone.po_number_id=po.id " + 
			"INNER JOIN project ON project.id=milestone.ispr_number_id " + 
			"INNER JOIN company ON company.id=project.cc_id " + 
			"WHERE company.company_name=?1", nativeQuery = true)
	public int getPOsByCompany(String name);

}
