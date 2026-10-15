import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
public class ListStats {
    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>();
        int[] values = {12, 7, 25, 3, 18, 9, 31, 14, 6, 20};
        for (int v : values) nums.add(v);          // autoboxing int -> Integer
 
        int sum = 0;
        int max = nums.get(0);
        for (int n : nums) {                       // unboxing Integer -> int
            sum += n;
            if (n > max) max = n;
        }
 
        System.out.println("List:    " + nums);
        System.out.println("Sum:     " + sum);
        System.out.println("Maximum: " + max);
 
        List<Integer> reversed = new ArrayList<>(nums);   // copy, so the original is untouched
        Collections.reverse(reversed);
        System.out.println("Reverse: " + reversed);
    }
}
