package top.wansho.jpa;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.wansho.jpa.entity.Customer;
import top.wansho.jpa.entity.ManyToOneOrder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author wanshuo
 * @date 2021-05-30 13:35:01
 */
public class ManyToOneTest {

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
    public void testManyToOnePersist(){

        Customer customer = new Customer();
        customer.setEmail("wdx@gmail.com");
        customer.setAge(18);
        customer.setLastName("wdx");

        ManyToOneOrder order1 = new ManyToOneOrder();
        order1.setOrderName("wdx-order-1");
        order1.setCustomer(customer);

        ManyToOneOrder order2 = new ManyToOneOrder();
        order2.setOrderName("wdx-order-2");
        order2.setCustomer(customer);

        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);
    }

    /***
     * 默认使用左外连接的方式来获取 n 的一端的对象和其关联的 1 的一端的对象
     *    select
     *         order0_.id as id1_1_0_,
     *         order0_.CUSTOMER_ID as customer3_1_0_,
     *         order0_.orderName as ordernam2_1_0_,
     *         customer1_.id as id1_0_1_,
     *         customer1_.age as age2_0_1_,
     *         customer1_.email as email3_0_1_,
     *         customer1_.lastName as lastname4_0_1_
     *     from
     *         JPA_ORDERS order0_
     *     left outer join
     *         JPA_CUSTOMERS customer1_
     *             on order0_.CUSTOMER_ID=customer1_.id
     *     where
     *         order0_.id=?
     *
     *
     * 懒加载模式：先查 ManyToOneOrder，再查 Customer
     *
     * Hibernate:
     *     select
     *         order0_.id as i d1_1_0_,
     *         order0_.CUSTOMER_ID as customer3_1_0_,
     *         order0_.orderName as ordernam2_1_0_
     *     from
     *         JPA_ORDERS order0_
     *     where
     *         order0_.id=?
     *
     * Hibernate:
     *     select
     *         customer0_.id as id1_0_0_,
     *         customer0_.age as age2_0_0_,
     *         customer0_.email as email3_0_0_,
     *         customer0_.lastName as lastname4_0_0_
     *     from
     *         JPA_CUSTOMERS customer0_
     *     where
     *         customer0_.id=?
     *
     */
    @Test
    public void testManyToOneFind(){
        ManyToOneOrder order = entityManager.find(ManyToOneOrder.class, 1);
        String customerName = order.getCustomer().getLastName();
        System.out.println(customerName);
    }

    /***
     * 不能直接删除 1 的一端，因为有外键约束
     */
    @Test
    public void testManyToOneRemove(){
        // 删除 n 的一端
//        ManyToOneOrder order = entityManager.find(ManyToOneOrder.class, 1);
//        entityManager.remove(order);

        // 删除 1 的一端，直接报错 Cannot delete or update a parent row: a foreign key constraint fails
        // 必须把依赖 1 的 n 全删了，才能删除 1
        Customer customer = entityManager.find(Customer.class, 17);
        entityManager.remove(customer);
    }

    @Test
    public void testManyToOneUpdate(){
        ManyToOneOrder order = entityManager.find(ManyToOneOrder.class, 3);
        order.getCustomer().setLastName("fff");
    }

}
