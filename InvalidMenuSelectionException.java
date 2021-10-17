import java.io.IOException;

public class InvalidMenuSelectionException extends IOException{

    InvalidMenuSelectionException(){
        super("ERROR: Invalid menu selection");
    }

    InvalidMenuSelectionException(char choice){
        super("ERROR: " + choice + " is an Invalid menu selection");
    }
}