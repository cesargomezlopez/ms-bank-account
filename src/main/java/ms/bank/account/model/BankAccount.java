package ms.bank.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.Valid;
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

  @NotEmpty(message = "Bank Account description can not be empty")
  private String description;

  private Double balance;

  private Integer maxDepositAmount;

  private Integer maxWithdrawalAmount;

  private Double commission;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @Valid
  @DBRef
  private BankAccountType bankAccountType;

}
