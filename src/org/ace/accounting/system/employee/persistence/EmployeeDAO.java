package org.ace.accounting.system.employee.persistence;

import org.ace.accounting.system.employee.Employee;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class EmployeeDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Employee employee) {
        if (employee.getId() == null) {
            em.persist(employee);   // Create
        } else {
            em.merge(employee);     // Update
        }
    }

    @Transactional
    public void delete(Employee employee) {
        Employee emp = em.find(Employee.class, employee.getId());
        if (emp != null) {
            em.remove(emp);
        }
    }

    public Employee findById(String id) {
        return em.find(Employee.class, id);
    }

    public List<Employee> findAll() {
        return em.createQuery("SELECT e FROM Employee e", Employee.class)
                 .getResultList();
    }
}
