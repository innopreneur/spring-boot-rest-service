package com.company.services.web.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.company.services.web.model.Employee;
import com.company.services.web.repository.EmployeeRepository;



@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

	@Mock	
	private EmployeeRepository employeeRepository;

	
	@Test
	public void whenValidId_thenEmployeeShouldBeFound() {
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Mockito.doReturn(Optional.of(new Employee())).when(employeeRepository).findById(anyLong());        
        assertNotNull(employeeService.getEmployeeById(1L));
	 }
	
	@Test
	public void findAllEmployeesTest() {
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Mockito.doReturn(new ArrayList<Employee>()).when(employeeRepository).findAll(); 
		assertNotNull(employeeService.getAllEmployees());
	}
	
	@Test
	public void updateEmployeeTest() {
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Mockito.doReturn(Optional.of(new Employee())).when(employeeRepository).findById(anyLong());
		Mockito.doReturn(new Employee()).when(employeeRepository).save(any());
		assertNotNull(employeeService.updateEmployee(2L, new Employee()));
	}
	
	
	@Test
	public void deleteEmployeeTest() {
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		Mockito.doReturn(Optional.of(new Employee())).when(employeeRepository).findById(anyLong());
		Mockito.doNothing().when(employeeRepository).delete(any());
		employeeService.deleteEmployee(anyLong());
	}
	
}

