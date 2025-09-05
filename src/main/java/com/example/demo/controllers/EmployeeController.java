package com.example.demo.controllers;

import com.example.demo.models.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    public EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Welcome to Employee Controller");
    }

    @GetMapping("/employees")
    public List<Employee> index(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee show(@PathVariable int employeeId){
        return employeeRepository.findById(employeeId).orElse(null);
    }



    @PostMapping("/employees")
    public Employee create(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{employeeId}")
    public Employee update(@PathVariable int employeeId, @RequestBody Employee employee){

        Employee oldEmployee = employeeRepository.findById(employeeId).orElse(null);
        if(oldEmployee == null){
            return employeeRepository.save(employee);
        }
        oldEmployee.setName(employee.getName());
        return employeeRepository.save(oldEmployee);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<?> delete(@PathVariable int employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null){
            return ResponseEntity.notFound().build();
        }
        employeeRepository.delete(employee);
        return ResponseEntity.ok().build();
    }


}
