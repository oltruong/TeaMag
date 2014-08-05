package fr.oltruong.teamag.model;

import fr.oltruong.teamag.model.converter.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "TM_WORK")
@Entity
@NamedQueries({@NamedQuery(name = "findWorksByMemberMonth", query = "SELECT w FROM Work w WHERE w.member.id=:fmemberId and w.month=:fmonth order by w.task.name, w.day"),
        @NamedQuery(name = "findWorksByMemberMonthNotNull", query = "SELECT w FROM Work w WHERE w.member.id=:fmemberId and w.total<>0 and w.month=:fmonth order by w.task.name, w.day"),
        @NamedQuery(name = "deleteWorksByMemberTaskMonth", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId and w.task.id=:ftaskId and w.month=:fmonth"),
        @NamedQuery(name = "deleteWorksByMember", query = "DELETE FROM Work w WHERE w.member.id=:fmemberId"),
        @NamedQuery(name = "findWorksMonth", query = "SELECT w FROM Work w WHERE (w.month=:fmonth AND w.total<>0 ) ORDER by w.member.name, w.member.company, w.task.project, w.task.name"),
        @NamedQuery(name = "findWorksByTask", query = "SELECT w FROM Work w WHERE (w.total<>0 and w.task.id=:fTaskId) ORDER by w.member.name,w.day"),
        @NamedQuery(name = "countWorksTask", query = "SELECT count(w) FROM Work w WHERE w.task.id=:fTaskId"),
        @NamedQuery(name = "countWorksMemberMonth", query = "SELECT SUM(w.total) FROM Work w WHERE (w.month=:fmonth AND w.member.id=:fmemberId )")})
public class Work {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    //  @Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime month;

    @Column(nullable = false)
    //@Temporal(TemporalType.DATE)
    @Convert(converter = DateConverter.class)
    private DateTime day;

    @ManyToOne
    @JoinColumn(nullable = false, name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false, name = "TASK_ID")
    private Task task;

    private Double total = 0d;

    @Transient
    private Double totalEdit = null;

    @Transient
    @Inject
    private Logger logger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getMonth() {
        return month;
    }

    public void setMonth(DateTime month) {
        this.month = month;
    }

    public DateTime getDay() {
        return day;
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
        totalEdit = total;
    }

    public Double getTotalEdit() {
        if (totalEdit == null) {
            totalEdit = total;
        }
        return totalEdit;
    }

    public String getTotalEditStr() {
        Double value = getTotalEdit();
        if (value.floatValue() == 0f) {
            return "";
        }
        return value.toString();
    }

    public void setTotalEditStr(String totalEditStr) {
        if (!StringUtils.isBlank(totalEditStr)) {
            String totalEditFormatted = totalEditStr.replace(",", ".");

            try {
                totalEdit = Double.valueOf(totalEditFormatted);
            } catch (NumberFormatException ex) {
                logger.error("Incorrect value " + totalEditStr);
            }
        } else {   // Blank means 0
            totalEdit = 0d;
        }
    }

    public void setTotalEdit(Double totalEdit) {

        this.totalEdit = totalEdit;
    }

    public String getDayStr() {
        return getDay().toString("E mmm dd");
    }

    public boolean hasChanged() {
        return total.floatValue() != totalEdit.floatValue();
    }


}