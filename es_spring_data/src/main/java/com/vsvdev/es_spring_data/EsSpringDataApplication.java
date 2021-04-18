package com.vsvdev.es_spring_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
public class EsSpringDataApplication {

    private final CustomerRepository repository;
    @Autowired
    public EsSpringDataApplication(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/saveCustomer")
    public int saveCustomer(@RequestBody List<Customer> customers) {
        repository.saveAll(customers);
        return customers.size();
    }

    @GetMapping("/findAll")
    public Iterable<Customer> findAllCustomers() {
        return repository.findAll();
    }

    @GetMapping("/findByFName/{firstName}")
    public List<Customer> findByFirstName(@PathVariable String firstName) {
        return repository.findByFirstname(firstName);
    }

    public static void main(String[] args) {
        SpringApplication.run(EsSpringDataApplication.class, args);
    }

}
