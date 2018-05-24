package com.company.services.web.controller; 

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.services.web.model.Employee;
import com.company.services.web.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeService service;


	@Test
	public void test_get_all_employees_success() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), null);
		List<Employee> allEmployees = Arrays.asList(employee);

		Mockito.when(service.getAllEmployees()).thenReturn(allEmployees);
		mvc.perform(get("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].name", is(employee.getName())));
	}

	@Test
	public void test_get_all_employees_with_invalid_user_throws_401() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), null);
		List<Employee> allEmployees = Arrays.asList(employee);

		Mockito.when(service.getAllEmployees()).thenReturn(allEmployees);
		mvc.perform(get("/api/employees")
				.with(httpBasic("invalid_user", "password"))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_get_all_employees_with_invalid_password_throws_401() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), null);
		List<Employee> allEmployees = Arrays.asList(employee);

		Mockito.when(service.getAllEmployees()).thenReturn(allEmployees);
		mvc.perform(get("/api/employees")
				.with(httpBasic("user", "invalid_password"))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_get_employee_by_id_success() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), null);

		Mockito.when(service.getEmployeeById(anyLong())).thenReturn(employee);
		mvc.perform(get("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(employee.getName())));
	}

	@Test
	public void test_get_employee_by_id_with_invalid_user_throws_401() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), null);

		Mockito.when(service.getEmployeeById(anyLong())).thenReturn(employee);
		mvc.perform(get("/api/employees/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_get_employee_by_id_with_invalid_password_throws_401() throws Exception {
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), null);

		Mockito.when(service.getEmployeeById(anyLong())).thenReturn(employee);
		mvc.perform(get("/api/employees/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_save_employee_success() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isCreated())
		.andExpect(header().string("location", containsString("/api/employees/")));
	}

	@Test
	public void test_save_employee_without_name_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", null, new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_save_employee_without_full_name_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_save_employee_name_exceeding_limit_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "SomeReallyReallyReallyReally"
				+ "UnappropriateInvalidAndLongNameWithoutConsideringAnyLimits", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_save_employee_with_future_birthdate_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", getTomorrow(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_save_employee_with_non_unique_email_throws_500() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenThrow(DuplicateKeyException.class);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void test_save_employee_with_invalid_user_throws_401() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("invalid_user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_save_employee_with_invalid_password_throws_401() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "invalid_password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void test_save_employee_without_csrf_token_throws_403() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);

		Mockito.when(service.saveEmployee(any(Employee.class))).thenReturn(employee);
		mvc.perform(post("/api/employees")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee)))
		.andExpect(status().isForbidden());
	}

	@Test
	public void test_update_employee_success() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);
		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(employee.getName())));
	}

	@Test
	public void test_update_employee_with_future_birthdate_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", getTomorrow(), hobbies);

		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_update_employee_without_name_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", null, new Date(), hobbies);

		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_update_employee_without_full_name_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan", new Date(), hobbies);

		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_update_employee_name_exceeding_limit_throws_400() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "SomeReallyReallyReallyReally"
				+ "UnappropriateInvalidAndLongNameWithoutConsideringAnyLimits", new Date(), hobbies);

		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void test_update_employee_with_invalid_user_throws_401() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);
		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("invalid_user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_update_employee_with_invalid_password_throws_401() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);
		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "invalid_password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee))
				.with(csrf()))
		.andExpect(status().isUnauthorized());			
	}
	
	@Test
	public void test_update_employee_without_csrf_throws_403() throws Exception {
		Set<String>hobbies = new HashSet<String>();
		hobbies.add("coding");
		Employee employee = new Employee(1L, "manan@test.com", "Manan Patel", new Date(), hobbies);
		Mockito.when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(employee);
		mvc.perform(put("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(asJsonString(employee)))
		.andExpect(status().isForbidden());			
	}
	

	@Test
	public void test_delete_employee_success() throws Exception {
		Mockito.doNothing().when(service).deleteEmployee(anyLong());
		mvc.perform(delete("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
		.andExpect(status().isOk());
	}

	@Test
	public void test_delete_employee_with_invalid_user_throws_401() throws Exception {
		Mockito.doNothing().when(service).deleteEmployee(anyLong());
		mvc.perform(delete("/api/employees/{id}", 1L)
				.with(httpBasic("invalid_user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
		.andExpect(status().isUnauthorized());
	}

	@Test
	public void test_delete_employee_with_invalid_password_throws_401() throws Exception {
		Mockito.doNothing().when(service).deleteEmployee(anyLong());
		mvc.perform(delete("/api/employees/{id}", 1L)
				.with(httpBasic("user", "invalid_password"))
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void test_delete_employee_without_csrf_throws_403() throws Exception {
		Mockito.doNothing().when(service).deleteEmployee(anyLong());
		mvc.perform(delete("/api/employees/{id}", 1L)
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}

	/**
	 * Serialize object to json string
	 * @param obj object to be serialized to json
	 * @return json formatted string
	 */
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	} 
	
	/**
	 * get tomorrow as date
	 * @return Date for tomorrow
	 */
	public static Date getTomorrow()
	  {
	    // get a calendar instance, which defaults to "now"
	    Calendar calendar = Calendar.getInstance();
	    
	    // add one day to the date/calendar
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	    
	    // now get "tomorrow"
	    Date tomorrow = calendar.getTime();

	    return tomorrow;
	  }
}
