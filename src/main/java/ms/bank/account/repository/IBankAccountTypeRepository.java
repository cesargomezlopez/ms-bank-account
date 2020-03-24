package ms.bank.account.repository;

import ms.bank.account.model.BankAccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankAccountTypeRepository extends ReactiveMongoRepository<BankAccountType, String>{

}
