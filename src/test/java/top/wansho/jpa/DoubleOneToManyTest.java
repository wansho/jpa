package top.wansho.jpa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.wansho.jpa.entity.DoubleOneToManyCustomer;
import top.wansho.jpa.entity.DoubleManyToOneOrder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author wanshuo
 * @date 2021-05-30 13:35:01
 */
public class DoubleOneToManyTest {

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
     * 双向 1 - n 的关联关系，执行保存时
     * 如果先保存 n 的一端，再保存 1 的一端，会多出 n 条 update 语句
     * 所以在进行双向 1-n 关联关系时，建议使用 n 的一方来维护关联关系，而 1 的一方不维护关联关系，这样可以有效减少 sql 语句
     * 1 的一方放弃关联关系： mappedBy = "" 这样就不会都出额外的 update
     * 定义了 mappedBy 就不用定义 JoinColumn 了
     */
    @Test
    public void testDoubleOneToManyPersist(){
        DoubleOneToManyCustomer customer = new DoubleOneToManyCustomer();
        customer.setEmail("wdx@gmail.com");
        customer.setAge(18);
        customer.setLastName("wdx");

        DoubleManyToOneOrder order1 = new DoubleManyToOneOrder();
        order1.setOrderName("wdx-order-1");
        order1.setCustomer(customer);

        DoubleManyToOneOrder order2 = new DoubleManyToOneOrder();
        order2.setOrderName("wdx-order-2");
        order2.setCustomer(customer);

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(customer);
    }

    /***
     * 默认对关联多的一方使用懒加载策略
     */
    @Test
    public void testOneToManyFind(){
        DoubleOneToManyCustomer customer = entityManager.find(DoubleOneToManyCustomer.class, 1);
        System.out.println(customer.getOrders());

    }

    /***
     * 将 order 中的外键置 null，然后才删成功
     * 默认情况下，若删除 1 的一端，则会先把关联的 n 的一端的外键置空，然后再删除 1
     * 如果设置了 cascade = {CascadeType.REMOVE}，那么就会把 n 的一端都删除了，例如删除一个概念，那么就会把该概念的实体都删除了
     *
     * Hibernate:
     *     select
     *         onetomanyc0_.id as id1_2_0_,
     *         onetomanyc0_.age as age2_2_0_,
     *         onetomanyc0_.email as email3_2_0_,
     *         onetomanyc0_.lastName as lastname4_2_0_
     *     from
     *         JPA_ONE_TO_MANY_CUSTOMERS onetomanyc0_
     *     where
     *         onetomanyc0_.id=?
     * Hibernate:
     *     update
     *         JPA_ORDERS
     *     set
     *         customer_id=null
     *     where
     *         customer_id=?
     * Hibernate:
     *     delete
     *     from
     *         JPA_ONE_TO_MANY_CUSTOMERS
     *     where
     *         id=?
     */
    @Test
    public void testOneToManyRemove(){
        DoubleOneToManyCustomer customer = entityManager.find(DoubleOneToManyCustomer.class, 2);
        entityManager.remove(customer);
    }

    @Test
    public void testOneToManyUpdate(){
        DoubleOneToManyCustomer customer = entityManager.find(DoubleOneToManyCustomer.class, 3);
        customer.getOrders().iterator().next().setOrderName("order-33");
    }

}
