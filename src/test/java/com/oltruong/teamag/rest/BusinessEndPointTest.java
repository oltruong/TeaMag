package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.Activity;
import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.builder.EntityFactory;
import com.oltruong.teamag.service.ActivityService;
import com.oltruong.teamag.service.BusinessCaseService;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BusinessEndPointTest extends AbstractEndPointTest {


    @Mock
    ActivityService mockActivityService;

    @Mock
    BusinessCaseService mockBusinessCaseService;

    BusinessEndPoint businessEndPoint;


    @Before
    public void prepare() {
        super.setup();
        businessEndPoint = new BusinessEndPoint();
        TestUtils.setPrivateAttribute(businessEndPoint, mockBusinessCaseService, "businessCaseService");
        TestUtils.setPrivateAttribute(businessEndPoint, mockActivityService, "activityService");

    }

    @Test
    public void testGetBC() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(mockBusinessCaseService.findAll()).thenReturn(businessCaseList);
        Response response = businessEndPoint.getBC();

        checkResponseOK(response);

        List<BusinessCase> businessCasesReturned = (List<BusinessCase>) response.getEntity();

        assertThat(businessCasesReturned).isEqualTo(businessCaseList);
        verify(mockBusinessCaseService).findAll();
    }

    @Test
    public void testGetBC_byId() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.find(eq(randomId))).thenReturn(businessCase);

        Response response = businessEndPoint.getBC(randomId);
        checkResponseOK(response);

        BusinessCase businessCaseReturned = (BusinessCase) response.getEntity();

        assertThat(businessCaseReturned).isEqualTo(businessCase);
        verify(mockBusinessCaseService).find(eq(randomId));

    }

    @Test
    public void testGetActivities() throws Exception {
        List<Activity> activityList = EntityFactory.createList(EntityFactory::createActivity);
        when(mockActivityService.findActivities()).thenReturn(activityList);
        Response response = businessEndPoint.getActivities();

        checkResponseOK(response);

        List<Activity> activityListReturned = (List<Activity>) response.getEntity();

        assertThat(activityListReturned).isEqualTo(activityList);
        verify(mockActivityService).findActivities();
    }


    @Test
    public void testGetActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.find(eq(randomId))).thenReturn(activity);

        Response response = businessEndPoint.getActivity(randomId);
        checkResponseOK(response);

        Activity activityReturned = (Activity) response.getEntity();

        assertThat(activityReturned).isEqualTo(activity);
        verify(mockActivityService).find(eq(randomId));
    }

    @Test
    public void testCreateActivity() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.persist(eq(activity))).thenReturn(activity);
        Response response = businessEndPoint.createActivity(activity);

        checkResponseCreated(response);
        verify(mockActivityService).persist(eq(activity));

    }


    @Test
    public void testCreateActivity_existing() throws Exception {
        Activity activity = EntityFactory.createActivity();
        when(mockActivityService.persist(eq(activity))).thenThrow(new EntityExistsException());

        Response response = businessEndPoint.createActivity(activity);

        checkResponseNotAcceptable(response);
        verify(mockActivityService).persist(eq(activity));

    }

    @Test
    public void testUpdateActivity() throws Exception {

        Activity activity = EntityFactory.createActivity();
        assertThat(activity.getId()).isNull();

        Response response = businessEndPoint.updateActivity(randomId, activity);
        checkResponseOK(response);

        assertThat(activity.getId()).isEqualTo(randomId);
        verify(mockActivityService).merge(eq(activity));
    }

    @Test
    public void testDeleteActivity() throws Exception {

        Response response = businessEndPoint.deleteActivity(randomId);
        checkResponseOK(response);


        verify(mockActivityService).remove(eq(randomId));
    }

    @Test
    public void testCreateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.persist(eq(businessCase))).thenReturn(businessCase);
        Response response = businessEndPoint.createBC(businessCase);

        checkResponseCreated(response);
        verify(mockBusinessCaseService).persist(eq(businessCase));

    }


    @Test
    public void testCreateBC_existing() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.persist(eq(businessCase))).thenThrow(new EntityExistsException());
        Response response = businessEndPoint.createBC(businessCase);

        checkResponseNotAcceptable(response);
        verify(mockBusinessCaseService).persist(eq(businessCase));

    }

    @Test
    public void testUpdateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        assertThat(businessCase.getId()).isNull();

        Response response = businessEndPoint.updateBC(randomId, businessCase);
        checkResponseOK(response);

        assertThat(businessCase.getId()).isEqualTo(randomId);
        verify(mockBusinessCaseService).merge(eq(businessCase));
    }

    @Test
    public void testDeleteBC() throws Exception {
        Response response = businessEndPoint.deleteBC(randomId);
        checkResponseOK(response);


        verify(mockBusinessCaseService).remove(eq(randomId));
    }
}