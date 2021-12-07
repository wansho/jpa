package top.wansho.jpa.helloworld;

import lombok.extern.slf4j.Slf4j;
import top.wansho.jpa.entity.Customer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * 不引入 Spring，单纯地用 JPA（Hibernate） 进行数据读写
 * @author wanshuo
 * @date 2021-05-29 14:14:31
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        // 1. 创建 EntityManagerFactory
        String persistenceUnitName = "default";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

        // 1.1 Persistence.createEntityManagerFactory 的一个重载方法，在 Java 代码中修改属性，和 xml 中配置作用一样
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql", true);
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);

        // 2. 创建 EntityManager，类似与 Hibernate 的 SessionFactory
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 3. 开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // 4. 进行持久化操作
        Customer customer = new Customer();
        customer.setLastName("wansho");
        customer.setAge(12);
        customer.setEmail("wanshojs@gmail.com");
        entityManager.persist(customer);

        // 5. 提交事务
        transaction.commit();

        // 6. 关闭 EntityManager
        entityManager.close();

        // 7. 关闭 EntityManagerFactory
        entityManagerFactory.close();
    }
}
