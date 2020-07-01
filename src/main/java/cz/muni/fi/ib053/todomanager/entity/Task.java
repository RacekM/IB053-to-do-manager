package cz.muni.fi.ib053.todomanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Exclude
        private Long id;

        @ManyToOne
        @NotNull
        private User owner;

        @NotNull
        private Long estimatedFinishTime;

        @NotNull
        private Long orderIndex;

        @ManyToMany
        @EqualsAndHashCode.Exclude
        private List<Task> prerequisites;

        public Task(User owner, Long estimatedFinishTime, Long orderIndex, List<Task> prerequisites) {
                this.owner = owner;
                this.estimatedFinishTime = estimatedFinishTime;
                this.orderIndex = orderIndex;
                this.prerequisites = prerequisites;
        }

}
