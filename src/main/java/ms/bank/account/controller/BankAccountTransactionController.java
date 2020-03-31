package ms.bank.account.controller;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import ms.bank.account.model.BankAccountTransaction;
import ms.bank.account.service.IBankAccountTransactionService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/bankAccountTransaction")
public class BankAccountTransactionController {

  @Autowired
  private IBankAccountTransactionService bankAccountTransactionService;
  
  @GetMapping(value = "/findAllBankAccountTransactions",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find All Bank Account Transactions",
      notes = "Find All Bank Account Transactions registered")
  public Mono<ResponseEntity<Flux<BankAccountTransaction>>> findAllBankAccountTransactions() {
    return Mono.just(ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(bankAccountTransactionService.findAll()))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @GetMapping(value = "findBankAccountTransactionById", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find a Bank Account Transaction by Id",
      notes = "Find a Bank Account Transaction registered")
  public Mono<ResponseEntity<BankAccountTransaction>> 
      findBankAccountTransactionById(@RequestParam("id")String id) {
    return bankAccountTransactionService.findById(id).flatMap(ca -> {
      return Mono.just(ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(ca));
    }).defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @PostMapping(value = "/createBankAccountTransaction", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Creates a Bank Account Transaction",
      notes = "Register a Bank Account Transaction")
  public Mono<ResponseEntity<BankAccountTransaction>>
      createBankAccountTransaction(
        @Valid @RequestBody BankAccountTransaction bankAccountTransaction) {
    return bankAccountTransactionService.create(bankAccountTransaction).flatMap(ca -> {
      return Mono.just(ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(ca));
    }).defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @PutMapping(value = "/updateBankAccountTransaction", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Updates a Bank Account Transaction",
      notes = "Updates a Bank Account Transaction registered")
  public Mono<ResponseEntity<BankAccountTransaction>>
      updateBankAccountTransaction(
        @Valid @RequestBody BankAccountTransaction bankAccountTransaction) {
    return bankAccountTransactionService.update(bankAccountTransaction).flatMap(ca -> {
      return Mono.just(ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(ca));
    }).defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @DeleteMapping(value = "/deleteBankAccountTransactionById", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Deletes a Bank Account Transaction by Id",
      notes = "Removes a Bank Account Transaction registered")
  public Mono<ResponseEntity<Void>>
      deleteBankAccountTransactionById(@RequestParam("id")String id) {
    return bankAccountTransactionService.deleteById(id)
      .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @GetMapping(value = "/findAllBankAccountTransactionsByClientId",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find All Client Bank Account Transactions",
      notes = "Find All Client Bank Account Transactions registered")
  public Mono<ResponseEntity<Flux<BankAccountTransaction>>>
          findAllBankAccountTransactionsByClientId(
            @RequestParam("clientId")String clientId) {
    return Mono.just(ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(bankAccountTransactionService.findByClientId(clientId)))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @GetMapping(value = "/findAllBankAccountTransactionsByBankAccountId",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find All Bank Account Transactions by Bank Account Id",
      notes = "Find All Bank Account Transactions registered by Bank Account Id")
  public Mono<ResponseEntity<Flux<BankAccountTransaction>>>
          findAllBankAccountTransactionsByBankAccountId(
            @RequestParam("bankAccountId")String bankAccountId) {
    return Mono.just(ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(bankAccountTransactionService.findByBankAccountId(bankAccountId)))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @GetMapping(value = "/findAllBankAccountTransactionsByBankAccountIdAndClientId",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Find All Bank Account Transactions by Bank Account Id and Client Id",
      notes = "Find All Bank Account Transactions registered by Bank Account Id and Client Id")
  public Mono<ResponseEntity<Flux<BankAccountTransaction>>>
      findAllBankAccountTransactionsByBankAccountIdAndClientId(
            @RequestParam("bankAccountId")String bankAccountId,
            @RequestParam("clientId")String clientId) {
    return Mono.just(ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(bankAccountTransactionService.findByBankAccountIdAndClientId(bankAccountId, clientId)))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
