package top.wansho.jpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 多对多
 * @author wanshuo
 * @date 2021-05-30 20:33:27
 */
@Getter
@Setter
@Entity
@Table(name = "jpa_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String categoryName;

    /***
     * mappedBy = "categories"：把维护权交给 Item 表的 categories 变量
     */
    @ManyToMany(mappedBy = "categories")
    private Set<Item> items = new HashSet<>();

}
