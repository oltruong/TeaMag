package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.WorkService;
import com.oltruong.teamag.utils.CalendarUtils;
import com.oltruong.teamag.utils.TestUtils;
import com.oltruong.teamag.webbean.WorkWebBean;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.model.Work;
import com.oltruong.teamag.service.WeekCommentService;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckWorkEndPointTest extends AbstractEndPointTest {

    @Mock
    private WorkService mockWorkService;


    private CheckWorkEndPoint checkWorkEndPoint;

    private int currentWeekNumber;

    @Before
    public void prepare() {

        super.setup();
        checkWorkEndPoint = new CheckWorkEndPoint();

        TestUtils.setPrivateAttribute(checkWorkEndPoint, mockWorkService, "workService");
        currentWeekNumber = CalendarUtils.getCurrentWeekNumber();
    }


    @Test
    public void testGetWeekInformation() {
        int weekNumber = 33;
        List<Work> workList = EntityFactory.createList(EntityFactory::createWork);
        workList.forEach(work -> {
            work.getTask().setTask(EntityFactory.createTask());
            work.getTask().setActivity(EntityFactory.createActivity());
        });
        List<Work> newWorkList = Lists.newArrayList(workList);
        newWorkList.addAll(workList);

        when(mockWorkService.findWorksNotNullByWeek(eq(randomId), anyInt())).thenReturn(newWorkList);

        Response response = checkWorkEndPoint.getWeekInformation(randomId, weekNumber, true);

        checkResponseOK(response);

        List<WorkWebBean> workWebBeanList = (List<WorkWebBean>) response.getEntity();
        assertThat(workWebBeanList).hasSameSizeAs(workList);
    }

}