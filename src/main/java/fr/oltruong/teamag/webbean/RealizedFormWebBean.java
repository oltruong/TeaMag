package fr.oltruong.teamag.webbean;

import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.slf4j.Logger;

public class RealizedFormWebBean {

    @Inject
    private Logger logger;

    private DateTime dayCursor;

    private int weekNumberMonth;

    private DateTime currentMonth;

    private final List<ColumnDayBean> columnsDay = Lists.newArrayListWithExpectedSize(5);

    private List<TaskWeekBean> taskWeeks = Lists.newArrayList();

    private TaskWeekBean selectedTaskWeek;


    public boolean getIsFirstWeek() {
        logger.debug("day cursor" + dayCursor.getDayOfMonth());
        logger.debug("weekNumberMonth" + weekNumberMonth);
        return weekNumberMonth == 1;
    }

    public boolean getIsLastWeek() {
        logger.debug("day cursor" + dayCursor.getDayOfMonth());
        logger.debug("weekNumberMonth" + weekNumberMonth);
        return weekNumberMonth == 5;
        // return CalendarUtils.isLastWeek( dayCursor );
    }

    public void addColumnDay(ColumnDayBean columnDay) {
        columnsDay.add(columnDay);
    }

    public List<ColumnDayBean> getColumnsDay() {
        return columnsDay;
    }

    public int getWeekNumber() {

        return dayCursor.getWeekOfWeekyear();
    }

    public int getYear() {
        return dayCursor.getYear();
    }

    public List<TaskWeekBean> getTaskWeeks() {
        return taskWeeks;
    }

    public void setTaskWeeks(List<TaskWeekBean> taskWeeks) {
        this.taskWeeks = taskWeeks;
    }

    public TaskWeekBean getSelectedTaskWeek() {
        return selectedTaskWeek;
    }

    public void setSelectedTaskWeek(TaskWeekBean selectedTaskWeek) {
        this.selectedTaskWeek = selectedTaskWeek;
    }

    public void incrementWeek() {
        weekNumberMonth += 1;
        dayCursor = dayCursor.plusWeeks(1);

    }

    public void decrementWeek() {
        weekNumberMonth -= 1;
        dayCursor = dayCursor.plusWeeks(-1);
    }

    public DateTime getCurrentMonth() {
        return currentMonth;
    }

    public String getCurrentMonthStr() {
        return currentMonth.toString("MMMMM");
    }

    public void setCurrentMonth(DateTime currentMonth) {
        this.currentMonth = currentMonth;
    }

    public void setDayCursor(DateTime dayCursor) {
        this.dayCursor = dayCursor;
        weekNumberMonth = dayCursor.toCalendar(Locale.getDefault()).get(Calendar.WEEK_OF_MONTH);
    }

    public DateTime getDayCursor() {
        return dayCursor;
    }
}