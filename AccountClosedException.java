import java.io.IOException;

public class AccountClosedException extends IOException{
    
    AccountClosedException(){
        super("ERROR: Account is Closed");
    }
    AccountClosedException(int accNum){
        super("ERROR: " + accNum + " is closed");
    }
}
