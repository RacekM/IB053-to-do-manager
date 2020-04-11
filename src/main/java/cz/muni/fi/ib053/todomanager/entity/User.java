package cz.muni.fi.ib053.todomanager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "Users")
@Data
@NoArgsConstructor
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Exclude
        private Long id;

        private String name;

        private String surname;

        @Column(unique = true)
        private String username;

        @ToString.Exclude
        private String password;

        public User(String name, String surname, String username, String password) {
                this.name = name;
                this.surname = surname;
                this.username = username;
                this.password = password;
        }

}
