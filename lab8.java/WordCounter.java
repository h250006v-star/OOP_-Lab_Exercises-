import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordCounter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a paragraph:");
        String paragraph = scanner.nextLine();
        scanner.close();

        // Count each word (lowercase, letters only)
        HashMap<String, Integer> wordCounts = new HashMap<>();
        for (String word : paragraph.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+")) {
            if (!word.isEmpty()) {
                wordCounts.merge(word, 1, Integer::sum);
            }
        }

        // Sort by count (highest first), then alphabetically
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(wordCounts.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));

        System.out.println("Word frequencies:");
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}