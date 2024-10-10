package cherrysumer.cherrysumer.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @NotEmpty(message = "구매날짜을 입력하세요.")
    private LocalDateTime purchase_date;

    @NotEmpty(message = "만료날짜을 입력하세요.")
    /*@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")*/
    private LocalDateTime expiration_date;

    @NotEmpty(message = "수량을 입력하세요.")
    private int quantity;

    private String stockLocation;

    private String category;


}
