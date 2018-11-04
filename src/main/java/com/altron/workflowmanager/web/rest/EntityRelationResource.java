package com.altron.workflowmanager.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.DAC;
import com.altron.workflowmanager.domain.Email;
import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.service.EntityRelationService;
import com.altron.workflowmanager.web.rest.util.HeaderUtil;

/**
 * RelationResource controller
 */
@RestController
@RequestMapping("/api/relation-resource")
public class EntityRelationResource {

	private final Logger log = LoggerFactory.getLogger(EntityRelationResource.class);

	private final EntityRelationService relationResource;

	@Autowired
	public EntityRelationResource(EntityRelationService relationService) {
		this.relationResource = relationService;
	}

	/**
	 * GET linkEmailToDac
	 */
	@GetMapping("/link-email-{emailId}-to-dac-{dacId}")
	public ResponseEntity<DAC> linkEmailToDac(@PathVariable long emailId, @PathVariable long dacId) {
		log.debug("Request to link dac {} to email {}", dacId, emailId);
		DAC result = this.relationResource.linkDACAndEmail(dacId, emailId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("dAC", result.getId().toString()))
				.body(result);
	}

	/**
	 * GET linkMilestoneToDac
	 */
	@GetMapping("/link-milestone-{milestoneId}-to-dac-{dacId}")
	public ResponseEntity<Milestone> linkMilestoneToDac(@PathVariable long milestoneId, @PathVariable long dacId) {
		log.debug("Request to link dac {} to milestone {}", dacId, milestoneId);
		Milestone result = this.relationResource.linkDACAndMilestone(dacId, milestoneId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("MILESTONE", result.getId().toString()))
				.body(result);
	}

	/**
	 * GET linkAttachmentToEmail
	 */
	@GetMapping("/link-attachment-{attachmentId}-to-email-{emailId}")
	public ResponseEntity<Email> linkAttachmentToEmail(@PathVariable long attachmentId, @PathVariable long emailId) {
		log.debug("Request to link attachment {} to email {}", attachmentId, emailId);
		Email result = this.relationResource.linkAttachmentToEmail(attachmentId, emailId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("EMAIL", result.getId().toString()))
				.body(result);
	}

	/**
	 * GET linkAttachmentToEmail
	 */
	@GetMapping("/link-po-{poId}-to-milestone-{milestoneId}")
	public ResponseEntity<Milestone> linkPoToMilestone(@PathVariable long poId, @PathVariable long milestoneId) {
		log.debug("Request to link po {} to milestone {}", poId, milestoneId);
		Milestone result = this.relationResource.linkPoToMilestone(poId, milestoneId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("MILESTONE", result.getId().toString()))
				.body(result);
	}

}
