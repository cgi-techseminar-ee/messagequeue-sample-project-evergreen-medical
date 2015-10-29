package com.cgi.seminar.domain;

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
 * A Questionnaire.
 */
@Entity
@Table(name = "questionnaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Questionnaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Size(min = 3)        
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 2)        
    @Column(name = "questions", nullable = false)
    private String questions;
    
    @Column(name = "external_id")
    private String externalId;

    @OneToMany(mappedBy = "questionnaire")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Workorder> workorders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Set<Workorder> getWorkorders() {
        return workorders;
    }

    public void setWorkorders(Set<Workorder> workorders) {
        this.workorders = workorders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Questionnaire questionnaire = (Questionnaire) o;

        if ( ! Objects.equals(id, questionnaire.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", questions='" + questions + "'" +
                ", externalId='" + externalId + "'" +
                '}';
    }
}
