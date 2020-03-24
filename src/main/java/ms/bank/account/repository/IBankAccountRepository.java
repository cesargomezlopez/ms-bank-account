package ms.bank.account.repository;

import ms.bank.account.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

}
