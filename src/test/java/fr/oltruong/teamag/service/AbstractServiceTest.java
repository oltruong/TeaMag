package fr.oltruong.teamag.service;

import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.utils.TestUtils;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class AbstractServiceTest {

    protected Long idTest = Long.valueOf(123l);

    @Mock
    protected EntityManager mockEntityManager;

    @Mock
    private Logger mockLogger;

    @Mock
    protected Query mockQuery;

    protected Long randomLong;

    @Before
    public void setup() {
        randomLong = EntityFactory.createRandomLong();
        MockitoAnnotations.initMocks(this);
        when(mockEntityManager.createNamedQuery(isA(String.class))).thenReturn(getMockQuery());

    }

    protected void prepareService(AbstractService service) {
        TestUtils.setPrivateAttribute(service, AbstractService.class, mockEntityManager, "entityManager");
        TestUtils.setPrivateAttribute(service, AbstractService.class, getMockLogger(), "logger");

    }

    protected void checkCreateNameQuery(String query) {
        verify(mockEntityManager).createNamedQuery(eq(query));
    }

    protected void checkParameter(String parameter, Object value) {
        verify(mockQuery).setParameter(eq(parameter), eq(value));
    }

    protected Logger getMockLogger() {
        return mockLogger;
    }

    protected Query getMockQuery() {
        return mockQuery;
    }

}
