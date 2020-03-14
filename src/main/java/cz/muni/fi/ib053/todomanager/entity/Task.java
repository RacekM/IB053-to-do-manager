package cz.muni.fi.ib053.todomanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        //@JoinColumn(name = "user_id", nullable = false)
        @JsonIgnore
        private User user;

        private Long estimatedFinishTime;

        @ManyToOne
        private Task parentTask;

        @OneToMany(mappedBy = "parentTask",
                cascade = CascadeType.ALL,
                fetch = FetchType.EAGER,
                orphanRemoval = true)
        private List<Task> prerequisites;

        public Task() {
        }

        public Task getParentTask() {
                return parentTask;
        }

        public Long getId() {
                return id;
        }

        public User getUser() {
                return user;
        }

        public Long getEstimatedFinishTime() {
                return estimatedFinishTime;
        }

        public List<Task> getPrerequisites() {
                return prerequisites;
        }

        public void setUser(User user) {
                this.user = user;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Task task = (Task) o;
                return Objects.equals(getUser(), task.getUser()) &&
                        Objects.equals(getEstimatedFinishTime(), task.getEstimatedFinishTime());
        }

        @Override
        public int hashCode() {
                return Objects.hash(getUser(), getEstimatedFinishTime());
        }
}
