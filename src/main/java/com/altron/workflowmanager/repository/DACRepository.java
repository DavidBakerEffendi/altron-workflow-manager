package com.altron.workflowmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.altron.workflowmanager.domain.DAC;

/**
 * Spring Data repository for the DAC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DACRepository extends JpaRepository<DAC, Long> {

	@Query(value = "SELECT dac.id, dac.due_date, dac.description, dac.dac_amount, dac.status, dac.email_id FROM dac "
			+ "INNER JOIN milestone_dac ON dac.id=milestone_dac.dacs_id "
			+ "INNER JOIN milestone ON milestone_dac.milestones_id=milestone.id "
			+ "WHERE milestone.id = ?1", nativeQuery = true)
	public List<DAC> getMilestoneDACs(Long id);

	@Query(value = "SELECT DISTINCT dac.id, dac.due_date, dac.description, dac.dac_amount, dac.status, dac.email_id  FROM dac "
			+ "INNER JOIN milestone_dac ON milestone_dac.milestones_id = milestone_dac.dacs_id "
			+ "INNER JOIN milestone ON milestone_dac.dacs_id=dac.id "
			+ "INNER JOIN jhi_user ON milestone.user_id = jhi_user.id "
			+ "WHERE jhi_user.login = ?1", nativeQuery = true)
	List<DAC> findByUserLogin(String userLogin);

}
