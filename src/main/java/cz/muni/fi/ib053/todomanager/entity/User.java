package cz.muni.fi.ib053.todomanager.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String surname;
        private String username;
        private String password;

        @OneToMany(mappedBy = "user",
                cascade = CascadeType.ALL,
                fetch = FetchType.EAGER,
                orphanRemoval = true)
        @Fetch(value = FetchMode.SUBSELECT)
        private List<Task> todos = new ArrayList<>();

        public User() {
        }

        public User(String name, String surname, String username, String password) {
                this.name = name;
                this.surname = surname;
                this.username = username;
                this.password = password;
        }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getSurname() {
                return surname;
        }

        public String getUsername() {
                return username;
        }

        public String getPassword() {
                return password;
        }

        public List<Task> getTodos() {
                return todos;
        }


        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                User user = (User) o;
                return Objects.equals(getName(), user.getName()) &&
                        Objects.equals(getSurname(), user.getSurname()) &&
                        Objects.equals(getUsername(), user.getUsername()) &&
                        Objects.equals(getPassword(), user.getPassword());
        }

        @Override
        public int hashCode() {
                return Objects.hash(getName(), getSurname(), getUsername(), getPassword());
        }
}
