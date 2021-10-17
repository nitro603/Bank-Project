import java.io.IOException;

public class InvalidAccountException extends IOException{
    InvalidAccountException(){
        super("ERROR: Invalid Account");
    }
    InvalidAccountException(int identifier, String a){
        super("ERROR: " + a + identifier + " does not exist");
    }
}
