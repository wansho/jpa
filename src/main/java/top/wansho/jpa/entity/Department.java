package top.wansho.jpa.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * OneToOne 测试，一个部门只有一位经理
 * @author wanshuo
 * @date 2021-05-30 18:30:50
 */
@Data
@Entity
@Table(name = "jpa_department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /***
     * 使用 OneToOne 来映射 1-1 关系
     * 添加外键，1-1 的关联关系，需要添加 `unique = true`
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager", unique = true)
    private Manager manager;

}
