package com.company.services.web.controller; 

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.services.web.model.Employee;
import com.company.services.web.service.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private EmployeeService service;
	
	@Test
	public void getAllEmployees() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "manan", new Date(), null);
	    List<Employee> allEmployees = Arrays.asList(employee);
	    Mockito.when(service.getAllEmployees()).thenReturn(allEmployees);
	    mvc.perform(get("/api/employees")
	    	      .contentType(MediaType.APPLICATION_JSON))
	    	      .andExpect(status().isOk())
	    	      .andExpect(jsonPath("$", hasSize(1)))
	    	      .andExpect(jsonPath("$[0].name", is(employee.getName())));
	}
	
	@Test
	public void getEmployeesById() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "manan", new Date(), null);
	    Mockito.when(service.getEmployeeById(anyLong())).thenReturn(employee);
	    mvc.perform(get("/api/employees/{id}", 1L)
	    	      .contentType(MediaType.APPLICATION_JSON))
	    	      .andExpect(status().isOk())
	    	      .andExpect(jsonPath("$.name", is(employee.getName())));
	}
}
