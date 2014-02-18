package fr.oltruong.teamag.utils;

import com.google.common.collect.Lists;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class CalendarUtilsTest {


    @Test
    public void testConstructorIsPrivate() {
        TestUtils.testConstructorIsPrivate(CalendarUtils.class);
    }

    @Test
    public void testGetListDaysOff() {
        List<DateTime> daysOff = CalendarUtils.getListDaysOff();

        assertThat(daysOff).hasSize(11);

        checkContains(daysOff, 1, 1);
        checkContains(daysOff, 21, 4);
        checkContains(daysOff, 1, 5);
        checkContains(daysOff, 8, 5);
        checkContains(daysOff, 29, 5);
        checkContains(daysOff, 9, 6);
        checkContains(daysOff, 14, 7);
        checkContains(daysOff, 15, 8);
        checkContains(daysOff, 1, 11);
        checkContains(daysOff, 11, 11);
        checkContains(daysOff, 25, 12);

    }

    private void checkContains(List<DateTime> daysOff, int day, int month) {
        DateTime dayOffCheck = DateTime.now().withTimeAtStartOfDay().withMonthOfYear(month).withDayOfMonth(day);
        boolean found = false;
        for (DateTime dayOff : daysOff) {
            if (dayOff.isEqual(dayOffCheck)) {
                found = true;
                break;
            }
        }
        assertThat(found).isTrue();
    }


    @Test
    public void testGetWorkingDaysJanuary() {

        testWorkingDays(1, Lists.newArrayList(1));
        testWorkingDays(2, Lists.newArrayList(0));
        testWorkingDays(4, Lists.newArrayList(21));
        testWorkingDays(5, Lists.newArrayList(1, 8, 29));
        testWorkingDays(6, Lists.newArrayList(9));
        testWorkingDays(7, Lists.newArrayList(14));
        testWorkingDays(8, Lists.newArrayList(15));
        testWorkingDays(11, Lists.newArrayList(1, 11));
        testWorkingDays(12, Lists.newArrayList(25));

    }

    private void testWorkingDays(int monthNumber, Iterable<Integer> values) {
        DateTime month = DateTime.now().withMonthOfYear(monthNumber);
        List<DateTime> workingDays = CalendarUtils.getWorkingDays(month);
        assertThat(workingDays).isNotNull();
        for (DateTime day : workingDays) {
            assertThat(day.getDayOfWeek()).isNotEqualTo(DateTimeConstants.SUNDAY).isNotEqualTo(DateTimeConstants.SATURDAY);
            assertThat(Integer.valueOf(day.getDayOfMonth())).isNotIn(values);

        }
    }


}