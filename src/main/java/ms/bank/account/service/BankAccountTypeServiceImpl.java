package ms.bank.account.service;

import ms.bank.account.model.BankAccountType;
import ms.bank.account.repository.IBankAccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountTypeServiceImpl implements IBankAccountTypeService {

	@Autowired
	private IBankAccountTypeRepository bankAccountTypeRepository;

	@Override
	public Flux<BankAccountType> findAll() {
		return bankAccountTypeRepository.findAll();
	}

	@Override
	public Mono<BankAccountType> findById(String id) {
		return bankAccountTypeRepository.findById(id)
				.switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<BankAccountType> create(BankAccountType entity) {
		return bankAccountTypeRepository.save(entity)
				.switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<BankAccountType> update(BankAccountType entity) {
		return bankAccountTypeRepository.findById(entity.getId()).flatMap(bat -> {
			return bankAccountTypeRepository.save(entity);
		}).switchIfEmpty(Mono.empty());
	}

	@Override
	public Mono<Void> deleteById(String id) {
		return bankAccountTypeRepository.findById(id).flatMap(bat -> {
			return bankAccountTypeRepository.delete(bat);
		}).switchIfEmpty(Mono.empty());
	}

}
