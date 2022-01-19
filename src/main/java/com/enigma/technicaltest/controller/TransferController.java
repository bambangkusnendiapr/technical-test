package com.enigma.technicaltest.controller;

import com.enigma.technicaltest.dto.TransferDTO;
import com.enigma.technicaltest.entity.Transfer;
import com.enigma.technicaltest.request.FillInBalanceRequest;
import com.enigma.technicaltest.request.TransferRequest;
import com.enigma.technicaltest.response.PageResponse;
import com.enigma.technicaltest.response.WebResponse;
import com.enigma.technicaltest.service.TransferService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/transfer"})
@SecurityRequirement(name = "technicaltestapi")
@NoArgsConstructor
public class TransferController {

  @Autowired
  private TransferService transferService;

  @PostMapping("/fill-in-balance")
  public ResponseEntity<WebResponse<Transfer>> fillInBalance(@RequestBody FillInBalanceRequest request) {
    Transfer transfer = transferService.fillInBalance(request);
    WebResponse<Transfer> response = new WebResponse<>("Fill In Balance is Successfully", transfer);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/transfer")
  public ResponseEntity<WebResponse<Transfer>> transfer(@RequestBody TransferRequest request) {
    Transfer transfer = transferService.transfer(request);
    WebResponse<Transfer> response = new WebResponse<>("Transfer is Successfully", transfer);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/history")
  public ResponseEntity<WebResponse<PageResponse<?>>> getHistory(
          @RequestParam(name = "size", defaultValue = "10") Integer size,
          @RequestParam(name = "page",defaultValue = "0") Integer page,
          @RequestParam(name = "sortBy",defaultValue = "createdAt") String sortBy,
          @RequestParam(name = "direction",defaultValue = "DESC") String direction
  ) {
    Sort sort = Sort.by(Sort.Direction.fromString(direction),sortBy);
    Pageable pageable = PageRequest.of(page,size,sort);
    TransferDTO transferDTO = new TransferDTO();
    Page<Transfer> transferPage = this.transferService.getAll(pageable, transferDTO);
    PageResponse<Transfer> pageResponse = new PageResponse<>(
            transferPage.getContent(),
            transferPage.getTotalElements(),
            transferPage.getTotalPages(),
            page,
            size,
            sortBy
    );

    return ResponseEntity.status(HttpStatus.OK).body( new WebResponse<>("Transfer History",pageResponse));
  }

}
