package edu.miu.cs.cs489appsd.lab2a.employeepensionapp;

import edu.miu.cs.cs489appsd.lab2a.employeepensionapp.model.Employee;
import edu.miu.cs.cs489appsd.lab2a.employeepensionapp.model.PensionPlan;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class EmployeePensionApp {
    private static final ObjectMapper mapper = buildMapper();

    public static void main(String[] args) {
        List<Employee> employees = loadData();

        System.out.println("All Employees (JSON):");
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::getYearlySalary).reversed()
                        .thenComparing(Employee::getLastName))
                .collect(Collectors.toList());
        printAsJson(sortedEmployees);

        System.out.println("\nQuarterly Upcoming Enrollees:");
        LocalDate nextQuarterStart = LocalDate.of(2025, 1, 1);  // Example
        LocalDate nextQuarterEnd = LocalDate.of(2025, 3, 31);
        List<Employee> upcomingEnrollees = employees.stream()
                .filter(e -> e.getPensionPlan() == null || e.getPensionPlan().getEnrollmentDate() == null)
                .filter(e -> e.getEmploymentDate().plusYears(3).isBefore(nextQuarterEnd.plusDays(1)))
                .sorted(Comparator.comparing(Employee::getEmploymentDate).reversed())
                .collect(Collectors.toList());
        printAsJson(upcomingEnrollees);
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    private static void printAsJson(Object obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> loadData() {
        Employee e1 = new Employee(1, "Daniel", "Agar", LocalDate.parse("2023-01-17"), 105945.50);
        e1.setPensionPlan(new PensionPlan("EX1089", null, 100.00));

        Employee e2 = new Employee(2, "Benard", "Shaw", LocalDate.parse("2022-09-03"), 197750.00);
        e2.setPensionPlan(new PensionPlan(null, LocalDate.parse("2025-09-03"), 0));

        Employee e3 = new Employee(3, "Carly", "Agar", LocalDate.parse("2014-05-16"), 842000.75);
        e3.setPensionPlan(new PensionPlan("SM2307", LocalDate.parse("2017-05-17"), 1555.50));

        Employee e4 = new Employee(4, "Wesley", "Schneider", LocalDate.parse("2023-07-21"), 74500.00);

        Employee e5 = new Employee(5, "Anna", "Wiltord", LocalDate.parse("2023-03-15"), 85750.00);

        Employee e6 = new Employee(6, "Yosef", "Tesfalem", LocalDate.parse("2024-10-31"), 100000.00);

        return Arrays.asList(e1, e2, e3, e4, e5, e6);
    }
}