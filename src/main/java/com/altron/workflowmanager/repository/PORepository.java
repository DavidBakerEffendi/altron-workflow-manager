package com.altron.workflowmanager.repository;

import com.altron.workflowmanager.domain.PO;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PORepository extends JpaRepository<PO, Long> {

	@Query(value = "SELECT po.id, po.po_amount FROM milestone INNER JOIN po ON po.id = milestone.po_number_id WHERE milestone.id=?1", 
			nativeQuery = true)
	public PO getPoByMilestoneID(Long id);
	
}
