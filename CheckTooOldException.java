import java.io.IOException;

public class CheckTooOldException extends IOException{
    
    CheckTooOldException(){
        super("ERROR: Check is too old");
    }
}