package me.escoffier;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import me.escoffier.model.Todo;
import org.jboss.resteasy.reactive.RestPath;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("/api")
@RolesAllowed({"user"})
public class TodoResource {

    private final SecurityIdentity securityIdentity;

    public TodoResource(SecurityIdentity securityIdentity) {
        this.securityIdentity = securityIdentity;
    }

    @GET
    public Uni<List<Todo>> getAll() {
        return Todo.getTodos(getCurrentUser());
    }

    @GET
    @Path("/{id}")
    public Uni<Todo> getOne(@RestPath Long id) {
        return Todo.getTodo(getCurrentUser(), id)
                .onItem().ifNull().failWith(
                        () -> new WebApplicationException("Todo with id of " + id + " does not exist.", 404)
                );
    }

    @POST
    public Uni<Response> create(@Valid Todo item) {
        item.owner = getCurrentUser();
        return Panache.withTransaction(() -> Todo.persist(item))
                .replaceWith(Response.ok(item).status(Response.Status.CREATED)::build);
    }

    @PATCH
    @Path("/{id}")
    public Uni<Response> update(@Valid Todo todo, @RestPath Long id) {
        return Panache.withTransaction(() ->
                Todo.getTodo(getCurrentUser(), id)
                        .onItem().ifNull().failWith(() -> new WebApplicationException("Todo with id of " + id + " does not exist.", 404))
                        .invoke(entity -> {
                            entity.id = id;
                            entity.completed = todo.completed;
                            entity.order = todo.order;
                            entity.title = todo.title;
                            entity.owner = getCurrentUser();
                        })
                        .replaceWith(Response.ok().build())
        );
    }

    @DELETE
    public Uni<Response> deleteCompleted() {
        return Todo.clearCompleted(getCurrentUser())
                .replaceWith(Response.noContent().build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteOne(@RestPath Long id) {
        return Panache.withTransaction(() ->
                Todo.<Todo>findById(id)
                        .onItem().ifNull().failWith(() -> new WebApplicationException("Todo with id of " + id + " does not exist.", 404))
                        .chain(PanacheEntityBase::delete)
        ).replaceWith(Response.noContent().build());
    }

    private String getCurrentUser() {
        return securityIdentity.getPrincipal().getName();
    }
}
