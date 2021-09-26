package me.escoffier.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Todo extends PanacheEntity  {

    @NotBlank
    public String title;

    public boolean completed;

    @Column(name = "ordering")
    public int order;

    @NotNull
    public String owner;

    public static Uni<List<Todo>> getTodos(String owner) {
        return list("owner", Sort.by("order"), owner);
    }

    public static Uni<Void> clearCompleted(String owner) {
        return delete("owner = ?1 and completed = ?2", owner, true)
                .map(x -> null);
    }

    public static Uni<Todo> getTodo(String owner, Long id) {
        return find("owner = ?1 and id = ?2", owner, id).firstResult();
    }
}
