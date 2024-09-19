package cherrysumer.cherrysumer.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class InventoryDTO {
    private Long id;
    private String productName;
    private LocalDateTime expirationDate;
    private int quantity;
    private String stockLocation;
    private List<Long> category;
    private List<String> detailedCategory;
}
