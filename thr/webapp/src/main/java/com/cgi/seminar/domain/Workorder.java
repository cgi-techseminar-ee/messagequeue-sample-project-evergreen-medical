package com.cgi.seminar.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.cgi.seminar.domain.util.CustomDateTimeDeserializer;
import com.cgi.seminar.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Workorder.
 */
@Entity
@Table(name = "workorder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Workorder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "planned_start", nullable = false)
    private DateTime plannedStart;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "planned_end", nullable = false)
    private DateTime plannedEnd;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "actual_start", nullable = false)
    private DateTime actualStart;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "actual_end", nullable = false)
    private DateTime actualEnd;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Questionnaire questionnaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(DateTime plannedStart) {
        this.plannedStart = plannedStart;
    }

    public DateTime getPlannedEnd() {
        return plannedEnd;
    }

    public void setPlannedEnd(DateTime plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public DateTime getActualStart() {
        return actualStart;
    }

    public void setActualStart(DateTime actualStart) {
        this.actualStart = actualStart;
    }

    public DateTime getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(DateTime actualEnd) {
        this.actualEnd = actualEnd;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Workorder workorder = (Workorder) o;

        if ( ! Objects.equals(id, workorder.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Workorder{" +
                "id=" + id +
                ", plannedStart='" + plannedStart + "'" +
                ", plannedEnd='" + plannedEnd + "'" +
                ", actualStart='" + actualStart + "'" +
                ", actualEnd='" + actualEnd + "'" +
                '}';
    }
}
