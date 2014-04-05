package fr.oltruong.teamag.entity;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

@Table(name = "TM_TASK")
@NamedQueries({@NamedQuery(name = "findAllTasks", query = "SELECT t from Task t order by t.name, t.project"), @NamedQuery(name = "findTaskByProject", query = "SELECT t from Task t where t.project=:fproject"),
        @NamedQuery(name = "findTaskByName", query = "SELECT t from Task t where (t.name=:fname and t.project=:fproject)")})
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String project = "";

    @ManyToMany
    private List<Member> members = Lists.newArrayListWithCapacity(1);


    @ManyToOne
    @JoinColumn(name = "TASK_PARENT_FK")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_FK")
    private Activity activity;

    @Column(nullable = false)
    private Boolean delegated = Boolean.FALSE;

    private String comment;

    private Double amount;

    @Transient
    private Double total;


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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
        }
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void addTotal(Double value) {
        if (total == null) {
            total = value;
        } else {
            total += value;
        }
    }

    public int compareTo(Task task) {

        return (project + name).compareTo(task.project + task.name);
    }

    @Override
    public boolean equals(Object otherTask) {
        if (!(otherTask instanceof Task)) {
            return false;
        }
        Task member0 = (Task) otherTask;
        return this.id.equals(member0.getId());
    }

    @Override
    public Task clone() {
        Task cloneTask = new Task();
        cloneTask.setId(id);
        cloneTask.setName(name);
        cloneTask.setProject(project);
        return cloneTask;

    }


    public String getDescription() {
        return this.getProject() + "-" + this.getName();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Boolean getDelegated() {
        return delegated;
    }

    public void setDelegated(Boolean delegated) {
        this.delegated = delegated;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
