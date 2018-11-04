package com.altron.workflowmanager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.service.EntityRelationService;
import com.altron.workflowmanager.web.rest.errors.BadRequestAlertException;
import com.altron.workflowmanager.web.rest.util.HeaderUtil;
import com.altron.workflowmanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Milestone.
 */
@RestController
@RequestMapping("/api")
public class MilestoneResource {

	private final Logger log = LoggerFactory.getLogger(MilestoneResource.class);

	private static final String ENTITY_NAME = "milestone";

	private final MilestoneRepository milestoneRepository;
	private final EntityRelationService linkService;

	@Autowired
	public MilestoneResource(MilestoneRepository milestoneRepository, EntityRelationService linkService) {
		this.milestoneRepository = milestoneRepository;
		this.linkService = linkService;
	}

	/**
	 * POST /milestones : Create a new milestone.
	 *
	 * @param milestone
	 *            the milestone to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         milestone, or with status 400 (Bad Request) if the milestone has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/milestones")
	@Timed
	public ResponseEntity<Milestone> createMilestone(@Valid @RequestBody Milestone milestone)
			throws URISyntaxException {
		log.debug("REST request to save Milestone : {}", milestone);
		if (milestone.getId() != null) {
			throw new BadRequestAlertException("A new milestone cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Milestone result = milestoneRepository.save(milestone);
		return ResponseEntity.created(new URI("/api/milestones/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /milestones : Updates an existing milestone.
	 *
	 * @param milestone
	 *            the milestone to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         milestone, or with status 400 (Bad Request) if the milestone is not
	 *         valid, or with status 500 (Internal Server Error) if the milestone
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/milestones")
	@Timed
	public ResponseEntity<Milestone> updateMilestone(@Valid @RequestBody Milestone milestone)
			throws URISyntaxException {
		log.debug("REST request to update Milestone : {}", milestone);
		if (milestone.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Milestone result = milestoneRepository.save(milestone);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, milestone.getId().toString())).body(result);
	}

	/**
	 * GET /milestones : get all the milestones.
	 *
	 * @param pageable
	 *            the pagination information
	 * @param eagerload
	 *            flag to eager load entities from relationships (This is applicable
	 *            for many-to-many)
	 * @return the ResponseEntity with status 200 (OK) and the list of milestones in
	 *         body
	 */
	@GetMapping("/milestones")
	@Timed
	public ResponseEntity<List<Milestone>> getAllMilestones(Pageable pageable,
			@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
		log.debug("REST request to get a page of Milestones");
		Page<Milestone> page;
		if (eagerload) {
			page = milestoneRepository.findAllWithEagerRelationships(pageable);
		} else {
			page = milestoneRepository.findAll(pageable);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				String.format("/api/milestones?eagerload=%b", eagerload));
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /milestones/:id : get the "id" milestone.
	 *
	 * @param id
	 *            the id of the milestone to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the milestone,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/milestones/{id}")
	@Timed
	public ResponseEntity<Milestone> getMilestone(@PathVariable Long id) {
		log.debug("REST request to get Milestone : {}", id);
		Optional<Milestone> milestone = milestoneRepository.findOneWithEagerRelationships(id);
		return ResponseUtil.wrapOrNotFound(milestone);
	}

	/**
	 * DELETE /milestones/:id : delete the "id" milestone.
	 *
	 * @param id
	 *            the id of the milestone to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/milestones/{id}")
	@Timed
	public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {
		log.debug("REST request to delete Milestone : {}", id);
		milestoneRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
	
}
