package fr.oltruong.teamag.rest;

import com.google.common.collect.Lists;
import fr.oltruong.teamag.service.WorkService;
import fr.oltruong.teamag.model.Work;
import fr.oltruong.teamag.interfaces.AdminChecked;
import fr.oltruong.teamag.webbean.WorkWebBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Olivier Truong
 */
@Path("work")
@Stateless
@AdminChecked
public class WorkEndPoint extends AbstractEndPoint {

    @Inject
    WorkService workService;

    @GET
    @Path("/{taskId}")
    public Response getWorks(@PathParam("taskId") Long taskId) {

        List<Work> workList = workService.findWorkByTask(taskId);

        List<WorkWebBean> workWebBeanList = transform(workList);
        return buildResponseOK(workWebBeanList);
    }

    private List<WorkWebBean> transform(List<Work> workList) {
        List<WorkWebBean> workWebBeanList = Lists.newArrayListWithExpectedSize(workList.size());

        for (Work work : workList) {
            WorkWebBean workWebBean = new WorkWebBean();
            workWebBean.setAmount(work.getTotal());
            workWebBean.setDay(work.getDay().toDate());
            workWebBean.setMember(work.getMember().getName());
            workWebBean.setTask(work.getTask().getDescription());

            workWebBeanList.add(workWebBean);

        }

        return workWebBeanList;

    }


}
