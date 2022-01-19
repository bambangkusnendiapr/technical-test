package com.enigma.technicaltest.controller;

import com.enigma.technicaltest.dto.CustomerDTO;
import com.enigma.technicaltest.dto.MerchantDTO;
import com.enigma.technicaltest.entity.Customer;
import com.enigma.technicaltest.entity.Merchant;
import com.enigma.technicaltest.exception.NotFoundException;
import com.enigma.technicaltest.request.CustomerUpdateRequest;
import com.enigma.technicaltest.request.MerchantUpdateRequest;
import com.enigma.technicaltest.response.PageResponse;
import com.enigma.technicaltest.response.WebResponse;
import com.enigma.technicaltest.service.MerchantService;
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
@RequestMapping({"/merchant"})
@SecurityRequirement(name = "technicaltestapi")
@NoArgsConstructor
public class MerchantController {

  @Autowired
  private MerchantService merchantService;

  @PutMapping(value = "/{merchantId}")
  @PreAuthorize("hasAnyAuthority('MERCHANT_ROLE')")
  @SecurityRequirement(name = "technicaltestapi")
  public ResponseEntity<WebResponse<?>> updateMerchant(
          @PathVariable("merchantId") String id,
          @RequestBody MerchantUpdateRequest request ) throws NotFoundException {
//    Customer customer = new Customer();
    Merchant merchant = new Merchant();
    merchant.setName(request.getName());
    merchant.setAddress(request.getAddres());
    Merchant update = merchantService.update(id,merchant);
    return ResponseEntity.status(HttpStatus.OK).body(new WebResponse<>(String.format("Successfully Updated Merchant Data with ID %s",id),update));
  }

  @GetMapping(value = "/{merchantId}")
  public ResponseEntity<WebResponse<?>> getCustomerById(@PathVariable("merchantId") String id )
          throws NotFoundException{
    Merchant merchant = merchantService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).
            body(new WebResponse<>(String.format("Merchant with ID %s found",id),merchant));
  }

  @DeleteMapping(value = "/{merchantId}")
  @PreAuthorize("hasAnyAuthority('MERCHANT_ROLE')")
  @SecurityRequirement(name = "technicaltestapi")
  public ResponseEntity<WebResponse<String>> deleteMerchant(@PathVariable("merchantId") String id){
    String deleteMerchant = merchantService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body(new WebResponse<>(String.format("ID %s successfully deleted",id),deleteMerchant));
  }

  @GetMapping
  public ResponseEntity<WebResponse<PageResponse<?>>> getAll(
          @RequestParam(name = "size", defaultValue = "10") Integer size,
          @RequestParam(name = "page",defaultValue = "0") Integer page,
          @RequestParam(name = "sortBy",defaultValue = "name") String sortBy,
          @RequestParam(name = "direction",defaultValue = "ASC") String direction,
          @RequestParam(name = "name", required = false) String name
  ){
    Sort sort = Sort.by(Sort.Direction.fromString(direction),sortBy);
    Pageable pageable = PageRequest.of(page,size,sort);
    MerchantDTO merchantDTO = new MerchantDTO(name);
    Page<Merchant> merchants =merchantService.getAll(pageable,merchantDTO,sort);
    PageResponse<Merchant> pageResponse =new PageResponse<>(
            merchants.getContent(),
            merchants.getTotalElements(),
            merchants.getTotalPages(),
            page,
            size,
            sortBy
    );
    return ResponseEntity.status(HttpStatus.OK).body( new WebResponse<>("All Merchant Data",pageResponse));
  }

}
