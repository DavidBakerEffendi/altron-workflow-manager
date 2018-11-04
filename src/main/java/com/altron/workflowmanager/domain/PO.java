package com.altron.workflowmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PO.
 */
@Entity
@Table(name = "po")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "po_amount", nullable = false)
    private Integer poAmount;

    @OneToMany(mappedBy = "poNumber")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Milestone> milestones = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoAmount() {
        return poAmount;
    }

    public PO poAmount(Integer poAmount) {
        this.poAmount = poAmount;
        return this;
    }

    public void setPoAmount(Integer poAmount) {
        this.poAmount = poAmount;
    }

    public Set<Milestone> getMilestones() {
        return milestones;
    }

    public PO milestones(Set<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    public PO addMilestone(Milestone milestone) {
        this.milestones.add(milestone);
        milestone.setPoNumber(this);
        return this;
    }

    public PO removeMilestone(Milestone milestone) {
        this.milestones.remove(milestone);
        milestone.setPoNumber(null);
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
        PO pO = (PO) o;
        if (pO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PO{" +
            "id=" + getId() +
            ", poAmount=" + getPoAmount() +
            "}";
    }
}
