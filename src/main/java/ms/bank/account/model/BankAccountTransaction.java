package ms.bank.account.model;

import java.util.Date;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bankAccountTransaction")
public class BankAccountTransaction {
  
  @Id
  @Generated
  private String id;
  
  @NotEmpty(message = "Bank Account Transaction Code can not be empty")
  private String code;
  
  private Double amount;
  
  @NotEmpty(message = "Bank Account Transaction Bank Account Id can not be empty")
  private String bankAccountId;
  
  private Date registerDate;
}
