package com.altron.workflowmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altron.workflowmanager.domain.DAC;
import com.altron.workflowmanager.domain.Email;
import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.repository.AttachmentsRepository;
import com.altron.workflowmanager.repository.DACRepository;
import com.altron.workflowmanager.repository.EmailRepository;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.repository.PORepository;

@Service
@Transactional
public class EntityRelationService {

	private final Logger log = LoggerFactory.getLogger(EntityRelationService.class);
	private final DACRepository dACRepository;
	private final MilestoneRepository milestoneRepository;
	private final EmailRepository emailRepository;
	private final AttachmentsRepository attachmentsRepository;
	private final PORepository poRepository;

	@Autowired
	public EntityRelationService(DACRepository dACRepository, MilestoneRepository milestoneRepository,
			EmailRepository emailRepository, AttachmentsRepository attachmentsRepository, PORepository poRepository) {
		this.dACRepository = dACRepository;
		this.milestoneRepository = milestoneRepository;
		this.emailRepository = emailRepository;
		this.attachmentsRepository = attachmentsRepository;
		this.poRepository = poRepository;
	}

	/**
	 * Links a DAC to a Milestone
	 * 
	 * @param dacId
	 * @param milestoneId
	 * @return
	 */
	public Milestone linkDACAndMilestone(long dacId, long milestoneId) {
		log.debug("Linking milestone {} to dac {}", milestoneId, dacId);
		return milestoneRepository.save(milestoneRepository.getOne(milestoneId).addDac(dACRepository.getOne(dacId)));
	}

	/**
	 * Links a DAC to a Email
	 * 
	 * @param dacId
	 * @param emailId
	 */
	public DAC linkDACAndEmail(Long dacId, Long emailId) {
		log.debug("Linking email {} to dac {}", emailId, dacId);
		DAC updatedDac = dACRepository.getOne(dacId);
		updatedDac.setEmail(emailRepository.getOne(emailId));
		return dACRepository.save(updatedDac);
	}

	/**
	 * Links attachment to a email
	 * 
	 * @param attachmentId
	 * @param emailId
	 * @return
	 */
	public Email linkAttachmentToEmail(Long attachmentId, Long emailId) {
		log.debug("Linking attachment {} to email {}", attachmentId, emailId);
		Email updatedEmail = emailRepository.getOne(emailId);
		updatedEmail.addAttachment(attachmentsRepository.getOne(attachmentId));
		return emailRepository.save(updatedEmail);
	}

	/**
	 * Links po and milestone
	 * 
	 * @param milestoneId
	 * @param poId
	 * @return
	 */
	public Milestone linkPoToMilestone(Long poId, Long milestoneId) {
		log.debug("Linking po {} to milestone {}", poId, milestoneId);
		Milestone updatedMilestone = milestoneRepository.getOne(milestoneId);
		updatedMilestone.setPoNumber(poRepository.getOne(poId));
		return milestoneRepository.save(updatedMilestone);
	}

}
