package com.oltruong.teamag.rest;

import com.oltruong.teamag.model.BusinessCase;
import com.oltruong.teamag.model.builder.EntityFactory;
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
    BusinessCaseService mockBusinessCaseService;

    BusinessCaseEndPoint businessEndPoint;

    @Before
    public void prepare() {
        super.setup();
        businessEndPoint = new BusinessCaseEndPoint();
        TestUtils.setPrivateAttribute(businessEndPoint, mockBusinessCaseService, "businessCaseService");
        TestUtils.setPrivateAttribute(businessEndPoint, AbstractEndPoint.class, mockLogger, "LOGGER");

    }

    @Test
    public void testGetBC() throws Exception {
        List<BusinessCase> businessCaseList = EntityFactory.createList(EntityFactory::createBusinessCase);
        when(mockBusinessCaseService.findAll()).thenReturn(businessCaseList);
        Response response = businessEndPoint.get();

        checkResponseOK(response);

        List<BusinessCase> businessCasesReturned = (List<BusinessCase>) response.getEntity();

        assertThat(businessCasesReturned).isEqualTo(businessCaseList);
        verify(mockBusinessCaseService).findAll();
    }

    @Test
    public void testGetBC_byId() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.find(eq(randomId))).thenReturn(businessCase);

        Response response = businessEndPoint.get(randomId);
        checkResponseOK(response);

        BusinessCase businessCaseReturned = (BusinessCase) response.getEntity();

        assertThat(businessCaseReturned).isEqualTo(businessCase);
        verify(mockBusinessCaseService).find(eq(randomId));

    }


    @Test
    public void testCreate() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.persist(eq(businessCase))).thenReturn(businessCase);
        Response response = businessEndPoint.create(businessCase);

        checkResponseCreated(response);
        verify(mockBusinessCaseService).persist(eq(businessCase));

    }


    @Test
    public void testCreate_existing() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        when(mockBusinessCaseService.persist(eq(businessCase))).thenThrow(new EntityExistsException());
        Response response = businessEndPoint.create(businessCase);

        checkResponseNotAcceptable(response);
        verify(mockBusinessCaseService).persist(eq(businessCase));

    }

    @Test
    public void testUpdateBC() throws Exception {
        BusinessCase businessCase = EntityFactory.createBusinessCase();
        assertThat(businessCase.getId()).isNull();

        Response response = businessEndPoint.update(randomId, businessCase);
        checkResponseOK(response);

        assertThat(businessCase.getId()).isEqualTo(randomId);
        verify(mockBusinessCaseService).merge(eq(businessCase));
    }

    @Test
    public void testDeleteBC() throws Exception {
        Response response = businessEndPoint.delete(randomId);
        checkResponseOK(response);


        verify(mockBusinessCaseService).remove(eq(randomId));
    }
}