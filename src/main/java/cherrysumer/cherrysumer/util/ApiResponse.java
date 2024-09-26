package cherrysumer.cherrysumer.util;

import cherrysumer.cherrysumer.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private Boolean isSuccess;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiResponse<T> onSuccess(T data){
        return new ApiResponse<>(true, "200", "요청이 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> onSuccess(String message){
        return new ApiResponse<>(true, "200", message, null);
    }

    public static <T> ApiResponse<T> onFailure(ErrorCode code, T data){
        return new ApiResponse<>(false, code.getCode(), code.getMessage(), data);
    }
}
