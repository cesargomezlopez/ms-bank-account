package ms.bank.account.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "bankAccountType")
public class BankAccountType {

  @Id
  private String id;
  
  @NotEmpty(message = "Bank Account Type code can not be empty")
  private String code;
  
  @NotEmpty(message = "Bank Account Type description can not be empty")
  private String description;

}
