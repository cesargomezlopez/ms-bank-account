package ms.bank.account.repository;

import java.util.Date;
import ms.bank.account.model.BankAccountCommission;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBankAccountCommissionRepository
    extends ReactiveMongoRepository<BankAccountCommission, String> {

  Flux<BankAccountCommission> findAllByRegisterDateBetween(Date startDate, Date endDate);

}
