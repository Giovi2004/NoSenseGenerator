package nosensegenerator.nosense;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Object metadata;

    public ApiResponse(String status, String message, T data, Object metadata) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.metadata = metadata;
    }

    // Constructor overload for simpler responses without metadata
    public ApiResponse(String status, String message, T data) {
        this(status, message, data, null);
    }

    // Constructor for success responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", "Operation completed successfully", data);
    }

    // Constructor for error responses
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
} 