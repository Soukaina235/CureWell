package com.example.curewell.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class EmployeeTest {
    private Employee employee;

    @Before
    public void before() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1, "comp1", "0609080706", "adr1"));
        companies.add(new Company(2, "comp2", "06123456", "adr2"));

        employee = new Employee(
                12345, "employee1", "EMP1",
                "060102030405", "adr123", "passwd1",
                "employee1@gmail.com", companies
        );
    }

    @Test
    public void testGettersConstructor() {
        assertEquals(12345, employee.getId());
        assertEquals("employee1", employee.getFirstName());
        assertEquals("EMP1", employee.getLastName());
        assertEquals("060102030405", employee.getPhone());
        assertEquals("adr123", employee.getAddress());
        assertEquals("passwd1", employee.getPassword());
        assertEquals("employee1@gmail.com", employee.getEmail());
    }

    @Test
    public void testGetCompaniesConstructor() {
//        assertTrue(new Company(1, "comp1", "0609080706", "adr1") == employee.getCompanies().get(0));
        assertEquals(new Company(1, "comp1", "0609080706", "adr1"), employee.getCompanies().get(0));
        assertTrue(Objects.equals(new Company(2, "comp2", "06123456", "adr2"), employee.getCompanies().get(1)));
    }

    @Test
    public void testSetters() {
        employee.setPhone("0601020304");
        assertEquals("0601020304", employee.getPhone());

        employee.setAddress("adr123 New");
        assertEquals("adr123 New", employee.getAddress());

        employee.setPassword("passwd1 New");
        assertEquals("passwd1 New", employee.getPassword());

        employee.setEmail("employee1NEW@gmail.com");
        assertEquals("employee1NEW@gmail.com", employee.getEmail());
    }

    @Test
    public void testToString() {
        String msg = "Employee{" +
                "id=" + employee.getId() +
                ", firstName='" + employee.getFirstName() + '\'' +
                ", lastName='" + employee.getLastName() + '\'' +
                ", phone='" + employee.getPhone() + '\'' +
                ", address='" + employee.getAddress() + '\'' +
                ", password='" + employee.getPassword() + '\'' +
                ", email='" + employee.getEmail() + '\'' +
                ", companies=" + employee.getCompanies() +
                '}';
        assertEquals(msg, employee.toString());
    }

    @Test
    public void testAddCompany() {
        Employee.addCompany(
                employee.getCompanies(),
                new Company(3, "comp3", "123456789", "adr3")
        );

        assertEquals(new Company(1, "comp1", "0609080706", "adr1"), employee.getCompanies().get(0));
        assertEquals(new Company(2, "comp2", "06123456", "adr2"), employee.getCompanies().get(1));
        assertEquals(new Company(3, "comp3", "123456789", "adr3"), employee.getCompanies().get(2));

    }

    @Test
    public void testDeleteCompany() {
        Employee.deleteCompany(
                employee.getCompanies(),
                new Company(2, "comp2", "06123456", "adr2")
        );

        assertEquals(new Company(1, "comp1", "0609080706", "adr1"), employee.getCompanies().get(0));
    }
}