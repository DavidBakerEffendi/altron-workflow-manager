package com.altron.workflowmanager.repository;

import com.altron.workflowmanager.domain.Milestone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data repository for the Milestone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

	@Query("select milestone from Milestone milestone where milestone.user.login = ?#{principal.username}")
	List<Milestone> findByUserIsCurrentUser();

	@Query(value = "select distinct milestone from Milestone milestone left join fetch milestone.dacs", countQuery = "select count(distinct milestone) from Milestone milestone")
	Page<Milestone> findAllWithEagerRelationships(Pageable pageable);

	@Query(value = "select distinct milestone from Milestone milestone left join fetch milestone.dacs")
	List<Milestone> findAllWithEagerRelationships();

	@Query("select milestone from Milestone milestone left join fetch milestone.dacs where milestone.id =:id")
	Optional<Milestone> findOneWithEagerRelationships(@Param("id") Long id);

	@Query("select milestone FROM Milestone milestone WHERE milestone.user.login = ?1")
	List<Milestone> findByUserLogin(String userLogin);

	@Query("SELECT milestone FROM Milestone milestone WHERE milestone.id=?1")
	public Milestone getMilestone(Long id);

	@Query(value = "SELECT COALESCE(project.ispr_number, 0) FROM milestone INNER JOIN project ON project.id=milestone.ispr_number_id WHERE milestone.id=?1", nativeQuery = true)
	public String getMilestoneISPR(Long id);

	@Query(value = "SELECT COALESCE(contact.first_name, 'UNASSIGNED') FROM milestone "
			+ "INNER JOIN project ON project.id=milestone.ispr_number_id "
			+ "INNER JOIN contact ON contact.id = project.contact_id " + "WHERE milestone.id=?1", nativeQuery = true)
	public String getContactFirstName(Long id);

	@Query(value = "SELECT COALESCE(contact.last_name, '') FROM milestone "
			+ "INNER JOIN project ON project.id=milestone.ispr_number_id "
			+ "INNER JOIN contact ON contact.id = project.contact_id " + "WHERE milestone.id=?1", nativeQuery = true)
	public String getContactLastName(Long id);

	@Query(value = "SELECT COALESCE(contact.email, '') FROM milestone "
			+ "INNER JOIN project ON project.id=milestone.ispr_number_id "
			+ "INNER JOIN contact ON contact.id = project.contact_id " + "WHERE milestone.id=?1", nativeQuery = true)
	public String getContactEmail(Long id);

	@Query(value = "SELECT COALESCE(jhi_user.first_name,'UNASSIGNED') FROM milestone "
			+ "INNER JOIN jhi_user ON jhi_user.id = milestone.user_id " + "WHERE milestone.id=?1", nativeQuery = true)
	public String getManagerFirstName(Long id);

	@Query(value = "SELECT COALESCE(jhi_user.last_name,'') FROM milestone "
			+ "INNER JOIN jhi_user ON jhi_user.id = milestone.user_id " + "WHERE milestone.id=?1", nativeQuery = true)
	public String getManagerLastName(Long id);

	@Query(value = "SELECT COALESCE(jhi_user.email,'') FROM milestone "
			+ "INNER JOIN jhi_user ON jhi_user.id = milestone.user_id " + "WHERE milestone.id=?1", nativeQuery = true)
	public String getManagerEmail(Long id);

	@Query(value =
			"SELECT milestone.id, milestone.name, milestone.due_date, milestone.previous_revenue, milestone.prereceipted_income, "
			+ "milestone.revenue_hold_back, milestone.revenue_in_next_fin_year, milestone.status, milestone.user_id, milestone.po_number_id, milestone.ispr_number_id "
			+ "FROM milestone INNER JOIN project ON milestone.ispr_number_id = project.id WHERE project.id=?1 ORDER BY milestone.due_date", nativeQuery = true)
	public Set<Milestone> getMilestonesByProjectId(Long id);

	@Query(value = "SELECT milestone.id, milestone.due_date, milestone.previous_revenue, milestone.prereceipted_income, milestone.revenue_hold_back, milestone.revenue_in_next_fin_year, milestone.status, milestone.user_id, milestone.po_number_id, milestone.ispr_number_id " +
			"FROM milestone INNER JOIN project ON project.id=milestone.ispr_number_id WHERE ispr_number=?1", nativeQuery=true)
	public Milestone getMilestonesByProjectName(String isprNumber);
}
