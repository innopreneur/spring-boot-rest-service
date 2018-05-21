package com.company.services.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.services.web.exception.EmployeeNotFoundException;
import com.company.services.web.model.Employee;
import com.company.services.web.repository.EmployeeRepository;

@Transactional
@Service
public class EmployeeService {
	
	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		employeeRepository.findAll().forEach(employees::add);
		return employees;
	}


	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}


	public Employee getEmployeeById(Long employeeId) {
		return employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException("employee", "id", employeeId));
	}


	public Employee updateEmployee(Long employeeId,
			Employee employeeDetails) {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException("employee", "id", employeeId));

		employee.setName(employeeDetails.getName());
		employee.setBirthDate(employeeDetails.getBirthDate());
		employee.setEmail(employeeDetails.getEmail());
		employee.setHobbies(employeeDetails.getHobbies());

		Employee updatedemployee = employeeRepository.save(employee);
		return updatedemployee;
	}


	public void deleteEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException("employee", "id", employeeId));

		employeeRepository.delete(employee);
	}
}


