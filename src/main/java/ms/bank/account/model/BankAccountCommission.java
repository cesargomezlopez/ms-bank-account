package ms.bank.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bankAccountCommission")
public class BankAccountCommission {

  @Id
  private String id;

  @NotEmpty(message = "Commission Bank Account Id can not be empty")
  private String bankAccountId;

  @Min(value = 0L, message = "Bank Account Commision Amount can not be negative")
  private Double amount;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date registerDate;
  
  public BankAccountCommission(String bankAccountId, Double amount, Date registerDate) {
    this.bankAccountId = bankAccountId;
    this.amount = amount;
    this.registerDate = registerDate;
  }

}
