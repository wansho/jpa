package top.wansho.jpa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.wansho.jpa.entity.Category;
import top.wansho.jpa.entity.Customer;
import top.wansho.jpa.entity.Item;
import top.wansho.jpa.entity.ManyToOneOrder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.stream.Collectors;

/**
 * @author wanshuo
 * @date 2021-05-30 13:35:01
 */
public class ManyToManyTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    @BeforeEach
    public void init(){
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
        // 开启事务
        entityTransaction.begin();
    }

    @AfterEach
    public void destroy(){
        // 提交事务
//        entityTransaction.rollback();
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    /***
     * 单向多对一的关联关系
     * 保存多对 1 时，建议先保存 1 的一段，再保存 n 的一端，这样不会多出额外的 update
     */
    @Test
    public void testManyToManyPersist(){
        Item item1 = new Item();
        item1.setItemName("item1");
        Item item2 = new Item();
        item2.setItemName("item2");

        Category category1 = new Category();
        category1.setCategoryName("category1");
        Category category2 = new Category();
        category2.setCategoryName("category2");

        // 将维护权交给了 item，所以 item 和 category 的关系必填
        item1.getCategories().add(category1);
        item1.getCategories().add(category2);
        item2.getCategories().add(category1);
        item2.getCategories().add(category2);

        category1.getItems().add(item1); // 引用互相调用导致栈溢出
        category1.getItems().add(item2);
        category2.getItems().add(item2);

        entityManager.persist(category1);
        entityManager.persist(category2);
        entityManager.persist(item1);
        entityManager.persist(item2);
    }

    /***
     * 不管是使用维护关系的一方获取，还是使用不维护关联关系的一方获取，SQL 语句相同
     */
    @Test
    public void testManyToManyFind(){
        Item item = entityManager.find(Item.class, 1);
        item.getCategories().stream().forEach(category -> {
            System.out.println(category);
        });
    }

}
