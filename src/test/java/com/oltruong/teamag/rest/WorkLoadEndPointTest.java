package com.oltruong.teamag.rest;

import com.oltruong.teamag.service.BusinessCaseService;
import com.oltruong.teamag.service.WorkLoadService;
import com.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Olivier Truong
 */
public class WorkLoadEndPointTest extends AbstractEndPointTest {

    private WorkLoadEndPoint workLoadEndPoint;

    @Mock
    private WorkLoadService mockWorkLoadService;

    @Mock
    private BusinessCaseService mockBusinessCaseService;

    @Before
    public void setup() {
        super.setup();
        workLoadEndPoint = new WorkLoadEndPoint();
        TestUtils.setPrivateAttribute(workLoadEndPoint, mockWorkLoadService, "workLoadService");
        TestUtils.setPrivateAttribute(workLoadEndPoint, mockBusinessCaseService, "businessCaseService");
    }

    @Test
    public void testUpdateWorkLoad_null() {
        checkResponseOK(workLoadEndPoint.updateWorkLoad(null));
    }
}