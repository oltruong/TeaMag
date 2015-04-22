package com.oltruong.teamag.backingbean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oltruong.teamag.service.MemberService;
import com.oltruong.teamag.webbean.TaskWeekBean;
import com.oltruong.teamag.interfaces.UserLogin;
import com.oltruong.teamag.model.Member;
import com.oltruong.teamag.model.Task;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.service.WeekCommentService;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.webbean.ColumnDayBean;
import com.oltruong.teamag.webbean.RealizedFormWebBean;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SessionScoped
@ManagedBean
public class CheckWorkController extends Controller {

    @Inject
    private Logger logger;

    private Map<Task, List<Work>> works;


    @Inject
    private RealizedFormWebBean realizedBean;

    @Inject
    private WorkService workService;

    @Inject
    private WeekCommentService weekCommentService;

    @Inject
    private MemberService memberEJB;

    private WeekComment weekComment;

    private List<Member> memberList;


    @Inject
    @UserLogin
    private Instance<Member> memberInstance;

    private Member memberToCheck;

    private static final String VIEWNAME = "checkrealized";

    public String init() {
        memberList = memberEJB.findActiveNonAdminMembers();
        memberList.add(memberInstance.get());

        memberToCheck = memberList.get(memberList.size() - 1);
        realizedBean.setDayCursor(DateTime.now());
        return refresh();
    }

    public String refresh() {
        memberToCheck = memberEJB.findMember(memberToCheck.getId());

        DateTime firstDayOfMonth = realizedBean.getDayCursor().withDayOfMonth(1);
        realizedBean.setCurrentMonth(firstDayOfMonth);
        works = workService.findWorksNotNullByMonth(memberToCheck, firstDayOfMonth);

        initTaskWeek();
        return VIEWNAME;
    }


    public String previousWeek() {
        logger.debug("Click Previous week");
        realizedBean.decrementWeek();
        return refresh();
    }

    public String nextWeek() {
        realizedBean.incrementWeek();
        return refresh();
    }


    private void initTaskWeek() {
        weekComment = weekCommentService.findWeekComment(memberToCheck.getId(), realizedBean.getWeekNumber(), realizedBean.getYear());

        if (works != null) {
            Integer weekNumber = realizedBean.getWeekNumber();

            Map<String, ColumnDayBean> mapColumns = Maps.newHashMapWithExpectedSize(5);

            List<TaskWeekBean> taskWeekList = Lists.newArrayListWithExpectedSize(works.keySet().size());
            for (Task task : works.keySet()) {
                TaskWeekBean taskWeek = new TaskWeekBean();
                taskWeek.setTask(task);
                boolean emptyWork = true;
                for (Work work : works.get(task)) {

                    if (work.getDay().getWeekOfWeekyear() == weekNumber) {

                        ColumnDayBean columnDay = new ColumnDayBean();
                        columnDay.setDay(work.getDay());
                        taskWeek.addWork(columnDay.getDayNumber(), work);

                        if (mapColumns.get(work.getDayStr()) == null) {
                            columnDay.addTotal(work.getTotal());
                            mapColumns.put(work.getDayStr(), columnDay);
                        } else {
                            mapColumns.get(work.getDayStr()).addTotal(work.getTotal());
                        }
                        emptyWork &= Double.valueOf(0d).equals(work.getTotal());

                    }


                }
                if (!emptyWork) {
                    taskWeekList.add(taskWeek);
                }
            }

            realizedBean.getColumnsDay().clear();
            for (ColumnDayBean col : mapColumns.values()) {
                realizedBean.addColumnDay(col);

            }
            Collections.sort(realizedBean.getColumnsDay());
            realizedBean.setTaskWeeks(taskWeekList);
            Collections.sort(realizedBean.getTaskWeeks());

        } else {
            logger.debug("No taskMonth found");
        }

    }

    public List<Task> getTaskList() {
        Set<Task> taskSet = works.keySet();
        List<Task> taskList = Lists.newArrayListWithCapacity(taskSet.size());
        for (Task task : taskSet) {
            if (!Float.valueOf(0f).equals(task.getTotal())) {
                taskList.add(task);
            }
        }
        return taskList;
    }


    public RealizedFormWebBean getRealizedBean() {
        return realizedBean;
    }

    public void setRealizedBean(RealizedFormWebBean realizedBean) {
        this.realizedBean = realizedBean;
    }


    public WeekComment getWeekComment() {
        if (weekComment == null) {
            weekComment = new WeekComment();
        }
        return weekComment;
    }

    public void setWeekComment(WeekComment weekComment) {
        this.weekComment = weekComment;
    }

    public Member getMemberToCheck() {
        return memberToCheck;
    }

    public void setMemberToCheck(Member memberToCheck) {
        this.memberToCheck = memberToCheck;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

}