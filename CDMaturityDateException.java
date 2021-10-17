import java.io.IOException;


public class CDMaturityDateException extends IOException{

    CDMaturityDateException(){
        super("ERROR: Maturity Date not Reached");
    }
    
}
