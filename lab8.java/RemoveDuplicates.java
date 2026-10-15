import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
 
public class RemoveDuplicates {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of(
            "Tino", "Rufaro", "Nomsa", "Tino", "Kuda", "Rufaro", "Ada", "Tino"));
        System.out.println("Original: " + names + " (size " + names.size() + ")");
 
        // LinkedHashSet: removes duplicates AND keeps the original order
        Set<String> unique = new LinkedHashSet<>(names);
        List<String> cleaned = new ArrayList<>(unique);
        System.out.println("Unique:   " + cleaned + " (size " + cleaned.size() + ")");
    }
}
