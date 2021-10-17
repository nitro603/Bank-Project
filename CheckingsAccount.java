
public class CheckingsAccount extends Account{

    CheckingsAccount(){
        super();
    }
    CheckingsAccount(Account object){
        super(object);

    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof CheckingsAccount){
            CheckingsAccount other = (CheckingsAccount)obj;
            return other.getDepositor().equals(this.getDepositor()) && other.getAccNum() == this.getAccNum() 
            && other.getAccBalance() == this.getAccBalance() && other.getAcctType() == this.getAcctType();
        }else{
            return false;
        }
    }
}