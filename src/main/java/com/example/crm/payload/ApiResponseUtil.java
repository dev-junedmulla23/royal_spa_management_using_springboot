package com.example.crm.payload;


public class ApiResponseUtil {
    private ApiResponseUtil(){
        //private const does not make object outside the class
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>("success","Operation Successful",data);
    }

    public static <T> ApiResponse<T> created(T data){
        return new ApiResponse<>("success","Record Created Successfully",data);
    }

    public static <T> ApiResponse<T> updated(T data){
        return new ApiResponse<>("success","Record Updated Successfully",data);
    }

    public static <T> ApiResponse<T> deleted(){
        return new ApiResponse<>("success","Record Deleted Successfully", null);
    }

    public static <T> ApiResponse<T> fetched(T data) {
        return new ApiResponse<>("success", "Record Fetched Successfully", data);
    }

    public static <T> ApiResponse<T> fetchedList(T data) {
        return new ApiResponse<>("success", "List Fetched Successfully", data);
    }

    public static <T> ApiResponse<T> fetchedPage(T data) {
        return new ApiResponse<>("success", "Page Fetched Successfully", data);
    }

    public static <T> ApiResponse<?> duplicateRecord(String status, String message,T data){
        return new ApiResponse<>(status,message,data);
    }

    public static <T> ApiResponse<T> notFound(String status, String message,T data){
        return new ApiResponse<>(status,message,data);
    }

    public static <T> ApiResponse<?> genericException(String status,String message,T data){
        return new ApiResponse<>(status,message,data);
    }

    public static <T> ApiResponse<?> runtimeException(String status,String message,T data){
        return new ApiResponse<>(status,message,data);
    }

    public static <T> ApiResponse<?> validationException(String status,String message,T data){
        return new ApiResponse<>(status,message,data);
    }

    public static <T> ApiResponse<?> resourceInUseException(String status,String message,T data){
        return new ApiResponse<>(status, message, data);
    }
}
