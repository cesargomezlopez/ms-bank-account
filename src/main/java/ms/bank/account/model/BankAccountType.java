package ms.bank.account.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

  @Min(value = 0L, message = "Bank Account Type Minimum Opening Balance can not be negative")
  private Double minOpeningBalance;

  @Min(value = 0L, message = "Bank Account Type Minimum End Month Balance can not be negative")
  private Double minEndMonthBalance;

}
