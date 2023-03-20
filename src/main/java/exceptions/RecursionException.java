package exceptions;

public class RecursionException extends Exception{
    public RecursionException(String message){
        super(message);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
