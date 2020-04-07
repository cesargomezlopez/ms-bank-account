package ms.bank.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ms.bank.account.model.BankAccount;
import ms.bank.account.service.IBankAccountService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/bankAccount")
@Api(value = "bankAccount")
public class BankAccountController {

  @Autowired
  private IBankAccountService bankAccountService;

  @GetMapping(value = "/findAllBankAccounts", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find all bank accounts", notes = "Find all bank accounts registered")
  public Mono<ResponseEntity<Flux<BankAccount>>> findAllBankAccounts() {
    return Mono.just(ResponseEntity
      .ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(bankAccountService.findAll()))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping(value = "/findBankAccountById", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find Bank Account by id", notes = "Find Bank Account registered by id")
  public Mono<ResponseEntity<BankAccount>> findBankAccountById(@RequestParam("id")String id) {
    return bankAccountService.findById(id).flatMap(ba -> {
      return Mono.just(ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(ba));
    }).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping(value = "/findBankAccountsByIdCient", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find Bank Accounts by client id",
      notes = "Find Bank Accounts registered by client id")
  public Mono<ResponseEntity<Flux<BankAccount>>>
      findBankAccountsByClientId(@RequestParam("clientId")String clientId) {
    return Mono.just(ResponseEntity
      .ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(bankAccountService.findByClientId(clientId)))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping(value = "/createBankAccount", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Creates a Bank Account", notes = "Register a new Bank Account")
  public Mono<ResponseEntity<BankAccount>>
      createBankAcount(@Valid @RequestBody BankAccount bankAccount) {
    return bankAccountService.create(bankAccount).flatMap(ba -> {
      return Mono.just(ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(ba));
    }).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping(value = "/updateBankAccount", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Updates a Bank Account", notes = "Modify a Bank Account")
  public Mono<ResponseEntity<BankAccount>> updateBankAcount(@Valid @RequestBody BankAccount bankAccount) {
    return bankAccountService.update(bankAccount).flatMap(ba -> {
      return Mono.just(ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(ba));
    }).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping(value = "/deleteBankAccountById", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Deletes a Bank Account by id", notes = "Removes a Bank Account registered by id")
  public Mono<ResponseEntity<Void>> deleteBankAccountById(@RequestParam("id")String id) {
    return bankAccountService.deleteById(id)
      .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

}
