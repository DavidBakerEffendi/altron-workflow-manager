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
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "head_name", nullable = false)
    private String headName;

    @NotNull
    @Column(name = "head_email", nullable = false)
    private String headEmail;

    @NotNull
    @Size(min = 12, max = 12)
    @Column(name = "head_number", length = 12, nullable = false)
    private String headNumber;

    @OneToMany(mappedBy = "cc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projects = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Company companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public Company description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadName() {
        return headName;
    }

    public Company headName(String headName) {
        this.headName = headName;
        return this;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadEmail() {
        return headEmail;
    }

    public Company headEmail(String headEmail) {
        this.headEmail = headEmail;
        return this;
    }

    public void setHeadEmail(String headEmail) {
        this.headEmail = headEmail;
    }

    public String getHeadNumber() {
        return headNumber;
    }

    public Company headNumber(String headNumber) {
        this.headNumber = headNumber;
        return this;
    }

    public void setHeadNumber(String headNumber) {
        this.headNumber = headNumber;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Company projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Company addProject(Project project) {
        this.projects.add(project);
        project.setCc(this);
        return this;
    }

    public Company removeProject(Project project) {
        this.projects.remove(project);
        project.setCc(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", description='" + getDescription() + "'" +
            ", headName='" + getHeadName() + "'" +
            ", headEmail='" + getHeadEmail() + "'" +
            ", headNumber='" + getHeadNumber() + "'" +
            "}";
    }
}
