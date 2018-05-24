package com.company.services.web.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.company.services.web.EmployeeServiceApplication;
import com.company.services.web.model.Employee;

@ContextConfiguration(classes = {PersistenceContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EmployeeServiceApplication.class)
public class EmployeeRepositoryIntegrationTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	Employee savedEmp;
	

	@Before
	public void setupDatabase() {
		//clean database
		employeeRepository.deleteAll();
		
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee1 = new Employee(1l, "manan@test.com", "Manan Patel", new Date(), null);
		Employee employee2 = new Employee(2l, "maria@test.com", "Maria Sharapova", new Date(), hobbies);
		
		//insert test data
		savedEmp = employeeRepository.save(employee1);
		employeeRepository.save(employee2);
	}
	
	@After
	public void cleanDatabase() {
		employeeRepository.deleteAll();
	}

	
	@Test
	public void test_find_employee_by_id_successfully() {
		assertThat(employeeRepository.findById(savedEmp.getId()))
		.containsInstanceOf(Employee.class);

	}
	
	@Test
	public void test_find_employee_by_incorrect_id_gives_empty() {
		assertThat(employeeRepository.findById(50L))
		.isEmpty();

	}

	@Test
	public void test_find_all_employees_successfully() {
		assertThat(employeeRepository.findAll())
		.hasSize(2);
	}
	
	@Test
	public void test_save_employee_successfully() {
		Employee employee3 = new Employee(1l, "tom@test.com", "Tom Cruise", new Date(), null);

		assertThat(employeeRepository.save(employee3))
		.isInstanceOf(Employee.class);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void test_save_employee_with_duplicate_email_throws_exception() {
		Employee employee = new Employee(1l, "manan@test.com", "Manan Patel", new Date(), null);
		assertThat(employeeRepository.save(employee));
		
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void test_save_employee_without_name_throws_exception() {
		Employee employee = new Employee(1l, "brad@test.com", null, new Date(), null);
		assertThat(employeeRepository.save(employee));
		
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void test_save_employee_without_email_throws_exception() {
		Employee employee = new Employee(1l, null, "Manuel Neuer", new Date(), null);
		assertThat(employeeRepository.save(employee));
		
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void test_save_employee_without_birthdate_throws_exception() {
		Employee employee = new Employee(1l, "ajolie@hollywood.com", "Angelina Jolie", null, null);
		assertThat(employeeRepository.save(employee));
		
	}



}