package ms.bank.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ms.bank.account.model.BankAccountType;
import ms.bank.account.service.IBankAccountTypeService;
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
@RequestMapping(value = "/bankAccountType")
@Api(value = "bankAccountType")
public class BankAccountTypeController {

	@Autowired
	private IBankAccountTypeService bankAccountTypeService;

	@GetMapping(value = "/findAllBankAccountTypes", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Find all Bank Account Types", notes = "Find all Bank Account Types registered")
	public Mono<ResponseEntity<Flux<BankAccountType>>> findAllBankAccountTypes() {
		return Mono.just(ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(bankAccountTypeService.findAll()))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/findBankAccountTypeById", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Find Bank Account Type by id", notes = "Find Bank Account Type registered by id")
	public Mono<ResponseEntity<BankAccountType>> findBankAccountTypeById(@RequestParam("id")String id) {
		return bankAccountTypeService.findById(id).flatMap(bat -> {
			return Mono.just(ResponseEntity
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(bat));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/createBankAccountType", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creates a Bank Account Type", notes = "Register a new Bank Account Type")
	public Mono<ResponseEntity<BankAccountType>> createBankAcountType(@Valid @RequestBody BankAccountType bankAccountType) {
		return bankAccountTypeService.create(bankAccountType).flatMap(bat -> {
			return Mono.just(ResponseEntity
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(bat));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PutMapping(value = "/updateBankAccountType", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Updates a Bank Account Type", notes = "Modify a Bank Account Type")
	public Mono<ResponseEntity<BankAccountType>> updateBankAcountType(@Valid @RequestBody BankAccountType bankAccountType) {
		return bankAccountTypeService.update(bankAccountType).flatMap(bat -> {
			return Mono.just(ResponseEntity
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(bat));
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(value = "/deleteBankAccountTypeById", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Deletes a Bank Account Type by id", notes = "Removes a Bank Account Type registered by id")
	public Mono<ResponseEntity<Void>> deleteBankAccountById(@RequestParam("id")String id) {
		return bankAccountTypeService.deleteById(id)
				.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
