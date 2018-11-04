package com.altron.workflowmanager.web.rest;

import com.altron.workflowmanager.WorkflowManagerApp;

import com.altron.workflowmanager.domain.PO;
import com.altron.workflowmanager.repository.PORepository;
import com.altron.workflowmanager.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;


import static com.altron.workflowmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the POResource REST controller.
 *
 * @see POResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkflowManagerApp.class)
public class POResourceIntTest {

    private static final Integer DEFAULT_PO_AMOUNT = 1;
    private static final Integer UPDATED_PO_AMOUNT = 2;

    @Autowired
    private PORepository pORepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPOMockMvc;

    private PO pO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final POResource pOResource = new POResource(pORepository);
        this.restPOMockMvc = MockMvcBuilders.standaloneSetup(pOResource)
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
    public static PO createEntity(EntityManager em) {
        PO pO = new PO()
            .poAmount(DEFAULT_PO_AMOUNT);
        return pO;
    }

    @Before
    public void initTest() {
        pO = createEntity(em);
    }

    @Test
    @Transactional
    public void createPO() throws Exception {
        int databaseSizeBeforeCreate = pORepository.findAll().size();

        // Create the PO
        restPOMockMvc.perform(post("/api/pos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pO)))
            .andExpect(status().isCreated());

        // Validate the PO in the database
        List<PO> pOList = pORepository.findAll();
        assertThat(pOList).hasSize(databaseSizeBeforeCreate + 1);
        PO testPO = pOList.get(pOList.size() - 1);
        assertThat(testPO.getPoAmount()).isEqualTo(DEFAULT_PO_AMOUNT);
    }

    @Test
    @Transactional
    public void createPOWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pORepository.findAll().size();

        // Create the PO with an existing ID
        pO.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPOMockMvc.perform(post("/api/pos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pO)))
            .andExpect(status().isBadRequest());

        // Validate the PO in the database
        List<PO> pOList = pORepository.findAll();
        assertThat(pOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPoAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pORepository.findAll().size();
        // set the field null
        pO.setPoAmount(null);

        // Create the PO, which fails.

        restPOMockMvc.perform(post("/api/pos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pO)))
            .andExpect(status().isBadRequest());

        List<PO> pOList = pORepository.findAll();
        assertThat(pOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPOS() throws Exception {
        // Initialize the database
        pORepository.saveAndFlush(pO);

        // Get all the pOList
        restPOMockMvc.perform(get("/api/pos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pO.getId().intValue())))
            .andExpect(jsonPath("$.[*].poAmount").value(hasItem(DEFAULT_PO_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getPO() throws Exception {
        // Initialize the database
        pORepository.saveAndFlush(pO);

        // Get the pO
        restPOMockMvc.perform(get("/api/pos/{id}", pO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pO.getId().intValue()))
            .andExpect(jsonPath("$.poAmount").value(DEFAULT_PO_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingPO() throws Exception {
        // Get the pO
        restPOMockMvc.perform(get("/api/pos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePO() throws Exception {
        // Initialize the database
        pORepository.saveAndFlush(pO);

        int databaseSizeBeforeUpdate = pORepository.findAll().size();

        // Update the pO
        PO updatedPO = pORepository.findById(pO.getId()).get();
        // Disconnect from session so that the updates on updatedPO are not directly saved in db
        em.detach(updatedPO);
        updatedPO
            .poAmount(UPDATED_PO_AMOUNT);

        restPOMockMvc.perform(put("/api/pos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPO)))
            .andExpect(status().isOk());

        // Validate the PO in the database
        List<PO> pOList = pORepository.findAll();
        assertThat(pOList).hasSize(databaseSizeBeforeUpdate);
        PO testPO = pOList.get(pOList.size() - 1);
        assertThat(testPO.getPoAmount()).isEqualTo(UPDATED_PO_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingPO() throws Exception {
        int databaseSizeBeforeUpdate = pORepository.findAll().size();

        // Create the PO

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPOMockMvc.perform(put("/api/pos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pO)))
            .andExpect(status().isBadRequest());

        // Validate the PO in the database
        List<PO> pOList = pORepository.findAll();
        assertThat(pOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePO() throws Exception {
        // Initialize the database
        pORepository.saveAndFlush(pO);

        int databaseSizeBeforeDelete = pORepository.findAll().size();

        // Get the pO
        restPOMockMvc.perform(delete("/api/pos/{id}", pO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PO> pOList = pORepository.findAll();
        assertThat(pOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PO.class);
        PO pO1 = new PO();
        pO1.setId(1L);
        PO pO2 = new PO();
        pO2.setId(pO1.getId());
        assertThat(pO1).isEqualTo(pO2);
        pO2.setId(2L);
        assertThat(pO1).isNotEqualTo(pO2);
        pO1.setId(null);
        assertThat(pO1).isNotEqualTo(pO2);
    }
}
