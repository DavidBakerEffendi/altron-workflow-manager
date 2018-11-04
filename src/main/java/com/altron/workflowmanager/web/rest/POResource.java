package com.altron.workflowmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.altron.workflowmanager.domain.PO;
import com.altron.workflowmanager.repository.PORepository;
import com.altron.workflowmanager.web.rest.errors.BadRequestAlertException;
import com.altron.workflowmanager.web.rest.util.HeaderUtil;
import com.altron.workflowmanager.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PO.
 */
@RestController
@RequestMapping("/api")
public class POResource {

	private final Logger log = LoggerFactory.getLogger(POResource.class);

	private static final String ENTITY_NAME = "pO";

	private final PORepository pORepository;

	public POResource(PORepository pORepository) {
		this.pORepository = pORepository;
	}

	/**
	 * POST /pos : Create a new pO.
	 *
	 * @param pO
	 *            the pO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         pO, or with status 400 (Bad Request) if the pO has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/pos")
	@Timed
	public ResponseEntity<PO> createPO(@Valid @RequestBody PO pO) throws URISyntaxException {
		log.debug("REST request to save PO : {}", pO);
		if (pO.getId() != null) {
			throw new BadRequestAlertException("A new pO cannot already have an ID", ENTITY_NAME, "idexists");
		}
		PO result = pORepository.save(pO);
		return ResponseEntity.created(new URI("/api/pos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /pos : Updates an existing pO.
	 *
	 * @param pO
	 *            the pO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated pO,
	 *         or with status 400 (Bad Request) if the pO is not valid, or with
	 *         status 500 (Internal Server Error) if the pO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/pos")
	@Timed
	public ResponseEntity<PO> updatePO(@Valid @RequestBody PO pO) throws URISyntaxException {
		log.debug("REST request to update PO : {}", pO);
		if (pO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		PO result = pORepository.save(pO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /pos : get all the pOS.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of pOS in body
	 */
	@GetMapping("/pos")
	@Timed
	public ResponseEntity<List<PO>> getAllPOS(Pageable pageable) {
		log.debug("REST request to get a page of POS");
		Page<PO> page = pORepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /pos/:id : get the "id" pO.
	 *
	 * @param id
	 *            the id of the pO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the pO, or with
	 *         status 404 (Not Found)
	 */
	@GetMapping("/pos/{id}")
	@Timed
	public ResponseEntity<PO> getPO(@PathVariable Long id) {
		log.debug("REST request to get PO : {}", id);
		Optional<PO> pO = pORepository.findById(id);
		return ResponseUtil.wrapOrNotFound(pO);
	}

	/**
	 * DELETE /pos/:id : delete the "id" pO.
	 *
	 * @param id
	 *            the id of the pO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/pos/{id}")
	@Timed
	public ResponseEntity<Void> deletePO(@PathVariable Long id) {
		log.debug("REST request to delete PO : {}", id);

		pORepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
