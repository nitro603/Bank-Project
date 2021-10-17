import java.io.IOException;

public class InsufficientFundsException extends IOException{
    
    InsufficientFundsException(){
        super("ERROR: Insufficient Funds");
    }
    InsufficientFundsException(Double amount){
        super("ERROR: Insufficient Funds for " + amount);
    }InsufficientFundsException(Check object){
        super("ERROR: Insufficient Funds - Bounce Fee Charged");
    }
}
