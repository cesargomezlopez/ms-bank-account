package ms.bank.account.service;

import ms.bank.account.model.BankAccountCommission;
import ms.bank.account.model.BankAccountTransaction;
import ms.bank.account.util.Confirmation;
import ms.bank.account.util.ICrud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountTransactionService extends ICrud<BankAccountTransaction, String> {

  Flux<BankAccountTransaction> findByBankAccountId(String bankAccountId);

  Flux<BankAccountCommission> getCommissionReport(String startDate, String endDate);
  
  Mono<Confirmation>
      payCreditAccountDebt(String bankAccountId, String creditAccountId, Double amount);

}
