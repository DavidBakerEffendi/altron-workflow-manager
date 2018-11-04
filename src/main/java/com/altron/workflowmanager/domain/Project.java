package com.altron.workflowmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ispr_number", nullable = false)
    private String isprNumber;

    @NotNull
    @Column(name = "ispr_amount", nullable = false)
    private Integer isprAmount;

    @Column(name = "ispr_description")
    private String isprDescription;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @ManyToOne
    @JsonIgnoreProperties("isprNumbers")
    private Contact contact;

    @OneToMany(mappedBy = "isprNumber")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Milestone> milestones = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("projects")
    private Company cc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsprNumber() {
        return isprNumber;
    }

    public Project isprNumber(String isprNumber) {
        this.isprNumber = isprNumber;
        return this;
    }

    public void setIsprNumber(String isprNumber) {
        this.isprNumber = isprNumber;
    }

    public Integer getIsprAmount() {
        return isprAmount;
    }

    public Project isprAmount(Integer isprAmount) {
        this.isprAmount = isprAmount;
        return this;
    }

    public void setIsprAmount(Integer isprAmount) {
        this.isprAmount = isprAmount;
    }

    public String getIsprDescription() {
        return isprDescription;
    }

    public Project isprDescription(String isprDescription) {
        this.isprDescription = isprDescription;
        return this;
    }

    public void setIsprDescription(String isprDescription) {
        this.isprDescription = isprDescription;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Project startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Contact getContact() {
        return contact;
    }

    public Project contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public Project milestones(Set<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    public Project addMilestone(Milestone milestone) {
        this.milestones.add(milestone);
        milestone.setIsprNumber(this);
        return this;
    }

    public Project removeMilestone(Milestone milestone) {
        this.milestones.remove(milestone);
        milestone.setIsprNumber(null);
        return this;
    }

    public void setMilestones(Set<Milestone> milestones) {
        this.milestones = milestones;
    }

    public Company getCc() {
        return cc;
    }

    public Project cc(Company company) {
        this.cc = company;
        return this;
    }

    public void setCc(Company company) {
        this.cc = company;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", isprNumber='" + getIsprNumber() + "'" +
            ", isprAmount=" + getIsprAmount() +
            ", isprDescription='" + getIsprDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            "}";
    }
}
