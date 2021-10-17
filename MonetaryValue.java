import java.util.Scanner;

public class MonetaryValue{

    private int cents;

    MonetaryValue(){
        this.cents = 0;
    }
    MonetaryValue(double money){
        this.cents = (int)(money * 100);
    }

    @Override
    public String toString(){
        if(cents < 0){
            this.cents = Math.abs(cents);
            return "$-" + cents/100 + "." + cents%100;
        }else{
            return "$" + cents/100 + "." + cents%100;
        }
    }

    public boolean isLessThan(MonetaryValue other){
        return  this.cents < other.getCents();
    }
    public boolean isGreaterThan(MonetaryValue other){
        return  this.cents > other.getCents();
    }

    public int getCents() {
        return cents;
    }
    
    public boolean isNegative(){
        return cents < 0;
    }

    public double doubleValue(){
        return (double)cents/100;
    }

    public boolean add(MonetaryValue other){
        if(other.cents < 0){
            return false;
        }else{
            this.cents += other.cents;
            return true;
        }
    }

    public boolean subtract(MonetaryValue other){
        if(other.cents < 0){
            return false;
        }else{
            this.cents -= other.cents;
            return true;
        }
    }
    public static MonetaryValue read(Scanner stdin){
        //add null if no input is given
        double money = stdin.nextDouble();
        return new MonetaryValue(money);
    }
}
    