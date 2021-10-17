import java.io.IOException;

public class InvalidAmountException extends IOException{
    InvalidAmountException(){
        super("ERROR: Invalid amount");
    }InvalidAmountException(String type){
        super("ERROR: invalid " + type + " amount - Transaction voided");
    }
}
