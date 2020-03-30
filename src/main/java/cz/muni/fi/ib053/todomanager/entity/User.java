package cz.muni.fi.ib053.todomanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "Users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String surname;
        @Column(unique = true)
        private String username;
        private String password;

        public User() {
        }

        public User(String name, String surname, String username, String password) {
                this.name = name;
                this.surname = surname;
                this.username = username;
                this.password = password;
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
