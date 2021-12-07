package top.wansho.jpa.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wanshuo
 * @date 2021-05-30 13:38:52
 */
@Table(name = "JPA_ORDERS")
@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderName;
}
