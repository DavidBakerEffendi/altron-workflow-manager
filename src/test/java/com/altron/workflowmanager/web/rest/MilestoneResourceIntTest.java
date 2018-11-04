package com.altron.workflowmanager.web.rest;

import com.altron.workflowmanager.WorkflowManagerApp;

import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.service.EntityRelationService;
import com.altron.workflowmanager.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static com.altron.workflowmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.altron.workflowmanager.domain.enumeration.MilestoneStatus;
/**
 * Test class for the MilestoneResource REST controller.
 *
 * @see MilestoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkflowManagerApp.class)
public class MilestoneResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_PREVIOUS_REVENUE = 1;
    private static final Integer UPDATED_PREVIOUS_REVENUE = 2;

    private static final Integer DEFAULT_PRERECEIPTED_INCOME = 1;
    private static final Integer UPDATED_PRERECEIPTED_INCOME = 2;

    private static final Integer DEFAULT_REVENUE_HOLD_BACK = 1;
    private static final Integer UPDATED_REVENUE_HOLD_BACK = 2;

    private static final Integer DEFAULT_REVENUE_IN_NEXT_FIN_YEAR = 1;
    private static final Integer UPDATED_REVENUE_IN_NEXT_FIN_YEAR = 2;

    private static final MilestoneStatus DEFAULT_STATUS = MilestoneStatus.ACTIVE;
    private static final MilestoneStatus UPDATED_STATUS = MilestoneStatus.WIP;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Mock
    private MilestoneRepository milestoneRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;
    
    @Autowired
    private EntityRelationService linkService;

    private MockMvc restMilestoneMockMvc;

    private Milestone milestone;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MilestoneResource milestoneResource = new MilestoneResource(milestoneRepository, linkService);
        this.restMilestoneMockMvc = MockMvcBuilders.standaloneSetup(milestoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestone createEntity(EntityManager em) {
        Milestone milestone = new Milestone()
            .name(DEFAULT_NAME)
            .dueDate(DEFAULT_DUE_DATE)
            .previousRevenue(DEFAULT_PREVIOUS_REVENUE)
            .prereceiptedIncome(DEFAULT_PRERECEIPTED_INCOME)
            .revenueHoldBack(DEFAULT_REVENUE_HOLD_BACK)
            .revenueInNextFinYear(DEFAULT_REVENUE_IN_NEXT_FIN_YEAR)
            .status(DEFAULT_STATUS);
        return milestone;
    }

    @Before
    public void initTest() {
        milestone = createEntity(em);
    }

    @Test
    @Transactional
    public void createMilestone() throws Exception {
        int databaseSizeBeforeCreate = milestoneRepository.findAll().size();

        // Create the Milestone
        restMilestoneMockMvc.perform(post("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isCreated());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate + 1);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMilestone.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testMilestone.getPreviousRevenue()).isEqualTo(DEFAULT_PREVIOUS_REVENUE);
        assertThat(testMilestone.getPrereceiptedIncome()).isEqualTo(DEFAULT_PRERECEIPTED_INCOME);
        assertThat(testMilestone.getRevenueHoldBack()).isEqualTo(DEFAULT_REVENUE_HOLD_BACK);
        assertThat(testMilestone.getRevenueInNextFinYear()).isEqualTo(DEFAULT_REVENUE_IN_NEXT_FIN_YEAR);
        assertThat(testMilestone.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createMilestoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = milestoneRepository.findAll().size();

        // Create the Milestone with an existing ID
        milestone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilestoneMockMvc.perform(post("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = milestoneRepository.findAll().size();
        // set the field null
        milestone.setName(null);

        // Create the Milestone, which fails.

        restMilestoneMockMvc.perform(post("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = milestoneRepository.findAll().size();
        // set the field null
        milestone.setDueDate(null);

        // Create the Milestone, which fails.

        restMilestoneMockMvc.perform(post("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = milestoneRepository.findAll().size();
        // set the field null
        milestone.setStatus(null);

        // Create the Milestone, which fails.

        restMilestoneMockMvc.perform(post("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMilestones() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList
        restMilestoneMockMvc.perform(get("/api/milestones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].previousRevenue").value(hasItem(DEFAULT_PREVIOUS_REVENUE)))
            .andExpect(jsonPath("$.[*].prereceiptedIncome").value(hasItem(DEFAULT_PRERECEIPTED_INCOME)))
            .andExpect(jsonPath("$.[*].revenueHoldBack").value(hasItem(DEFAULT_REVENUE_HOLD_BACK)))
            .andExpect(jsonPath("$.[*].revenueInNextFinYear").value(hasItem(DEFAULT_REVENUE_IN_NEXT_FIN_YEAR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    public void getAllMilestonesWithEagerRelationshipsIsEnabled() throws Exception {
        MilestoneResource milestoneResource = new MilestoneResource(milestoneRepositoryMock, linkService);
        when(milestoneRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMilestoneMockMvc = MockMvcBuilders.standaloneSetup(milestoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMilestoneMockMvc.perform(get("/api/milestones?eagerload=true"))
        .andExpect(status().isOk());

        verify(milestoneRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllMilestonesWithEagerRelationshipsIsNotEnabled() throws Exception {
        MilestoneResource milestoneResource = new MilestoneResource(milestoneRepositoryMock, linkService);
            when(milestoneRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMilestoneMockMvc = MockMvcBuilders.standaloneSetup(milestoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMilestoneMockMvc.perform(get("/api/milestones?eagerload=true"))
        .andExpect(status().isOk());

            verify(milestoneRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get the milestone
        restMilestoneMockMvc.perform(get("/api/milestones/{id}", milestone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(milestone.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.previousRevenue").value(DEFAULT_PREVIOUS_REVENUE))
            .andExpect(jsonPath("$.prereceiptedIncome").value(DEFAULT_PRERECEIPTED_INCOME))
            .andExpect(jsonPath("$.revenueHoldBack").value(DEFAULT_REVENUE_HOLD_BACK))
            .andExpect(jsonPath("$.revenueInNextFinYear").value(DEFAULT_REVENUE_IN_NEXT_FIN_YEAR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMilestone() throws Exception {
        // Get the milestone
        restMilestoneMockMvc.perform(get("/api/milestones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // Update the milestone
        Milestone updatedMilestone = milestoneRepository.findById(milestone.getId()).get();
        // Disconnect from session so that the updates on updatedMilestone are not directly saved in db
        em.detach(updatedMilestone);
        updatedMilestone
            .name(UPDATED_NAME)
            .dueDate(UPDATED_DUE_DATE)
            .previousRevenue(UPDATED_PREVIOUS_REVENUE)
            .prereceiptedIncome(UPDATED_PRERECEIPTED_INCOME)
            .revenueHoldBack(UPDATED_REVENUE_HOLD_BACK)
            .revenueInNextFinYear(UPDATED_REVENUE_IN_NEXT_FIN_YEAR)
            .status(UPDATED_STATUS);

        restMilestoneMockMvc.perform(put("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMilestone)))
            .andExpect(status().isOk());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMilestone.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testMilestone.getPreviousRevenue()).isEqualTo(UPDATED_PREVIOUS_REVENUE);
        assertThat(testMilestone.getPrereceiptedIncome()).isEqualTo(UPDATED_PRERECEIPTED_INCOME);
        assertThat(testMilestone.getRevenueHoldBack()).isEqualTo(UPDATED_REVENUE_HOLD_BACK);
        assertThat(testMilestone.getRevenueInNextFinYear()).isEqualTo(UPDATED_REVENUE_IN_NEXT_FIN_YEAR);
        assertThat(testMilestone.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // Create the Milestone

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestoneMockMvc.perform(put("/api/milestones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeDelete = milestoneRepository.findAll().size();

        // Get the milestone
        restMilestoneMockMvc.perform(delete("/api/milestones/{id}", milestone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Milestone.class);
        Milestone milestone1 = new Milestone();
        milestone1.setId(1L);
        Milestone milestone2 = new Milestone();
        milestone2.setId(milestone1.getId());
        assertThat(milestone1).isEqualTo(milestone2);
        milestone2.setId(2L);
        assertThat(milestone1).isNotEqualTo(milestone2);
        milestone1.setId(null);
        assertThat(milestone1).isNotEqualTo(milestone2);
    }
}
