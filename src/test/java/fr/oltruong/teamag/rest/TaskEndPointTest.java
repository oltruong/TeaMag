package fr.oltruong.teamag.rest;

import fr.oltruong.teamag.exception.ExistingDataException;
import fr.oltruong.teamag.model.Task;
import fr.oltruong.teamag.model.builder.EntityFactory;
import fr.oltruong.teamag.service.WorkService;
import fr.oltruong.teamag.utils.TestUtils;
import fr.oltruong.teamag.webbean.TaskWebBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskEndPointTest extends AbstractEndPointTest {

    @Mock
    private WorkService mockWorkService;

    private TaskEndPoint taskEndPoint;

    private Task task;

    @Before
    public void prepare() {
        super.setup();

        taskEndPoint = new TaskEndPoint();
        TestUtils.setPrivateAttribute(taskEndPoint, mockWorkService, "workService");
        task = EntityFactory.createTask();


    }


    @Test
    public void testGetTasks() throws Exception {
        testFindTasks(mockWorkService::findAllTasks, taskEndPoint::getTasks);
        verify(mockWorkService, atLeastOnce()).findAllTasks();
    }

    @Test
    public void testGetTasksWithActivity() throws Exception {
        testFindTasks(mockWorkService::findTaskWithActivity, taskEndPoint::getTasksWithActivity);
        verify(mockWorkService, atLeastOnce()).findTaskWithActivity();

    }


    private void testFindTasks(Supplier<List<Task>> taskListSupplier, Supplier<Response> responseSupplier) {
        List<Task> taskList = EntityFactory.createList(EntityFactory::createTask);


        Task parentTask = EntityFactory.createTask();
        parentTask.setId(EntityFactory.createRandomLong());


        taskList.forEach(task -> task.setTask(parentTask));
        when(taskListSupplier.get()).thenReturn(taskList);

        Response response = responseSupplier.get();

        checkResponseOK(response);
        List<TaskWebBean> taskListReturned = (List<TaskWebBean>) response.getEntity();

        assertThat(taskListReturned).hasSameSizeAs(taskList);


    }


    @Test
    public void testGetTask() throws Exception {


        when(mockWorkService.findTask(any())).thenReturn(task);

        Response response = taskEndPoint.getTask(randomId);
        checkResponseOK(response);

        Task taskReturned = (Task) response.getEntity();


        assertThat(taskReturned).isEqualToIgnoringNullFields(task);
        verify(mockWorkService).findTask(eq(randomId));
    }

    @Test
    public void testCreateTask() throws Exception {
        task.setId(randomId);

        Response response = taskEndPoint.createTask(task);

        checkResponseCreated(response);
        verify(mockWorkService).createTask(eq(task));

    }


    @Test
    public void testCreateTask_existing() throws Exception {

        task.setId(randomId);
        doThrow(new ExistingDataException()).when(mockWorkService).createTask(eq(task));

        Response response = taskEndPoint.createTask(task);

        checkResponseNotAcceptable(response);
        verify(mockWorkService).createTask(eq(task));

    }

    @Test
    public void testUpdateTask() throws Exception {
        Task task = Mockito.spy(EntityFactory.createTask());
        assertThat(task.getId()).isNull();

        Response response = taskEndPoint.updateTask(randomId, task);
        checkResponseOK(response);

        assertThat(task.getId()).isEqualTo(randomId);
        verify(mockWorkService).updateTask(eq(task));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Response response = taskEndPoint.deleteTask(randomId);
        checkResponseOK(response);
        verify(mockWorkService).deleteTask(eq(randomId));
    }
}