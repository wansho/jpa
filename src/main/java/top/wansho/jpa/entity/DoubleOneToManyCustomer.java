package top.wansho.jpa.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 双向 1 - n / n - 1关系
 * @author wanshuo
 * @date 2021-05-29 14:06:50
 */
@Data
@ToString
@Entity
@Table(name = "JPA_DOUBLE_ONE_TO_MANY_CUSTOMERS")
public class DoubleOneToManyCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lastName;
    private String email;
    private Integer age;

    /***
     * 双向 1 对 n 的关联关系，双向，注意，双向的列名 customer_id 要保持一致
     * 注意：customer_id 表示在 n 的那张表里生成的外键字段叫 customer_id
     * fetch 设置为饿汉式加载，使用 join 查询
     * cascade 设置成删除 REMOVE 后，其相关的 n 也删除（级联）
     * mappedBy 表示 Customer 放弃维护关联关系，交给 n 的一方的 customer 成员变量来维护关联关系，这样就不会都出额外的 update
     * 定义了 mappedBy 就不用定义 JoinColumn 了
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE}, mappedBy = "customer")
//    @JoinColumn(name = "customer_id")
    private Set<DoubleManyToOneOrder> orders = new HashSet<>();

    public Set<DoubleManyToOneOrder> getOrders(){
        return this.orders;
    }
}
