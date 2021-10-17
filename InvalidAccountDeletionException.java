import java.io.IOException;

public class InvalidAccountDeletionException extends IOException {
    
    InvalidAccountDeletionException(){
        super("ERROR: Account has non-zero balance");
    }
}
