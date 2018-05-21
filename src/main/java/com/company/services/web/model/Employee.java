package com.company.services.web.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID uuid = UUID.randomUUID();

	@NotNull
	@Column(name = "email", unique = true)
	private String email;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Past
	@Column(name = "birthdate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date birthDate;

	@ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hobbies", joinColumns = @JoinColumn(name = "employee_id"))
	@Column(name = "hobbies")
	private Set<String> hobbies = new HashSet<>();

	public Employee() {}

	public Employee(@NotNull String email, @NotNull String name, @NotNull @Past Date birthDate,
			Set<String> hobbies) {
		super();
		this.email = email;
		this.name = name;
		this.birthDate = birthDate;
		this.hobbies = hobbies;
	}

	public Long getId() {
		return id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Set<String> getHobbies() {
		return hobbies;
	}
	public void setHobbies(Set<String> hobbies) {
		this.hobbies = hobbies;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", uuid=" + uuid + ","
				+ " email=" + email + ", name=" + name + ", birthDate="
				+ birthDate + ", hobbies=" + hobbies + "]";
	}



}
