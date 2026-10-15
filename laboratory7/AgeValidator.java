public class AgeValidator {
    static void validateAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 120) {
            throw new InvalidAgeException("Invalid Age:" + age + " " + "must be between 0 and 120.");
        } else {
            System.out.println("age" + age + " is valid.");
        }
    }
    public static void main (String[]args){
        int[]tests = {25, -5, 121 , 0,  120};
        for (int age : tests) {
            try {
                validateAge(age);
            } catch (InvalidAgeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
