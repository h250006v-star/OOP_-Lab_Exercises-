import java.util.InputMismatchException;
import java.util.Scanner;

public class DivideInput {
    static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: please enter whole numbers only.");
                sc.nextLine();          // throw away the bad text, then ask again
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = readInt(sc, "Enter first integer: ");
        int b = readInt(sc, "Enter second integer: ");
        try {
            System.out.println(a + " / " + b + " = " + (a / b));
        } catch (ArithmeticException e) {
            System.out.println("Error: cannot divide by zero.");
        } finally {
            sc.close();
        }
    }
}