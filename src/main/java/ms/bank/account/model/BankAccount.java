package ms.bank.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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

  private Double balance;

  private Integer maxDepositAmount;

  private Integer maxWithdrawalAmount;

  private Double commission;

  @CreatedDate
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date createdDate = new Date();

  @LastModifiedDate
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date lastModifiedDate = new Date();

  @Valid
  @DBRef
  private BankAccountType bankAccountType;

}
