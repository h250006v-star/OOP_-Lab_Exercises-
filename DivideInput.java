import java.util.InputMismatchException;
import java.util.Scanner;

public class DivideInput {
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the first number: ");
            int num1 = sc.nextInt();
            System.out.print("Enter the second number: ");
            int num2 = sc.nextInt();
            System.out.println(num1 + " / " + num2 + " = " + (num1 / num2));

        } catch (InputMismatchException e) {
            System.out.println("Please enter valid integers.");
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero.");
        }
        finally {
            sc.close();
        }
    }
}