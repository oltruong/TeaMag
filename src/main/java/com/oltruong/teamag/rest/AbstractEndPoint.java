package com.oltruong.teamag.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Olivier Truong
 */
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public abstract class AbstractEndPoint {


    protected Response get(Supplier finder) {
        Object result = finder.get();
        if (result == null) {
            return notFound();
        } else {
            return ok(result);
        }
    }

    protected Response delete(Supplier finder, Consumer deleter) {

        return delete(finder, x -> true, deleter);
    }

    protected Response delete(Supplier finder, Predicate authorizer, Consumer deleter) {
        Object result = finder.get();
        Response response;
        if (result == null) {
            response = notFound();
        } else {
            if (authorizer.test(result)) {
                deleter.accept(result);
                response = noContent();
            } else {
                response = forbidden();
            }
        }
        return response;
    }

    protected Response ok(Object object) {
        return Response.ok(object).build();
    }

    protected Response ok() {
        return Response.ok().build();
    }

    protected Response created() {
        return Response.status(Response.Status.CREATED).build();
    }

    protected Response notAcceptable() {
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    protected Response noContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    protected Response notFound() {
        return buildResponse(Response.Status.NOT_FOUND);
    }

    protected Response forbidden() {
        return buildResponse(Response.Status.FORBIDDEN);
    }

    protected Response badRequest() {
        return buildResponse(Response.Status.BAD_REQUEST);
    }

    private Response buildResponse(Response.Status status) {
        return Response.status(status).build();
    }

}