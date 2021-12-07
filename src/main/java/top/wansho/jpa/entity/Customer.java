package top.wansho.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author wanshuo
 * @date 2021-05-29 14:06:50
 */
@Data
@ToString
@Entity
@NoArgsConstructor
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lastName;
    private String email;
    private Integer age;

    public Customer(String lastName, Integer age) {
        this.lastName = lastName;
        this.age = age;
    }
}
