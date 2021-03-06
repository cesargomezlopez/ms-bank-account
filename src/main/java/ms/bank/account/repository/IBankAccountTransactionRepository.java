package ms.bank.account.repository;

import ms.bank.account.model.BankAccountTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IBankAccountTransactionRepository
    extends ReactiveMongoRepository<BankAccountTransaction, String> {

  Flux<BankAccountTransaction> findByBankAccountId(String bankAccountId);

}
