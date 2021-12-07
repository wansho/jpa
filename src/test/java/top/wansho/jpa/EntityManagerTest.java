package top.wansho.jpa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.wansho.jpa.entity.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EntityManagerTest {

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
        System.out.println("entityTransaction is active? " + entityTransaction.isActive()); // true
        entityTransaction.commit();
        System.out.println("entityTransaction is active? " + entityTransaction.isActive()); // false
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void testFind(){
        // 类似于 Hibernate 中的 get 方法，获取 id 为 1 的记录
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println("-------------------------");
        System.out.println(customer);
        // 先打印查询语句，再打印 --，饿汉式
    }

    @Test
    public void testGetReference(){
        // 类似于 Hibernate 中的 load 方法
        Customer customer = entityManager.getReference(Customer.class, 1); // 只获取引用
        System.out.println(customer.getClass().getName()); // top.wansho.jpa.entity.Customer$HibernateProxy$GLVOzPIc
        System.out.println("-------------------------");
        System.out.println(customer);
        // 先打印 --，再打印查询语句，懒加载，懒汉式，customer 是一个代理对象
    }

    @Test
    public void testPersist(){
        // 类似于 Hibernate 的 save 方法，使对象由临时状态变为持久化状态
        // 和 Hibernate 的 save 方法有些许不同，如果对象有 id，则不能执行 insert 操作，而会抛出异常
        Customer customer = new Customer();
        customer.setAge(13);
        customer.setEmail("wansho@163.com");
        customer.setLastName("www");
        entityManager.persist(customer);
        System.out.println(customer.getId()); // 有 id 了
    }

    @Test
    public void testRemove(){
        // 类似于 Hibernate 的 delete 对象，把对象对应的记录从数据库中移除
        // 注意：该方法只能移除持久化对象，而 hibernate 的 delete 方法实际上还可以移除游离对象
//        Customer customer = new Customer();
//        customer.setId(1); // 还是因为代理类的原因，不能删除游离对象
        Customer customer = entityManager.find(Customer.class, 1);
        entityManager.remove(customer);
    }

    @Test
    public void testMerge1(){
        // 1. 测试临时对象
        // 如果传入一个临时对象（没有 id），会创建一个新的对象，把临时对象的属性复制到新的对象中，然后对新的对象执行持久化操作
        // 所以新的对象会有 id，而临时对象没有 id
        Customer customer = new Customer();
        customer.setEmail("hehe@gmail.com");
        customer.setAge(18);
        customer.setLastName("ww");

        Customer customer2 = entityManager.merge(customer);
        System.out.println(customer.getId()); // null
        System.out.println(customer2.getId()); // 有 id
    }

    @Test
    public void testMerge2(){
        // 2. 测试游离对象
        // 2.1 在 EntityManager 缓存中没有该对象
        // 2.2 在数据库中也没有对应的记录
        // 2.3 JPA 会创建一个新的对象，然后把当前游离对象的属性复制到新创建的对象中
        // 2.4 对新创建的对象执行 insert 操作
        Customer customer = new Customer();
        customer.setEmail("hehe@gmail.com");
        customer.setAge(18);
        customer.setLastName("ww");
        customer.setId(20);

        Customer customer2 = entityManager.merge(customer);
        System.out.println(customer.getId());  // 20
        System.out.println(customer2.getId()); // 6，新对象 的 id 自增，和写入的 id 不一样
    }

    @Test
    public void testMerge3(){
        // 3. 测试游离对象
        // 3.1 在 EntityManager 缓存中没有该对象
        // 3.2 在数据库中有对应的记录
        // 3.3 JPA 会查询对应的记录，然后返回该记录对应的对象，然后再把游离对象的属性复制到查询到的对象中
        // 3.4 对查询到的对象进行 update
        Customer customer = new Customer();
        customer.setEmail("ww@gmail.com");
        customer.setAge(18);
        customer.setLastName("ww");
        customer.setId(6);

        Customer customer2 = entityManager.merge(customer);
        System.out.println(customer2 == customer); // false
        System.out.println(customer.getId());  // 6
        System.out.println(customer2.getId()); // 6，新对象 的 id 自增，和写入的 id 不一样
    }

    @Test
    public void testMerge4(){
        // 4. 测试游离对象
        // 4.1 在 EntityManager 缓存有该对象
        // 4.2 JPA 会把游离对象的属性直接复制给缓存中的对象
        // 4.3 对缓存中的对象执行 update
        Customer customer = new Customer();
        customer.setEmail("ww@gmail.com");
        customer.setAge(18);
        customer.setLastName("ww");
        customer.setId(6);

        Customer customer2 = entityManager.find(Customer.class, 6);
        entityManager.merge(customer);
        System.out.println(customer2 == customer);
    }

    @Test
    public void testFlush(){
        // 同 Hibernate 中 session 的 flush 方法，强制写入数据库
        Customer customer = entityManager.find(Customer.class, 1);
        customer.setLastName("dd");
        entityManager.flush();
    }

    @Test
    public void testRefresh(){
        // 从数据库中读最新的数据，强制刷新
        Customer customer = entityManager.find(Customer.class, 2); // select 1
        customer = entityManager.find(Customer.class, 2); // jpa 有一级缓存，这条语句没有去数据库中查询
        entityManager.refresh(customer); // select 2
        // 一共执行了两条查询语句
    }

}