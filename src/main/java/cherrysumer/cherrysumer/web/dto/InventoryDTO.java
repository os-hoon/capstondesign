package cherrysumer.cherrysumer.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    @NotEmpty(message = "재고이름을 입력하세요.")
    private String productName;

    private LocalDateTime expiration_date;

    private int quantity;

    private String stockLocation;

    private String category;


}
