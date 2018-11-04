package com.altron.workflowmanager.domain;

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

import com.altron.workflowmanager.domain.enumeration.MilestoneStatus;

/**
 * A Milestone.
 */
@Entity
@Table(name = "milestone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Milestone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Column(name = "previous_revenue")
    private Integer previousRevenue;

    @Column(name = "prereceipted_income")
    private Integer prereceiptedIncome;

    @Column(name = "revenue_hold_back")
    private Integer revenueHoldBack;

    @Column(name = "revenue_in_next_fin_year")
    private Integer revenueInNextFinYear;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MilestoneStatus status;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("milestones")
    private PO poNumber;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "milestone_dac",
               joinColumns = @JoinColumn(name = "milestones_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "dacs_id", referencedColumnName = "id"))
    private Set<DAC> dacs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("milestones")
    private Project isprNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Milestone name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public Milestone dueDate(Instant dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPreviousRevenue() {
        return previousRevenue;
    }

    public Milestone previousRevenue(Integer previousRevenue) {
        this.previousRevenue = previousRevenue;
        return this;
    }

    public void setPreviousRevenue(Integer previousRevenue) {
        this.previousRevenue = previousRevenue;
    }

    public Integer getPrereceiptedIncome() {
        return prereceiptedIncome;
    }

    public Milestone prereceiptedIncome(Integer prereceiptedIncome) {
        this.prereceiptedIncome = prereceiptedIncome;
        return this;
    }

    public void setPrereceiptedIncome(Integer prereceiptedIncome) {
        this.prereceiptedIncome = prereceiptedIncome;
    }

    public Integer getRevenueHoldBack() {
        return revenueHoldBack;
    }

    public Milestone revenueHoldBack(Integer revenueHoldBack) {
        this.revenueHoldBack = revenueHoldBack;
        return this;
    }

    public void setRevenueHoldBack(Integer revenueHoldBack) {
        this.revenueHoldBack = revenueHoldBack;
    }

    public Integer getRevenueInNextFinYear() {
        return revenueInNextFinYear;
    }

    public Milestone revenueInNextFinYear(Integer revenueInNextFinYear) {
        this.revenueInNextFinYear = revenueInNextFinYear;
        return this;
    }

    public void setRevenueInNextFinYear(Integer revenueInNextFinYear) {
        this.revenueInNextFinYear = revenueInNextFinYear;
    }

    public MilestoneStatus getStatus() {
        return status;
    }

    public Milestone status(MilestoneStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(MilestoneStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Milestone user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PO getPoNumber() {
        return poNumber;
    }

    public Milestone poNumber(PO pO) {
        this.poNumber = pO;
        return this;
    }

    public void setPoNumber(PO pO) {
        this.poNumber = pO;
    }

    public Set<DAC> getDacs() {
        return dacs;
    }

    public Milestone dacs(Set<DAC> dACS) {
        this.dacs = dACS;
        return this;
    }

    public Milestone addDac(DAC dAC) {
        this.dacs.add(dAC);
        dAC.getMilestones().add(this);
        return this;
    }

    public Milestone removeDac(DAC dAC) {
        this.dacs.remove(dAC);
        dAC.getMilestones().remove(this);
        return this;
    }

    public void setDacs(Set<DAC> dACS) {
        this.dacs = dACS;
    }

    public Project getIsprNumber() {
        return isprNumber;
    }

    public Milestone isprNumber(Project project) {
        this.isprNumber = project;
        return this;
    }

    public void setIsprNumber(Project project) {
        this.isprNumber = project;
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
        Milestone milestone = (Milestone) o;
        if (milestone.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), milestone.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Milestone{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", previousRevenue=" + getPreviousRevenue() +
            ", prereceiptedIncome=" + getPrereceiptedIncome() +
            ", revenueHoldBack=" + getRevenueHoldBack() +
            ", revenueInNextFinYear=" + getRevenueInNextFinYear() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
