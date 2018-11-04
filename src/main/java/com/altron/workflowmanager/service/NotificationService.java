package com.altron.workflowmanager.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altron.workflowmanager.domain.DAC;
import com.altron.workflowmanager.domain.Milestone;
import com.altron.workflowmanager.domain.User;
import com.altron.workflowmanager.domain.enumeration.DacStatus;
import com.altron.workflowmanager.domain.enumeration.MilestoneStatus;
import com.altron.workflowmanager.repository.DACRepository;
import com.altron.workflowmanager.repository.MilestoneRepository;
import com.altron.workflowmanager.repository.UserRepository;

/**
 * Service for checking the database for nearby due dates and alerting all
 * parties involved via email
 * <p>
 * 
 * @author ogulcan
 * 
 *         TODO - Read from the DB - implement:
 *         https://spring.io/guides/gs/scheduling-tasks/ - Interact with
 *         MailService class
 */
@Service
@Transactional
public class NotificationService {

	private final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final MailProperties mailProperties;
	private final UserRepository userRepository;
	private final MilestoneRepository milestoneRepository;
	private final DACRepository dacRepository;

	@Autowired
	public NotificationService(MailProperties mailProperties, UserRepository userRepository,
			MilestoneRepository milestoneRepository, DACRepository dacRepository) {
		this.mailProperties = mailProperties;
		this.userRepository = userRepository;
		this.milestoneRepository = milestoneRepository;
		this.dacRepository = dacRepository;
	}

	/**
	 * Checks if a user has any milestones with nearby due dates every 24 hours.
	 */
	@Scheduled(initialDelay = 1000 * 60, fixedRate = 1000 * 60 * 60 * 24)
	public void checkForNearbyDueDates() {
		List<User> users = userRepository.findAll();
		String content = "";

		for (User user : users) {
			log.debug("Checking user " + user.getLogin() + " for nearby deadlines...");
			boolean milestonesDue = false;
			boolean dacsDue = false;
			List<Milestone> milestones = milestoneRepository.findByUserLogin(user.getLogin());
			List<Milestone> dueStones = new LinkedList<Milestone>();
			List<DAC> dacs = dacRepository.findByUserLogin(user.getLogin());
			List<DAC> dueDacs = new LinkedList<DAC>();
			if (milestones != null) {
				/* Parse a list of milestones this user is responsible for */
				for (Milestone mstone : milestones) {
					Instant dueDate = mstone.getDueDate();
					Instant dtime = dueDate.minusSeconds(60 * 60 * 24);
					if (dtime.isBefore(Instant.now()) && Instant.now().isBefore(dueDate)
							&& !mstone.getStatus().equals(MilestoneStatus.COMPLETE)) {
						dueStones.add(mstone);
						milestonesDue = true;
					}
				}
			}
			if (dacs != null) {
				/* Parse a list of dacs connected to milestones this user is responsible for */
				for (DAC dac : dacs) {
					Instant dueDate = dac.getDueDate();
					Instant dtime = dueDate.minusSeconds(60 * 60 * 24);
					if (dtime.isBefore(Instant.now()) && Instant.now().isBefore(dueDate)
							&& !dac.getStatus().equals(DacStatus.INVOICED)) {
						dueDacs.add(dac);
						dacsDue = true;
					}
				}
			}
			/* Creating opening */
			content = "Dear";
			if (user.getFirstName() != null) {
				content += " " + user.getFirstName();
			}
			content += ",";
			/* Check flags for body creation of email */
			if (milestonesDue) {
				log.debug("Milestone(s) for user " + user.getLogin() + " due within 24 hours, creating email...");

				content += "\n\nThe following milestones have deadlines due in less than 24 hours:";
				for (Milestone dueStone : dueStones) {
					content += "\n     * " + dueStone.getName() + " (" + dueStone.getStatus() + ")";
				}
			}
			if (dacsDue) {
				log.debug("DAC(s) for user " + user.getLogin() + " due within 24 hours, creating email...");
				content += "\n\nThe following DACs associated with a milestone assigned to you have deadlines due in less than 24 hours:";
				for (DAC dac : dueDacs) {
					content += "\n     * ID:" + dac.getId() + " with description '" + dac.getDescription() + "' (" + dac.getStatus() + ")";
				}
			}
			/* If any milestones or dacs are due, create and send email to user */
			if (milestonesDue || dacsDue) {
				content += "\n\nKind Regards,\nAltron Workflow Manager";
				sendNotificationEmail(user.getEmail(), "DEADLINE ALERT - Workflow Manager", content, false, false);
				log.debug("Email sent!");
			}
		}
	}

	@Async
	public void sendNotificationEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
				isHtml, to, subject, content);

		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setPort(mailProperties.getPort());
		javaMailSender.setProtocol(mailProperties.getProtocol());
		javaMailSender.setUsername(mailProperties.getUsername());
		javaMailSender.setPassword(mailProperties.getPassword());

		final Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		javaMailSender.setJavaMailProperties(props);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			message.setTo(to);
			message.setFrom(mailProperties.getUsername());
			message.setSubject(subject);
			message.setText(content, isHtml);
			javaMailSender.send(mimeMessage);
			log.debug("Sent email to User '{}'", to);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.warn("Email could not be sent to user '{}'", to, e);
			} else {
				log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
			}
		}
	}

}
