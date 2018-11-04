package com.altron.workflowmanager.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.altron.workflowmanager.repository.CompanyRepository;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.repository.PORepository;
import com.altron.workflowmanager.service.StatisticsService;

/**
 * Test class for the StatisticsResource REST controller.
 *
 * @see StatisticsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkflowManagerApp.class)
public class StatisticsResourceIntTest {

	private MockMvc restMockMvc;
	@Autowired
	private MilestoneRepository milestoneRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private PORepository poRepository;
	@Autowired
	private StatisticsService statisticsService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		StatisticsResource statisticsResource = new StatisticsResource(milestoneRepository, companyRepository,
				poRepository, statisticsService);
		restMockMvc = MockMvcBuilders.standaloneSetup(statisticsResource).build();
	}

	/**
	 * Test dashboardStats
	 */
	@Test
	public void testDashboardStats() throws Exception {
		restMockMvc.perform(get("/api/statistics/dashboard-stats")).andExpect(status().isOk());
	}

}
