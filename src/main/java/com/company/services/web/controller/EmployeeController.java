package com.company.services.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.company.services.web.model.Employee;
import com.company.services.web.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {

		return employeeService.getAllEmployees();
	}

	@PostMapping("/employees")
	public ResponseEntity<Object> createEmployee(
			@Valid @RequestBody Employee employee) {
		Employee savedEmployee = employeeService.saveEmployee(employee);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedEmployee.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/employees/{id}")
	public Resource<Employee> getEmployeeById(
			@PathVariable(value = "id") Long employeeId) {
		
		Employee employee =  
				employeeService.getEmployeeById(employeeId);

		//HATEOS implementation 
		//(for more details - https://en.wikipedia.org/wiki/HATEOAS)
		Resource<Employee> resource = new Resource<Employee>(employee);
		ControllerLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).getAllEmployees());
		resource.add(linkTo.withRel("all-employees"));

		return resource;
	}

	@PutMapping("/employees/{id}")
	public Employee updateEmployee(
			@PathVariable(value = "id") Long employeeId,
			@Valid @RequestBody Employee employeeDetails) {

		return employeeService.updateEmployee(
				employeeId, employeeDetails);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(
			@PathVariable(value = "id") Long employeeId) {
		
		employeeService.deleteEmployee(employeeId);
		return ResponseEntity.ok().build();
	}
}
