import java.io.IOException;

public class AccountExistsException extends IOException{
    
    AccountExistsException(){
        super("ERROR: Account already Exists");
    }
}
