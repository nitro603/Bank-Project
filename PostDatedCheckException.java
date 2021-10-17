import java.io.IOException;

public class PostDatedCheckException extends IOException{
    
    PostDatedCheckException(){
        super("ERROR: Post Dated Check");
    }
}
