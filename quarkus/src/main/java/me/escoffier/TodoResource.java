package me.escoffier;

import io.quarkus.security.identity.SecurityIdentity;
import me.escoffier.model.Todo;
import org.jboss.resteasy.reactive.RestPath;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static me.escoffier.helper.Exceptions.notFound;

@Path("/api")
@RolesAllowed({"user"})
public class TodoResource {

    final SecurityIdentity securityIdentity;

    public TodoResource(SecurityIdentity identity) {
        this.securityIdentity = identity;
    }

    private String getCurrentUser() {
        return securityIdentity.getPrincipal().getName();
    }

    @GET
    public List<Todo> getAll() {
        return Todo.getTodos(getCurrentUser());
    }

    @GET
    @Path("/{id}")
    public Todo getOne(@RestPath Long id) {
        return Todo.getTodo(getCurrentUser(), id)
                .orElseThrow(() -> notFound(id));
    }

    @POST
    @Transactional
    public Response create(@Valid Todo item) throws URISyntaxException {
        item.owner = getCurrentUser();
        Todo.persist(item);
        return Response.created(new URI("/api/" + item.id)).entity(item).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    public Response update(@Valid Todo todo, @RestPath Long id) {
        Todo entity = Todo.getTodo(getCurrentUser(), id)
                .orElseThrow(() -> notFound(id));
        entity.id = id;
        entity.completed = todo.completed;
        entity.order = todo.order;
        entity.title = todo.title;
        entity.owner = getCurrentUser();
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    public Response deleteCompleted() {
        Todo.clearCompleted(getCurrentUser());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteOne(@RestPath Long id) {
        Todo entity = Todo.<Todo>findByIdOptional(id)
                .orElseThrow(() -> notFound(id));
        entity.delete();
        return Response.noContent().build();
    }

}
