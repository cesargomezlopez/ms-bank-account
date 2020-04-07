package ms.bank.account.service;

import ms.bank.account.model.BankAccount;
import ms.bank.account.util.ICrud;
import reactor.core.publisher.Flux;

public interface IBankAccountService extends ICrud<BankAccount, String> {

  Flux<BankAccount> findByClientId(String clientId);

}
