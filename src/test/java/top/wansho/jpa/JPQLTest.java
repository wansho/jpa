package top.wansho.jpa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.wansho.jpa.entity.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author wanshuo
 * @date 2021-05-30 13:35:01
 */
public class JPQLTest {

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
     * 查询 Demo
     * 注意：
     * 1. from 后面的表要写类名
     * 2. setParameter 的占位符要从 1 开始
     *
     */
    @Test
    public void testHelloJPQL(){
        String jpql = "from Customer c where c.age > ?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1, 1); // 查询年龄大于 1 岁的消费者
        List<Customer> resultList = query.getResultList();
        System.out.println(resultList);
    }

    /***
     * 默认情况下，只查询部分属性的化，则返回 Object[] 类型的 List
     * 也可以在实体中创建对应的构造器，然后在 JPQL 语句中利用对应的构造器返回实体类的对象。
     */
    @Test
    public void testPartlyProperties(){
        String jpql = "select new Customer(c.lastName, c.age) from Customer c where c.age > ?1";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1, 10).getResultList().stream().forEach(result -> {
            System.out.println(result); // 默认返回的是数组对象
        });
    }

    /***
     * 写原生的 sql
     */
    @Test
    public void testNativeQuery(){
        String sql = "select age from jpa_customers where id = ?1";
        Query query = entityManager.createNativeQuery(sql).setParameter(1, 3);
        System.out.println(query.getSingleResult());
    }

    /***
     * 测试 orderBy
     */
    @Test
    public void testOrderBy(){
        String jpql = "select new Customer(c.lastName, c.age) from Customer c where c.age > ?1 order by c.age desc";
        Query query = entityManager.createQuery(jpql);
        query.setParameter(1, 10).getResultList().stream().forEach(result -> {
            System.out.println(result); // 默认返回的是数组对象
        });
    }

    /***
     * 测试 groupBy, having
     * 查询 order 数量大于 1 的那些 Customer
     */
    @Test
    public void testGroupBy(){
        String jpql = "select o.customer from ManyToOneOrder o group by o.customer having count(o.id) > 1";
        Query query = entityManager.createQuery(jpql);
        query.getResultList().stream().forEach(result -> {
            System.out.println(result); // 默认返回的是数组对象
        });
    }

    /***
     * 左外连接查询
     * 获取某一个用户的所有订单
     */
    @Test
    public void testLeftOuterJoinFetch(){
        String jpql = "from OneToManyCustomer c left outer join fetch c.orders where c.id = ?1";
        OneToManyCustomer customer = (OneToManyCustomer) entityManager.createQuery(jpql).setParameter(1, 3).getSingleResult();
        System.out.println(customer.getOrders());
    }

    /***
     * 子查询
     * 查询所有 Customer 的 lastName 为 www 的 Order
     */
    @Test
    public void testSubQuery(){
        String jpql = "select o from ManyToOneOrder o where o.customer = (select c from Customer c where c.lastName = ?1)";
        List<ManyToOneOrder> manyToOneOrders = entityManager.createQuery(jpql).setParameter(1, "wdx").getResultList();
        System.out.println(manyToOneOrders);
    }

    /***
     * 使用 JPQL 内建函数
     */
    @Test
    public void testFunction(){
        String jpql = "select upper(c.email) from Customer c";
        List<Customer> customers = entityManager.createQuery(jpql).getResultList();
        System.out.println(customers);
    }

    /***
     * 执行 update，delete
     */
    @Test
    public void testUpdate(){
        String jpql = "update Customer c set c.lastName = ?1 where c.id = ?2";
        Query query = entityManager.createQuery(jpql).setParameter(1, "wwww").setParameter(2, 1);
        query.executeUpdate();
    }

}
