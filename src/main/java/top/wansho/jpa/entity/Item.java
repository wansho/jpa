package top.wansho.jpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wanshuo
 * @date 2021-05-30 20:33:18
 */
@Entity
@Getter
@Setter
@Table(name = "jpa_item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String itemName;

    /***
     * JoinTable: 中间表
     *
     * name: 中间表的名字
     *
     * joinColumns: 该表的外键在中间表中列的属性
     * @JoinColumn(name = "item_id", referencedColumnName = "id") ：
     *      name: 中间表中代表该表主键作为外键时的 name
     *      referencedColumnName: 该表中的哪一个成员变量作为外键
     * inverseJoinColumns: 关联的这个对象，在中间表中的外键的属性
     * @JoinColumn(name = "category_id", referencedColumnName = "id")
     *      name: 关联的对象在中间表中的外键的 name
     *      referencedColumnName: 关联的对象的中的哪一个成员变量作为外键
     *
     * Hibernate:
     *
     *     create table jpa_item_jpa_category (
     *        item_id integer not null,
     *         category_id integer not null,
     *         primary key (item_id, category_id)
     *     ) engine=InnoDB
     * Hibernate:
     *
     *     alter table jpa_item_jpa_category
     *        add constraint FKedbrfsg4dplyysrucevm258pk
     *        foreign key (category_id)
     *        references jpa_category (id)
     * Hibernate:
     *
     *     alter table jpa_item_jpa_category
     *        add constraint FK7vk3csx9i2cq3etibrkouumid
     *        foreign key (item_id)
     *        references jpa_item (id)
     */
    @JoinTable(name = "jpa_item_category",
            joinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Category> categories = new HashSet<>();

}
