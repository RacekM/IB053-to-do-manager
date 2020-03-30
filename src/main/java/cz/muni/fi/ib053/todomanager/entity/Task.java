package cz.muni.fi.ib053.todomanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User owner;

    @NotNull
    private Long estimatedFinishTime;

    @NotNull
    private Long orderIndex;

    @ManyToMany
    private List<Task> prerequisites;

    public Task() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(getOwner(), task.getOwner()) &&
                Objects.equals(getEstimatedFinishTime(), task.getEstimatedFinishTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getEstimatedFinishTime());
    }
}
