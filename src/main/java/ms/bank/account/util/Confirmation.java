package ms.bank.account.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Confirmation {
  private Integer status;
  private String message;
}
