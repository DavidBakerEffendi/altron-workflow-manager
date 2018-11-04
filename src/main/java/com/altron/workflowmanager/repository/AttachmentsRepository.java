package com.altron.workflowmanager.repository;

import com.altron.workflowmanager.domain.Attachments;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Attachments entity.
 */
@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {
	
	@Query("select attachments from Attachments attachments where attachments.email.id = ?1")
	List<Attachments> findAllByEmailID(long email_id);
	
	@Query(value = "SELECT attachments.id, attachments.name, attachments.attachment, attachments.attachment_content_type, attachments.email_id FROM attachments "
			+ "INNER JOIN email ON attachments.email_id=email.id "
			+ "WHERE email.id=?1", nativeQuery = true)
	public List<Attachments> getAttachments(Long id);
}
