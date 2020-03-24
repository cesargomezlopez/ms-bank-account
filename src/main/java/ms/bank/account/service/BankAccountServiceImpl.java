package ms.bank.account.service;

import ms.bank.account.model.BankAccount;
import ms.bank.account.repository.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountServiceImpl implements IBankAccountService {

	@Autowired
	private IBankAccountRepository bankAccountRepository;

	@Override
	public Flux<BankAccount> findAll() {
		return bankAccountRepository.findAll();
	}

	@Override
	public Mono<BankAccount> findById(String id) {
		return bankAccountRepository.findById(id)
				.switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<BankAccount> create(BankAccount entity) {
		return bankAccountRepository.save(entity)
				.switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<BankAccount> update(BankAccount entity) {
		return bankAccountRepository.findById(entity.getId()).flatMap(ba -> {
			return bankAccountRepository.save(entity);
		}).switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<Void> deleteById(String id) {
		return bankAccountRepository.findById(id).flatMap(ba -> {
			return bankAccountRepository.delete(ba);
		}).switchIfEmpty(Mono.empty());
	}

}
