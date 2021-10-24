import java.util.ArrayList;

public class BinarySearch {
    
    private ArrayList<Account> accounts;

    BinarySearch(ArrayList<Account> accountslist){
        accounts = new ArrayList<Account>(accountslist);
    }

    public int search(Integer arr[], int left, int right, int accNum){

        if(left < right){
            int mid = (left + right)/2;
            int leftright = right - mid;

            if(accounts.get(arr[mid]).getAccNum() == accNum){
                return arr[mid];//once found returns index BASE CASE
            }else if(leftright == 1){
                if(accounts.get(arr[right]).getAccNum() == accNum){
                    return arr[right];
                }else if(accounts.get(arr[left]).getAccNum() == accNum){
                    return arr[left];
                }
            }else if(accounts.get(arr[mid]).getAccNum() > accNum){//searching the left side
                return search(arr, left, mid - 1, accNum);
            }else if(accounts.get(arr[mid]).getAccNum() < accNum){//searching the right side
                return search(arr, mid + 1, right, accNum);
            }
        }
        return -1;//BASE CASE
    }

}
