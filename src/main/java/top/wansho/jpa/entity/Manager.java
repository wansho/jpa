package top.wansho.jpa.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * OneToOne 测试，一个经理只管一个部门
 * @author wanshuo
 * @date 2021-05-30 18:30:36
 */
@Entity
@Data
@Table(name = "jpa_manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(mappedBy = "manager")
    private Department department;


}
