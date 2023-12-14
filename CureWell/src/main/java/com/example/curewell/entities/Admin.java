package com.example.curewell.entities;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class Admin extends Employee{
    public Admin(int id, String firstName, String lastName, String phone, String address, String password, String email, List<Company> companies) {
        super(id, firstName, lastName, phone, address, password, email, companies);
    }

    public static void addEmployee(@NotNull List<Employee> employeeList, Employee employee) {
        employeeList.add(employee);
    }

    public static void deleteEmployee(@NotNull List<Employee> employeeList, Employee employee) {
        employeeList.removeIf(comp -> Objects.equals(comp, employee));
    }
}
