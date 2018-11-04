package com.altron.workflowmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.altron.workflowmanager.domain.enumeration.DacStatus;

/**
 * A DAC.
 */
@Entity
@Table(name = "dac")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DAC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "dac_amount", nullable = false)
    private Integer dacAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DacStatus status;

    @OneToOne    @JoinColumn(unique = true)
    private Email email;

    @ManyToMany(mappedBy = "dacs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Milestone> milestones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public DAC dueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public DAC description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDacAmount() {
        return dacAmount;
    }

    public DAC dacAmount(Integer dacAmount) {
        this.dacAmount = dacAmount;
        return this;
    }

    public void setDacAmount(Integer dacAmount) {
        this.dacAmount = dacAmount;
    }

    public DacStatus getStatus() {
        return status;
    }

    public DAC status(DacStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DacStatus status) {
        this.status = status;
    }

    public Email getEmail() {
        return email;
    }

    public DAC email(Email email) {
        this.email = email;
        return this;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public DAC milestones(Set<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    public DAC addMilestone(Milestone milestone) {
        this.milestones.add(milestone);
        milestone.getDacs().add(this);
        return this;
    }

    public DAC removeMilestone(Milestone milestone) {
        this.milestones.remove(milestone);
        milestone.getDacs().remove(this);
        return this;
    }

    public void setMilestones(Set<Milestone> milestones) {
        this.milestones = milestones;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DAC dAC = (DAC) o;
        if (dAC.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dAC.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DAC{" +
            "id=" + getId() +
            ", dueDate='" + getDueDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", dacAmount=" + getDacAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
