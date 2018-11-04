package com.altron.workflowmanager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.Attachments;
import com.altron.workflowmanager.repository.AttachmentsRepository;
import com.altron.workflowmanager.service.EntityRelationService;
import com.altron.workflowmanager.web.rest.errors.BadRequestAlertException;
import com.altron.workflowmanager.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Attachments.
 */
@RestController
@RequestMapping("/api")
public class AttachmentsResource {

	private final Logger log = LoggerFactory.getLogger(AttachmentsResource.class);

	private static final String ENTITY_NAME = "attachments";

	private final AttachmentsRepository attachmentsRepository;
	private final EntityRelationService linkService;

	public AttachmentsResource(AttachmentsRepository attachmentsRepository, EntityRelationService linkService) {
		this.attachmentsRepository = attachmentsRepository;
		this.linkService = linkService;
	}

	/**
	 * POST /attachments : Create a new attachments.
	 *
	 * @param attachments
	 *            the attachments to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         attachments, or with status 400 (Bad Request) if the attachments has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/attachments")
	@Timed
	public ResponseEntity<Attachments> createAttachments(@Valid @RequestBody Attachments attachments)
			throws URISyntaxException {
		log.debug("REST request to save Attachments : {}", attachments);
		if (attachments.getId() != null) {
			throw new BadRequestAlertException("A new attachments cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Attachments result = attachmentsRepository.save(attachments);
		return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /attachments : Updates an existing attachments.
	 *
	 * @param attachments
	 *            the attachments to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         attachments, or with status 400 (Bad Request) if the attachments is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         attachments couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/attachments")
	@Timed
	public ResponseEntity<Attachments> updateAttachments(@Valid @RequestBody Attachments attachments)
			throws URISyntaxException {
		log.debug("REST request to update Attachments : {}", attachments);
		if (attachments.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Attachments result = attachmentsRepository.save(attachments);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachments.getId().toString())).body(result);
	}

	/**
	 * GET /attachments : get all the attachments.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of attachments
	 *         in body
	 */
	@GetMapping("/attachments")
	@Timed
	public List<Attachments> getAllAttachments() {
		log.debug("REST request to get all Attachments");
		return attachmentsRepository.findAll();
	}

	/**
	 * GET /attachments/:id : get the "id" attachments.
	 *
	 * @param id
	 *            the id of the attachments to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         attachments, or with status 404 (Not Found)
	 */
	@GetMapping("/attachments/{id}")
	@Timed
	public ResponseEntity<Attachments> getAttachments(@PathVariable Long id) {
		log.debug("REST request to get Attachments : {}", id);
		Optional<Attachments> attachments = attachmentsRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(attachments);
	}

	/**
	 * DELETE /attachments/:id : delete the "id" attachments.
	 *
	 * @param id
	 *            the id of the attachments to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/attachments/{id}")
	@Timed
	public ResponseEntity<Void> deleteAttachments(@PathVariable Long id) {
		log.debug("REST request to delete Attachments : {}", id);
		attachmentsRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	
}
