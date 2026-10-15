import java.util.Arrays;
 
public class BubbleSortDemo {
 
    static <T extends Comparable<T>> void bubbleSort(T[] arr) {
        for (int pass = 0; pass < arr.length - 1; pass++) {
            boolean swapped = false;
            for (int i = 0; i < arr.length - 1 - pass; i++) {
                if (arr[i].compareTo(arr[i + 1]) > 0) {      // out of order?
                    T temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;                              // sorted: stop
        }
    }
 
    public static void main(String[] args) {
        Integer[] nums = {5, 2, 9, 1, 7};
        bubbleSort(nums);
        System.out.println(Arrays.toString(nums));
 
        String[] names = {"pear", "apple", "zebra", "mango"};
        bubbleSort(names);
        System.out.println(Arrays.toString(names));
    }
}
