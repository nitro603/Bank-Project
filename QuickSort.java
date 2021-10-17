import java.util.ArrayList;


public class QuickSort {
   
    private static ArrayList<Account> accounts;

    QuickSort(ArrayList<Account> accountslist){
        accounts = new ArrayList<Account>(accountslist);
    }

    static void swap(Integer[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static int partition(Integer[] arr, int low, int high)
    {   

        // pivot
        int pivotACCNUM = accounts.get(arr[high]).getAccNum(); 
        
        // Index of smaller element and
        // indicates the right position
        // of pivot found so far
        int i = (low - 1); 
    
        for(int j = low; j <= high - 1; j++)
        {
            
            // If current element is smaller 
            // than the pivot
            if (accounts.get(arr[j]).getAccNum() < pivotACCNUM)
            {
                // Increment index of 
                // smaller element
                i++; 
                //then swap
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    public void Sort(Integer[] arr, int low, int high)
    {
        if (low < high) 
        {
            // pi is partitioning index, arr[p]
            // is now at right place 
            int pi = partition(arr, low, high);
    
            // Separately sort elements before
            // partition and after partition
            Sort(arr, low, pi - 1);
            Sort(arr, pi + 1, high);
        }
    }



}
