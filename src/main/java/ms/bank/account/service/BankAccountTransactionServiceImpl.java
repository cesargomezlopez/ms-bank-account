package ms.bank.account.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.SimpleDateFormat;
import java.util.Date;
import ms.bank.account.model.BankAccountCommission;
import ms.bank.account.model.BankAccountTransaction;
import ms.bank.account.repository.IBankAccountCommissionRepository;
import ms.bank.account.repository.IBankAccountRepository;
import ms.bank.account.repository.IBankAccountTransactionRepository;
import ms.bank.account.util.Confirmation;
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

  @Autowired
  private IBankAccountCommissionRepository bankAccountCommissionRepository;

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
          
          if (ba.getNumTransactions() >= ba.getMaxNumTransactions()) {
            ba.setBalance(ba.getBalance() - ba.getCommission());
          }
          
          if (ba.getBalance() >= 0) {
            ba.setNumTransactions(ba.getNumTransactions() + 1);
            bankAccountRepository.save(ba).subscribe();
            if (ba.getNumTransactions() > ba.getMaxNumTransactions()) {
              BankAccountCommission bco =
                  new BankAccountCommission(ba.getId(), ba.getCommission(), new Date());
              bankAccountCommissionRepository.save(bco).subscribe();
            }
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

  @Override
  public Flux<BankAccountCommission> getCommissionReport(String startDate, String endDate) {
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Date startD = format.parse(startDate);
      Date endD = format.parse(endDate);
      
      return bankAccountCommissionRepository.findAllByRegisterDateBetween(startD, endD);
    } catch (Exception e) {
      return Flux.error(e);
    }
  }
  
  @Override
  public Mono<Confirmation>
      payCreditAccountDebt(String bankAccountId, String creditAccountId) {      
    try {
      return bankAccountRepository.findById(bankAccountId).flatMap(ba -> {
        return getCreditAccountDebt(creditAccountId).flatMap(debt -> {
   
          ba.setBalance(ba.getBalance() - debt);
          
          if (ba.getNumTransactions() >= ba.getMaxNumTransactions()) {
            ba.setBalance(ba.getBalance() - ba.getCommission());
          }
          
          if (ba.getBalance() >= 0) {
            ba.setNumTransactions(ba.getNumTransactions() + 1);
            bankAccountRepository.save(ba).subscribe();
            
            if (ba.getNumTransactions() > ba.getMaxNumTransactions()) {
              BankAccountCommission bco =
                  new BankAccountCommission(ba.getId(), ba.getCommission(), new Date());
              bankAccountCommissionRepository.save(bco).subscribe();
            }
            BankAccountTransaction batt = new BankAccountTransaction();
            batt.setCode("PCAD" + creditAccountId);
            batt.setAmount(-debt);
            batt.setBankAccountId(bankAccountId);
            batt.setClientId(ba.getClientId());
            batt.setRegisterDate(new Date());
            bankAccountTransactionRepository.save(batt).subscribe();
            
            return payCreditAccountDebt(creditAccountId).flatMap(rpt -> {
              if (rpt == 1) {
                return Mono.just(new Confirmation(1, "Ok, Credit Account Debt paid"));
              }  else {
                return Mono.just(new Confirmation(-1, "An error was found when"
                    + "get the credit account debt"));
              }
            });

          } else {
            return Mono.just(new Confirmation(0,
              "The bank account doesn´t have sufficient balance to pay credit account debt"));
          }
        }).switchIfEmpty(Mono.just(new Confirmation(-1,
              "An error was found when get the credit account debt")));
      }).switchIfEmpty(Mono.error(new Exception("Bank Account not found"))); 
    } catch (Exception e) {
      return Mono.error(e);
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
  
  public Mono<Double> getCreditAccountDebt(String creditAccountId) {
    try {
      String url = "http://localhost:8003/creditAccount/getDebt?creditAccountId=" + creditAccountId;
      return WebClient.create()
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(Double.class);	
    } catch (Exception e) {
      return Mono.error(e);
    }
  }
  
  public Mono<Integer> payCreditAccountDebt(String creditAccountId) {
    try {
      String url = "http://localhost:8003/creditAccount/payDebt?creditAccountId=" + creditAccountId;
      return WebClient.create()
        .post()
        .uri(url)
        .retrieve()
        .bodyToMono(Integer.class);	
    } catch (Exception e) {
      return Mono.error(e);
    }
  }

}
