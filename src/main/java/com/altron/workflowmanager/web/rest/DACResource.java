package com.altron.workflowmanager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.DAC;
import com.altron.workflowmanager.repository.DACRepository;
import com.altron.workflowmanager.repository.EmailRepository;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.service.EntityRelationService;
import com.altron.workflowmanager.web.rest.errors.BadRequestAlertException;
import com.altron.workflowmanager.web.rest.util.HeaderUtil;
import com.altron.workflowmanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing DAC.
 */
@RestController
@RequestMapping("/api")
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class DACResource {

	private final Logger log = LoggerFactory.getLogger(DACResource.class);

	private static final String ENTITY_NAME = "dAC";

	private final DACRepository dACRepository;
	private final EntityRelationService linkService;

	public DACResource(EntityRelationService linkService, DACRepository dACRepository) {
		this.dACRepository = dACRepository;
		this.linkService = linkService;
	}

	/**
	 * POST /dacs : Create a new dAC.
	 *
	 * @param dAC
	 *            the dAC to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         dAC, or with status 400 (Bad Request) if the dAC has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/dacs")
	@Timed
	public ResponseEntity<DAC> createDAC(@Valid @RequestBody DAC dAC) throws URISyntaxException {
		log.debug("REST request to save DAC : {}", dAC);
		if (dAC.getId() != null) {
			throw new BadRequestAlertException("A new dAC cannot already have an ID", ENTITY_NAME, "idexists");
		}
		DAC result = dACRepository.save(dAC);
		return ResponseEntity.created(new URI("/api/dacs/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /dacs : Updates an existing dAC.
	 *
	 * @param dAC
	 *            the dAC to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         dAC, or with status 400 (Bad Request) if the dAC is not valid, or
	 *         with status 500 (Internal Server Error) if the dAC couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/dacs")
	@Timed
	public ResponseEntity<DAC> updateDAC(@Valid @RequestBody DAC dAC) throws URISyntaxException {
		log.debug("REST request to update DAC : {}", dAC);
		if (dAC.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		DAC result = dACRepository.save(dAC);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dAC.getId().toString()))
				.body(result);
	}

	/**
	 * GET /dacs : get all the dACS.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of dACS in body
	 */
	@GetMapping("/dacs")
	@Timed
	public ResponseEntity<List<DAC>> getAllDACS(Pageable pageable) {
		log.debug("REST request to get a page of DACS");
		Page<DAC> page = dACRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dacs");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /dacs/:id : get the "id" dAC.
	 *
	 * @param id
	 *            the id of the dAC to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the dAC, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/dacs/{id}")
	@Timed
	public ResponseEntity<DAC> getDAC(@PathVariable Long id) {
		log.debug("REST request to get DAC : {}", id);
		Optional<DAC> dAC = dACRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(dAC);
	}

	/**
	 * DELETE /dacs/:id : delete the "id" dAC.
	 *
	 * @param id
	 *            the id of the dAC to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/dacs/{id}")
	@Timed
	public ResponseEntity<Void> deleteDAC(@PathVariable Long id) {
		log.debug("REST request to delete DAC : {}", id);

		dACRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * GET /dacs/milestone-:id : obtain the DACs for the given milestone.
	 * 
	 * @param id
	 *            the id of the milestone to find DACs for.
	 * @return the list of DACs for given milestone.
	 */
	@GetMapping("/dacs/milestone-{id}")
	public ResponseEntity<List<DAC>> getMilestoneDACs(@PathVariable Long id) {
		log.debug("REST request to get DACs belonging to milestone with id : {}", id);
		List<DAC> dacs = dACRepository.getMilestoneDACs(id);
		return new ResponseEntity<>(dacs, null, HttpStatus.OK);
	}
	
}
