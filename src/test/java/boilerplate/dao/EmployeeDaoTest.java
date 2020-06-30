package boilerplate.dao;

import boilerplate.AppConfig;
import boilerplate.TestEnvironment;
import boilerplate.entity.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(TestEnvironment.class)
public class EmployeeDaoTest {

  private final EmployeeDao dao = new EmployeeDaoImpl();

  @Test
  public void testSelectById() {
    TransactionManager tm = AppConfig.singleton().getTransactionManager();
    tm.required(
        () -> {
          Employee employee = dao.selectById(1);
          assertNotNull(employee);
          assertEquals("ALLEN", employee.name);
          assertEquals(Integer.valueOf(30), employee.age);
          assertEquals(Integer.valueOf(0), employee.version);
        });
  }

  @Test
  public void testSelectByAge() {
    TransactionManager tm = AppConfig.singleton().getTransactionManager();
    tm.required(
        () -> {
          List<Employee> employees = dao.selectByAge(35);
          assertEquals(2, employees.size());
        });
  }

  @Test
  public void testInser() {
    TransactionManager tm = AppConfig.singleton().getTransactionManager();

    Employee employee = new Employee();

    tm.required(
        () -> {
          employee.name = "HOGE";
          employee.age = 20;
          dao.insert(employee);
          assertNotNull(employee.id);
        });

    tm.required(
        () -> {
          Employee employee2 = dao.selectById(employee.id);
          assertEquals("HOGE", employee2.name);
          assertEquals(Integer.valueOf(20), employee2.age);
          assertEquals(Integer.valueOf(1), employee2.version);
        });
  }
}
