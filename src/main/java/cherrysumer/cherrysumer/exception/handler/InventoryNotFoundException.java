package cherrysumer.cherrysumer.exception.handler;

import cherrysumer.cherrysumer.exception.BaseException;
import cherrysumer.cherrysumer.exception.ErrorCode;

public class InventoryNotFoundException extends BaseException {
    public InventoryNotFoundException() {
        super(ErrorCode._INVENTORY_NOT_FOUND);
    }
}