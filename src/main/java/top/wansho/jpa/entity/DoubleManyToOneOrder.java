package top.wansho.jpa.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 双向 1 - n / n - 1关系
 * @author wanshuo
 * @date 2021-05-30 13:38:52
 */
@Table(name = "JPA_DOUBLE_MANY_TO_ONE_ORDERS")
@Entity
@Data
public class DoubleManyToOneOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderName;

    /***
     * 多个 order 对应一个 customer，双向，注意，双向的列名 customer_id 要保持一致
     * @JoinColumn 的 name 指定该字段在数据库中的名称
     * @ManyToOne 映射多对一的关联关系，懒加载
     */
    @JoinColumn(name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DoubleOneToManyCustomer customer;

}
