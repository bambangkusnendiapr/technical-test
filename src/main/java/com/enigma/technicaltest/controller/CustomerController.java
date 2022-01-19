package com.enigma.technicaltest.controller;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.request.CustomerUpdateRequest;
import com.enigma.technicaltest.response.PageResponse;
import com.enigma.technicaltest.response.WebResponse;
import com.enigma.technicaltest.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/customers"})
@SecurityRequirement(name = "technicaltestapi")
@NoArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<WebResponse<PageResponse<?>>> getAllCustomer(
        @RequestParam(name = "size", defaultValue = "10") Integer size,
        @RequestParam(name = "page",defaultValue = "0") Integer page,
        @RequestParam(name = "sortBy",defaultValue = "name") String sortBy,
        @RequestParam(name = "direction",defaultValue = "ASC") String direction,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "accountNumber",required = false) String accountNumber
    ){
        Sort sort = Sort.by(Sort.Direction.fromString(direction),sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);
        CustomerDTO customerDTO = new CustomerDTO(name,accountNumber);
        Page<Customer> customers =customerService.getAll(pageable,customerDTO,sort);
        PageResponse<Customer> pageResponse =new PageResponse<>(
            customers.getContent(),
            customers.getTotalElements(),
            customers.getTotalPages(),
            page,
            size,
            sortBy
        );
        return ResponseEntity.status(HttpStatus.OK).body( new WebResponse<>("All Customer Data",pageResponse));
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<WebResponse<?>> getCustomerById(@PathVariable("customerId") String id )
            throws NotFoundException{
        Customer customer = customerService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).
                body(new WebResponse<>(String.format("Customer with ID %s found",id),customer));
    }

    @PutMapping(value = "/{customerId}")
    public ResponseEntity<WebResponse<?>> updateCustomerById(
            @PathVariable("customerId") String id,
            @RequestBody CustomerUpdateRequest request ) throws NotFoundException {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setAddress(request.getAddres());
        customer.setPhone(request.getPhone());
        Customer update = customerService.update(id,customer);
        return ResponseEntity.status(HttpStatus.OK).body(new WebResponse<>(String.format("Successfully Updated Customer Data with ID %s",id),update));
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity<WebResponse<String>> deleteCustomerById(@PathVariable("customerId") String id){
        String deleteCustomer = customerService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new WebResponse<>(String.format("ID %s successfully deleted",id),deleteCustomer));
    }


}
