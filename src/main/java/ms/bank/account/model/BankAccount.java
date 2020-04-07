package ms.bank.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "bankAccount")
public class BankAccount {

  @Id
  private String id;

  @NotEmpty(message = "Bank Account code can not be empty")
  private String code;

  @NotEmpty(message = "Bank Account Transaction Client Id can not be empty")
  private String clientId;

  @Min(value = 0L, message = "Bank Account Balance can not be negative")
  private Double balance;

  @Min(value = 0L, message = "Bank Account Maximum Deposit Amount can not be negative")
  private Double maxDepositAmount;

  @Min(value = 0L, message = "Bank Account Maximum Withdrawal Amount can not be negative")
  private Double maxWithdrawalAmount;

  @Min(value = 0, message = "Bank Account Maximun Number of Transactions can not be negative")
  private Integer maxNumTransactions;

  @Min(value = 0, message = "Bank Account Number of Transactions can not be negative")
  private Integer numTransactions;

  @Min(value = 0L, message = "Bank Account Commission can not be negative")
  private Double commission;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createdDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date lastModifiedDate;

  @Valid
  @DBRef
  private BankAccountType bankAccountType;

}
