package ms.bank.account.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ms.bank.account.model.BankAccountTransaction;
import ms.bank.account.repository.IBankAccountRepository;
import ms.bank.account.repository.IBankAccountTransactionRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountTransactionServiceImpl implements IBankAccountTransactionService {

  @Autowired
  private IBankAccountTransactionRepository bankAccountTransactionRepository;

  @Autowired
  private IBankAccountRepository bankAccountRepository;

  @Override
  public Flux<BankAccountTransaction> findAll() {
    return bankAccountTransactionRepository.findAll();
  }

  @Override
  public Mono<BankAccountTransaction> findById(String id) {
    return bankAccountTransactionRepository.findById(id)
        .switchIfEmpty(Mono.empty());
  }

  @Override
  public Mono<BankAccountTransaction> create(BankAccountTransaction entity) {
    try {
      return getClientByIdFromApi(entity.getClientId()).flatMap(cl -> {
        return bankAccountRepository.findById(entity.getBankAccountId()).flatMap(ba -> {
          ba.setBalance(ba.getBalance() + entity.getAmount());
          if (ba.getBalance() >= 0) {
            bankAccountRepository.save(ba).subscribe();
            entity.setRegisterDate(new Date());
            return bankAccountTransactionRepository.save(entity);
          } else {
            return Mono.error(new Exception("Bank Account doesn´t have sufficient balance"));
          }
        }).switchIfEmpty(Mono.error(new Exception("Bank Account not found")));
      }).switchIfEmpty(Mono.error(new Exception("Client not found")));
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Mono<BankAccountTransaction> update(BankAccountTransaction entity) {
    try {
      return bankAccountTransactionRepository.findById(entity.getId()).flatMap(bat -> {
        return getClientByIdFromApi(entity.getClientId()).flatMap(cl -> {
          return bankAccountRepository.findById(entity.getBankAccountId()).flatMap(ba -> {
            ba.setBalance(ba.getBalance() + entity.getAmount());
            if (ba.getBalance() >= 0) {
              bankAccountRepository.save(ba).subscribe();
              return bankAccountTransactionRepository.save(entity);
            } else {
              return Mono.error(new Exception("Bank Account doesn´t have sufficient balance"));
            }
          }).switchIfEmpty(Mono.error(new Exception("Bank Account not found")));
        }).switchIfEmpty(Mono.error(new Exception("Client not found")));
      }).switchIfEmpty(Mono.error(new Exception("Bank Account Transaction not found")));
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Mono<Void> deleteById(String id) {
    try {
      return bankAccountTransactionRepository.findById(id).flatMap(bat -> {
        return bankAccountTransactionRepository.delete(bat);
      });
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

  @Override
  public Flux<BankAccountTransaction> findByClientId(String clientId) {
    try {
      return getClientByIdFromApi(clientId).flatMapMany(cl -> {
        JsonParser parser = new JsonParser();
        JsonObject client = parser.parse(cl).getAsJsonObject();
        String id = client.get("id").getAsString();
        
        if (id != null) {
          return bankAccountTransactionRepository.findByClientId(clientId);
        } else {
          return Flux.error(new Exception("Client not found"));
        }
      });
    } catch (Exception e) {
      return Flux.error(e);
    }
  }

  @Override
  public Flux<BankAccountTransaction> findByBankAccountId(String bankAccountId) {
    try {
      return bankAccountRepository.findById(bankAccountId).flatMapMany(ba -> {
        return bankAccountTransactionRepository.findByBankAccountId(bankAccountId);
      }).switchIfEmpty(Flux.error(new Exception("Bank Account not found")));
    } catch (Exception e) {
      return Flux.error(e);
    }
  }

  @Override
  public Flux<BankAccountTransaction>
      findByBankAccountIdAndClientId(String bankAccountId, String clientId) {
    try {
      return getClientByIdFromApi(clientId).flatMapMany(cl -> {
        JsonParser parser = new JsonParser();
        JsonObject client = parser.parse(cl).getAsJsonObject();
        String id = client.get("id").getAsString();
        
        if (id != null) {
          return bankAccountRepository.findById(bankAccountId).flatMapMany(ba -> {
            return bankAccountTransactionRepository
              .findByBankAccountIdAndClientId(bankAccountId, clientId);
          }).switchIfEmpty(Flux.error(new Exception("Bank Account not found")));
        } else {
          return Flux.error(new Exception("Client not found"));
        }
      });
    } catch (Exception e) {
      return Flux.error(e);
    }
  }

  public Mono<String> getClientByIdFromApi(String clientId) {
    try {
      String url = "http://localhost:8001/client/findClientById?id=" + clientId;
      return WebClient.create()
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class);	
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

}
