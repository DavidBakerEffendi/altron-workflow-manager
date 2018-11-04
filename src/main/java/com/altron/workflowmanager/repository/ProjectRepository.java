package com.altron.workflowmanager.repository;

import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.domain.Project;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query(value = "SELECT project.id, project.ispr_number,  project.ispr_amount, project.ispr_description, project.start_date, project.contact_id, project.cc_id "
			+ "FROM company INNER JOIN project ON project.cc_id=company.id WHERE company.company_name=?1", nativeQuery = true)
	public List<Project> getProjectsByCompanyName(String name);

	@Query(value = "SELECT * FROM project WHERE ispr_number=?1 LIMIT 1", nativeQuery = true)
	public Project getProjectByIsprNumber(String ispr);

	@Query(value = "SELECT project.id, project.ispr_number,  project.ispr_amount, project.ispr_description, project.start_date, project.contact_id, project.cc_id "
			+ "FROM company INNER JOIN project ON project.cc_id=company.id "
			+ "WHERE company.company_name=?1 AND project.ispr_number=?2 LIMIT 1", nativeQuery = true)
	public Project getProjectByName(String name, String comp);

	@Query(value = "SELECT COALESCE(SUM(dac_values.dac_amount), 0) FROM "
			+ "(SELECT dac_amount FROM milestone INNER JOIN milestone_dac ON milestone.id = milestone_dac.milestones_id INNER JOIN dac ON milestone_dac.dacs_id = dac.id "
			+ "WHERE dac.status='INVOICED' AND milestone.ispr_number_id=?1) dac_values", nativeQuery = true)
	public Long getProjectInvoicedIncome(Long id);

	@Query(value = "SELECT COALESCE(SUM(dac_values.dac_amount), 0) FROM "
			+ "(SELECT dac_amount FROM milestone INNER JOIN milestone_dac ON milestone.id = milestone_dac.milestones_id INNER JOIN dac ON milestone_dac.dacs_id = dac.id "
			+ "WHERE dac.status<>'DECLINED' AND milestone.ispr_number_id=?1) dac_values", nativeQuery = true)
	public Long getProjectProjectedIncome(Long id);

	@Query(value = "SELECT COALESCE(COUNT(DISTINCT(dac_values.dacs_id)), 0) FROM "
			+ "(SELECT dacs_id FROM milestone INNER JOIN milestone_dac ON milestone.id = milestone_dac.milestones_id INNER JOIN dac ON milestone_dac.dacs_id = dac.id "
			+ "WHERE milestone.ispr_number_id=?1) dac_values", nativeQuery = true)
	public Long getProjectProjectedDacs(Long id);

	@Query(value = "SELECT COALESCE(COUNT(DISTINCT(pos.po_number_id)), 0) FROM "
			+ "(SELECT milestone.po_number_id FROM milestone WHERE milestone.ispr_number_id=1) pos", nativeQuery = true)
	public Long getProjectNumPo(Long id);

	@Query(value = "SELECT project.id FROM project WHERE ispr_number=?1 LIMIT 1", nativeQuery = true)
	public long getProjectIdByIsprNumber(String isprNo);
}
