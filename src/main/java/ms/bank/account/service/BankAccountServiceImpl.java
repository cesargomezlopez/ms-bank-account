package ms.bank.account.service;

import ms.bank.account.model.BankAccount;
import ms.bank.account.repository.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    try {
      return getClientByIdFromApi(entity.getClientId()).flatMap(cl -> {
        JsonParser parser = new JsonParser();
        JsonObject client = parser.parse(cl).getAsJsonObject();
        JsonObject clientType = client.get("clientType").getAsJsonObject();
        String clientId = client.get("id").getAsString();
        String clientTypeCode = clientType.get("code").getAsString();
        return existBankAccountByClientIdAndBankAccountTypeId(
          clientId, entity.getBankAccountType().getId()).flatMap(rs -> {
            if(rs && clientTypeCode.equals("01")) {
              return Mono.error(new Exception(
                "Personal Client Type can not has more than 1 '"
                + entity.getBankAccountType().getDescription() + "' Type Bank Account"));
            } else {
              return bankAccountRepository.save(entity);
            }
          });
      }).switchIfEmpty(Mono.error(new Exception("Client not found")));
    } catch (Exception e) {
      return Mono.error(e);  
    }
  }

  @Override
  public Mono<BankAccount> update(BankAccount entity) {
    try {
      return getClientByIdFromApi(entity.getClientId()).flatMap(cl -> {
        return bankAccountRepository.findById(entity.getId()).flatMap(ba -> {
          return bankAccountRepository.save(entity);
        }).switchIfEmpty(Mono.error(new Exception("Bank Account not found")));
      }).switchIfEmpty(Mono.error(new Exception("Client not found")));
    } catch (Exception e) {
      return Mono.error(e);  
    }
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return bankAccountRepository.findById(id).flatMap(ba -> {
      return bankAccountRepository.delete(ba);
    }).switchIfEmpty(Mono.empty());
  }
  
  public Mono<Boolean> existBankAccountByClientIdAndBankAccountTypeId(
      String clientId, String bankAccountTypeId) {
    return bankAccountRepository
      .existsByClientIdAndBankAccountType_Id(clientId, bankAccountTypeId);
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
