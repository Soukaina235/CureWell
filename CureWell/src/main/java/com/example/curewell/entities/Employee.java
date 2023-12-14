package com.example.curewell.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Employee {
    protected final int id;
    protected final String firstName;
    protected final String lastName;
    protected String phone;
    protected String address;
    protected String password;
    protected String email;
    protected List<Company> companies;

    public Employee(int id, String firstName, String lastName, String phone, String address, String password, String email, List<Company> companies) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.email = email;
        this.companies = companies;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", companies=" + companies +
                '}';
    }

    // login and logout in LoginController

    public static void addCompany(@org.jetbrains.annotations.NotNull List<Company> companies, Company company) {
        companies.add(company);
    }

    public static void deleteCompany(@NotNull List<Company> companies, Company company) {
        companies.removeIf(comp -> Objects.equals(comp, company));
// same as
//        for (Company comp : companies) {
//            if (Objects.equals(comp, company)) companies.remove(comp);
//        }
    }
}