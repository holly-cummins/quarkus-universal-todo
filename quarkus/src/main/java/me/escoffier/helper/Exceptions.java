package me.escoffier.helper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Exceptions {

    public static WebApplicationException notFound(Long id) {
        return new WebApplicationException("Todo with id of " + id + " does not exist.", Response.Status.NOT_FOUND);
    }

}
