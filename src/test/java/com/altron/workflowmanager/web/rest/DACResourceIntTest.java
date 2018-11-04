package com.altron.workflowmanager.web.rest;

import static com.altron.workflowmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.altron.workflowmanager.WorkflowManagerApp;
import com.altron.workflowmanager.domain.DAC;
import com.altron.workflowmanager.domain.enumeration.DacStatus;
import com.altron.workflowmanager.repository.DACRepository;
import com.altron.workflowmanager.service.EntityRelationService;
import com.altron.workflowmanager.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the DACResource REST controller.
 *
 * @see DACResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkflowManagerApp.class)
public class DACResourceIntTest {

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DAC_AMOUNT = 1;
    private static final Integer UPDATED_DAC_AMOUNT = 2;

    private static final DacStatus DEFAULT_STATUS = DacStatus.CREATED;
    private static final DacStatus UPDATED_STATUS = DacStatus.SENT;

    @Autowired
    private DACRepository dACRepository;
    
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;
    
    @Autowired
    private EntityRelationService relationService;

    @Autowired
    private EntityManager em;

    private MockMvc restDACMockMvc;

    private DAC dAC;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DACResource dACResource = new DACResource(relationService, dACRepository);
        this.restDACMockMvc = MockMvcBuilders.standaloneSetup(dACResource)
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
    public static DAC createEntity(EntityManager em) {
        DAC dAC = new DAC()
            .dueDate(DEFAULT_DUE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .dacAmount(DEFAULT_DAC_AMOUNT)
            .status(DEFAULT_STATUS);
        return dAC;
    }

    @Before
    public void initTest() {
        dAC = createEntity(em);
    }

    @Test
    @Transactional
    public void createDAC() throws Exception {
        int databaseSizeBeforeCreate = dACRepository.findAll().size();

        // Create the DAC
        restDACMockMvc.perform(post("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dAC)))
            .andExpect(status().isCreated());

        // Validate the DAC in the database
        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeCreate + 1);
        DAC testDAC = dACList.get(dACList.size() - 1);
        assertThat(testDAC.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testDAC.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDAC.getDacAmount()).isEqualTo(DEFAULT_DAC_AMOUNT);
        assertThat(testDAC.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createDACWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dACRepository.findAll().size();

        // Create the DAC with an existing ID
        dAC.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDACMockMvc.perform(post("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dAC)))
            .andExpect(status().isBadRequest());

        // Validate the DAC in the database
        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dACRepository.findAll().size();
        // set the field null
        dAC.setDueDate(null);

        // Create the DAC, which fails.

        restDACMockMvc.perform(post("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dAC)))
            .andExpect(status().isBadRequest());

        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDacAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = dACRepository.findAll().size();
        // set the field null
        dAC.setDacAmount(null);

        // Create the DAC, which fails.

        restDACMockMvc.perform(post("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dAC)))
            .andExpect(status().isBadRequest());

        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = dACRepository.findAll().size();
        // set the field null
        dAC.setStatus(null);

        // Create the DAC, which fails.

        restDACMockMvc.perform(post("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dAC)))
            .andExpect(status().isBadRequest());

        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDACS() throws Exception {
        // Initialize the database
        dACRepository.saveAndFlush(dAC);

        // Get all the dACList
        restDACMockMvc.perform(get("/api/dacs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dAC.getId().intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dacAmount").value(hasItem(DEFAULT_DAC_AMOUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getDAC() throws Exception {
        // Initialize the database
        dACRepository.saveAndFlush(dAC);

        // Get the dAC
        restDACMockMvc.perform(get("/api/dacs/{id}", dAC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dAC.getId().intValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dacAmount").value(DEFAULT_DAC_AMOUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDAC() throws Exception {
        // Get the dAC
        restDACMockMvc.perform(get("/api/dacs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDAC() throws Exception {
        // Initialize the database
        dACRepository.saveAndFlush(dAC);

        int databaseSizeBeforeUpdate = dACRepository.findAll().size();

        // Update the dAC
        DAC updatedDAC = dACRepository.findById(dAC.getId()).get();
        // Disconnect from session so that the updates on updatedDAC are not directly saved in db
        em.detach(updatedDAC);
        updatedDAC
            .dueDate(UPDATED_DUE_DATE)
            .description(UPDATED_DESCRIPTION)
            .dacAmount(UPDATED_DAC_AMOUNT)
            .status(UPDATED_STATUS);

        restDACMockMvc.perform(put("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDAC)))
            .andExpect(status().isOk());

        // Validate the DAC in the database
        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeUpdate);
        DAC testDAC = dACList.get(dACList.size() - 1);
        assertThat(testDAC.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testDAC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDAC.getDacAmount()).isEqualTo(UPDATED_DAC_AMOUNT);
        assertThat(testDAC.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingDAC() throws Exception {
        int databaseSizeBeforeUpdate = dACRepository.findAll().size();

        // Create the DAC

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDACMockMvc.perform(put("/api/dacs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dAC)))
            .andExpect(status().isBadRequest());

        // Validate the DAC in the database
        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDAC() throws Exception {
        // Initialize the database
        dACRepository.saveAndFlush(dAC);

        int databaseSizeBeforeDelete = dACRepository.findAll().size();

        // Get the dAC
        restDACMockMvc.perform(delete("/api/dacs/{id}", dAC.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DAC> dACList = dACRepository.findAll();
        assertThat(dACList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DAC.class);
        DAC dAC1 = new DAC();
        dAC1.setId(1L);
        DAC dAC2 = new DAC();
        dAC2.setId(dAC1.getId());
        assertThat(dAC1).isEqualTo(dAC2);
        dAC2.setId(2L);
        assertThat(dAC1).isNotEqualTo(dAC2);
        dAC1.setId(null);
        assertThat(dAC1).isNotEqualTo(dAC2);
    }
}
