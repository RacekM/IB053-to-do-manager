package cz.muni.fi.ib053.todomanager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
        @JsonBackReference
        private Task parentTask;

        @OneToMany(mappedBy = "parentTask",
                cascade = CascadeType.ALL,
//                fetch = FetchType.EAGER,
                orphanRemoval = true)
        @JsonManagedReference
        private List<Task> prerequisites;

        public Task() {
        }

        public Task getParentTask() {
                return parentTask;
        }

        public void setParentTask(Task parentTask) {
                this.parentTask = parentTask;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public void setEstimatedFinishTime(Long estimatedFinishTime) {
                this.estimatedFinishTime = estimatedFinishTime;
        }

        public void setPrerequisites(List<Task> prerequisites) {
                this.prerequisites = prerequisites;
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
