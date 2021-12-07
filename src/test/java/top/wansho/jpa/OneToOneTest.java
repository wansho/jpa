package top.wansho.jpa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.wansho.jpa.entity.Department;
import top.wansho.jpa.entity.Manager;
import top.wansho.jpa.entity.OneToManyCustomer;
import top.wansho.jpa.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author wanshuo
 * @date 2021-05-30 13:35:01
 */
public class OneToOneTest {

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
     * 双向 1-1 关系，先保存不维护关系的一方，即没有外键的一方，这样不会多出 UPDATE 语句
     */
    @Test
    public void testOneToOnePersist(){
        Manager manager = new Manager();
        manager.setName("zjf");

        Department department = new Department();
        department.setName("txpt");

        department.setManager(manager);
        manager.setDepartment(department);

        entityManager.persist(manager);
        entityManager.persist(department);
    }

    /***
     * 默认情况下，如果获取维护关系的一方，则会通过左外连接获取其关联的对象
     * 但是可以通过修改 fetch 属性来修改加载策略
     */
    @Test
    public void testOneToOneFind(){
        Department department = entityManager.find(Department.class, 1);
        System.out.println(department.getManager().getDepartment().getClass().getName()); // 如果不设置懒加载，就是代理类
    }

    /***
     * 默认情况下，若获取不维护关系的一方，则也会通过左外连接来获取关联的对象
     * 可以通过修改 fetch 来修改加载策略，但是依然会发送 SQL 语句来初始化其关联的对象
     * 说明在不维护关系的一方，不建议修改 fetch 属性
     */
    @Test
    public void testOneToOneFind2(){
        Manager manager = entityManager.find(Manager.class, 1);
        System.out.println(manager.getDepartment().getClass().getName());
    }

}
