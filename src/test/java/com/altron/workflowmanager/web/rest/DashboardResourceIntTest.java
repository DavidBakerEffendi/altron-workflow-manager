package com.altron.workflowmanager.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.altron.workflowmanager.WorkflowManagerApp;
import com.altron.workflowmanager.config.ApplicationProperties;
import com.altron.workflowmanager.repository.CompanyRepository;

/**
 * Test class for the DashboardResource REST controller.
 *
 * @see DashboardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkflowManagerApp.class)
public class DashboardResourceIntTest {

	private MockMvc restMockMvc;

	@Autowired
	private CompanyRepository companyRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		DashboardResource dashboardResource = new DashboardResource(companyRepository);
		restMockMvc = MockMvcBuilders.standaloneSetup(dashboardResource).build();
	}

	/**
	 * Test getCompanies
	 * 
	 * All we can really check is if the REST controller responds positively as we
	 * won't be guaranteed of what's in the server
	 */
	@Test
	public void testGetCompanies() throws Exception {
		restMockMvc.perform(get("/api/dashboard/get-companies"))
			.andExpect(status().isOk());
	}
	
}
