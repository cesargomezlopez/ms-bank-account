package ms.bank.account.repository;

import ms.bank.account.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IBankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

  Mono<Boolean> existsByClientIdAndBankAccountType_Id(
      String clientId, String bankAccountTypeId);
  
  Flux<BankAccount> findByClientId(String clientId);

}
