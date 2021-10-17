import java.util.Calendar;

public class Check {
    private int accNum;
    private MonetaryValue checkAmount;
    private Calendar dateOfCheck;
    
    Check(){
        accNum = 0;
        checkAmount = new MonetaryValue();
        dateOfCheck = Calendar.getInstance();
    }
    Check(int accNum, MonetaryValue checkAmount, Calendar date){
        this.accNum = accNum;
        this.checkAmount = checkAmount;
        dateOfCheck = date;
    }
    Check(Check other){
        this.accNum = other.getAccNum();
        this.checkAmount = other.getCheckAmount();
        this.dateOfCheck = other.getDateofCheck();
    }

    public void checkDate(){
        System.out.println(this.dateOfCheck.getTime());
    }

    public int getAccNum(){
        return accNum;
    }
    public MonetaryValue getCheckAmount(){
        return checkAmount;
    }
    public Calendar getDateofCheck(){
        return dateOfCheck;
    }

    @Override
    public String toString(){
        return "Account Number: " + accNum + "\nCheck Amount: " + checkAmount.toString() + "\nDate: " + Bank.dateToString(dateOfCheck);
     }
    
}
