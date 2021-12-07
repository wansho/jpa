package top.wansho.jpa.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wanshuo
 * @date 2021-05-30 13:38:52
 */
@Table(name = "JPA_MANY_TO_ONE_ORDERS")
@Entity
@Data
public class ManyToOneOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderName;

    /***
     * 多个 order 对应一个 customer
     * @JoinColumn 的 name 指定该字段在数据库中的名称
     * @ManyToOne 映射多对一的关联关系，懒加载
     */
    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

}
