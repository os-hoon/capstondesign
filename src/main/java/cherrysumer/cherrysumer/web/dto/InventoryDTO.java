package cherrysumer.cherrysumer.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "구매날짜을 입력하세요.")
    private LocalDateTime purchase_date;

    private LocalDateTime expiration_date;

    @NotNull(message = "수량을 입력하세요.")
    private Integer quantity;

    private String stockLocation;

    private String category;


}
