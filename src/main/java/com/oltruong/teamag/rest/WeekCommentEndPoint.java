package com.oltruong.teamag.rest;

import com.oltruong.teamag.interfaces.SecurityChecked;
import com.oltruong.teamag.model.WeekComment;
import com.oltruong.teamag.service.WeekCommentService;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * @author oltruong
 */
@Path("weekComment")
@SecurityChecked
@Stateless
public class WeekCommentEndPoint extends AbstractEndPoint {

    @Inject
    private WeekCommentService weekCommentService;


    @GET
    public Response getWeekComment(@QueryParam("memberId") Long memberId, @QueryParam("weekNumber") int weekNumber) {

        WeekComment weekComment = weekCommentService.findWeekComment(memberId, weekNumber, DateTime.now().getYear());
        if (weekComment == null) {
            return notFound();
        }
        return ok(weekComment);
    }


}
