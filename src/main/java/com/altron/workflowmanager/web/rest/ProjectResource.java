package com.altron.workflowmanager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;

import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.domain.Project;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.repository.ProjectRepository;
import com.altron.workflowmanager.web.rest.errors.BadRequestAlertException;
import com.altron.workflowmanager.web.rest.util.HeaderUtil;
import com.altron.workflowmanager.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

	private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

	private static final String ENTITY_NAME = "project";

	private final ProjectRepository projectRepository;
	private final MilestoneRepository milestoneRepository;

	@Autowired
	public ProjectResource(ProjectRepository projectRepository, MilestoneRepository milestoneRepository) {
		this.projectRepository = projectRepository;
		this.milestoneRepository = milestoneRepository;
	}

	/**
	 * POST /projects : Create a new project.
	 *
	 * @param project
	 *            the project to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         project, or with status 400 (Bad Request) if the project has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/projects")
	@Timed
	public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
		log.debug("REST request to save Project : {}", project);
		if (project.getId() != null) {
			throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Project result = projectRepository.save(project);
		return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /projects : Updates an existing project.
	 *
	 * @param project
	 *            the project to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         project, or with status 400 (Bad Request) if the project is not
	 *         valid, or with status 500 (Internal Server Error) if the project
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/projects")
	@Timed
	public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
		log.debug("REST request to update Project : {}", project);
		if (project.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Project result = projectRepository.save(project);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString()))
				.body(result);
	}

	/**
	 * GET /projects : get all the projects.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of projects in
	 *         body
	 */
	@GetMapping("/projects")
	@Timed
	public ResponseEntity<List<Project>> getAllProjects(Pageable pageable) {
		log.debug("REST request to get a page of Projects");
		Page<Project> page = projectRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /projects/:id : get the "id" project.
	 *
	 * @param id
	 *            the id of the project to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the project, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/projects/{id}")
	@Timed
	public ResponseEntity<Project> getProject(@PathVariable Long id) {
		log.debug("REST request to get Project : {}", id);
		Optional<Project> project = projectRepository.findById(id);
		return ResponseUtil.wrapOrNotFound(project);
	}

	/**
	 * DELETE /projects/:id : delete the "id" project.
	 *
	 * @param id
	 *            the id of the project to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/projects/{id}")
	@Timed
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		log.debug("REST request to delete Project : {}", id);

		projectRepository.deleteById(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * GET /project/get-:name-projects : get projects by company name
	 *
	 * @param name
	 *            company name
	 * @return company entity
	 */
	@GetMapping("/project/get-{name}-projects")
	public ResponseEntity<List<Project>> getProjectsByCompanyName(@PathVariable String name) {
		log.debug("REST request to get projects for company : '{}'", name);
		return new ResponseEntity<>(projectRepository.getProjectsByCompanyName(name), null, HttpStatus.OK);
	}

	/**
	 * GET /project/get-:name-projects : get projects by ISPR number
	 *
	 * @param isprNo
	 *            ISPR number
	 * @return project entity
	 */
	@GetMapping("/project/get-{isprNo}")
	public ResponseEntity<Project> getProjectsByIsprNumber(@PathVariable String isprNo) {
		log.debug("REST request to get project : '{}'", isprNo);
		Project project = projectRepository.getProjectByIsprNumber(isprNo);
		project.setMilestones(milestoneRepository.getMilestonesByProjectId(projectRepository.getProjectIdByIsprNumber(isprNo)));
		return new ResponseEntity<>(project, null, HttpStatus.OK);
	}

	@GetMapping("/project/get-{comp}-project-{name}")
	public ResponseEntity<Project> getProjectByName(@PathVariable("comp") String comp,
			@PathVariable("name") String name) {
		return new ResponseEntity<>(projectRepository.getProjectByName(comp, name), null, HttpStatus.OK);
	}

	@GetMapping("/project/get-proj-{id}-invoiced-income")
	public ResponseEntity<Long> getProjectInvoicedIncome(@PathVariable Long id) {
		log.debug("REST request to get total invoiced amount for project : '{}'", id);
		return new ResponseEntity<>(projectRepository.getProjectInvoicedIncome(id), null, HttpStatus.OK);
	}

	@GetMapping("/project/get-project-{id}-projected-income")
	public ResponseEntity<Long> getProjectProjectedIncome(@PathVariable Long id) {
		log.debug("REST request to get total invoiced amount for project : '{}'", id);
		return new ResponseEntity<>(projectRepository.getProjectProjectedIncome(id), null, HttpStatus.OK);
	}

	@GetMapping("/project/get-project-{id}-dacs")
	public ResponseEntity<Long> getProjectProjectedDacs(@PathVariable Long id) {
		log.debug("REST request to get total invoiced amount for project : '{}'", id);
		return new ResponseEntity<>(projectRepository.getProjectProjectedDacs(id), null, HttpStatus.OK);
	}

	@GetMapping("/project/get-project-{id}-pos")
	public ResponseEntity<Long> getProjectNumPo(@PathVariable Long id) {
		log.debug("REST request to get total invoiced amount for project : '{}'", id);
		return new ResponseEntity<>(projectRepository.getProjectNumPo(id), null, HttpStatus.OK);
	}

	@GetMapping("/project/get-{id}-milestones")
	public ResponseEntity<Set<Milestone>> getCompanyById(@PathVariable Long id) {
		log.debug("REST request to get milestones for project : '{}'", id);
		return new ResponseEntity<>(milestoneRepository.getMilestonesByProjectId(id), null, HttpStatus.OK);
	}
}
