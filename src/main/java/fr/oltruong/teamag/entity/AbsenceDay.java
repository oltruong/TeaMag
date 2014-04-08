package fr.oltruong.teamag.entity;


import fr.oltruong.teamag.entity.converter.DateConverter;
import org.joda.time.DateTime;

import javax.persistence.*;

@Table(name = "TM_ABSENCE_DAY")
@Entity
@NamedQueries({@NamedQuery(name = "findAbsenceDayByAbsenceId", query = "SELECT a FROM AbsenceDay a where a.absence.id=:fAbsenceId"), @NamedQuery(name = "findAllAbsenceDays", query = "SELECT a FROM AbsenceDay a order by a.week, a.member.name")})
public class AbsenceDay {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer month;

    private Integer week;


    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime day;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_FK")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ABSENCE_FK")
    private Absence absence;


    private Float value = Float.valueOf(1f);

    public AbsenceDay() {

    }

    public AbsenceDay(Absence absenceReference) {
        absence = absenceReference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getWeek() {
        return week;
    }


    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
        this.month = day.getMonthOfYear();
        this.week = day.getWeekOfWeekyear();
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }
}