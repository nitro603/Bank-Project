import java.util.Scanner;

public class Depositor {
    private Name FirstLast;
    private int SSN;

    Depositor(){
        this.FirstLast = new Name();
        this.SSN = 0;
    }

    Depositor(Name fl, int ssn){
        this.FirstLast = fl;
        this.SSN = ssn;
    }
    Depositor(Depositor other){

    }

    public Name getName(){
        return FirstLast;
    }
    public int getSSN(){
        return SSN;
    }

    public void setName(Name name){
        this.FirstLast = name;
    }
    public void setSSN(int social){
        this.SSN = social;
    }

    @Override
    public String toString(){
        return FirstLast + "         " + SSN;
    }
    @Override
    public boolean equals(Object obj){
       if(obj instanceof Depositor){
           Depositor other = (Depositor)obj;
           return other.getName().toString().equals(FirstLast.toString()) && this.SSN == other.getSSN();
       }else{
           return false;
       }
    }

    
    public static Depositor readScan(Scanner fileScan){

        Name ScannedName = Name.read(fileScan);
        int Social = fileScan.nextInt();

        return new Depositor(ScannedName, Social);
    }

}
