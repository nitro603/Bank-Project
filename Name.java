import java.util.Scanner;

public class Name{

    private String first;
    private String last;

    Name(){
        this.first = "";
        this.last = "";
    }
    Name(String firstN, String lastN){
        this.first = firstN;
        this.last = lastN;
    }
    Name(Name a){
        this.first = a.first;
        this.last = a.last;
    }


    public String getFirst(){
        return first;
    }
    public String getLast(){
        return last;
    }
    public void setFirst(String first){
        this.first = first;
    }
    public void setLast(String last){
        this.last = last;
    }
    @Override
    public boolean equals(Object obj){
       if(obj instanceof Name){
           Name other = (Name)obj;
           return other.first.equals(this.first) && other.last.equals(this.last);
       }else{
           return false;
       }
    }

    @Override
    public String toString(){
        return "    " + this.first + "       " + this.last;
    }

    public static Name read(Scanner input){//have to add the null if no input given
        String f = input.next();
        String l = input.next();
        return new Name(f,l);
    }
}