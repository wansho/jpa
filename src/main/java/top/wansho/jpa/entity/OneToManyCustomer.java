package top.wansho.jpa.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wanshuo
 * @date 2021-05-29 14:06:50
 */
@Data
@ToString
@Entity
@Table(name = "JPA_ONE_TO_MANY_CUSTOMERS")
public class OneToManyCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lastName;
    private String email;
    private Integer age;

    /***
     * 单向 1 对 n 的关联关系
     * 注意：customer_id 表示在 n 的那张表里生成的外键字段叫 customer_id
     * fetch 设置为饿汉式加载，使用 join 查询
     * cascade 设置成删除 1 后，其相关的 n 也删除（级联）
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "customer_id")
    private Set<Order> orders = new HashSet<>();
}
