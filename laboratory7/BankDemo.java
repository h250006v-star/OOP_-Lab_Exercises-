public class BankDemo {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount("ACC-001", "Tendai", 500.00);
        acc.deposit(250.00);
        System.out.println(acc);
        try {
            acc.withdraw(100.00);
            System.out.println("Withdraw 100: OK");
            acc.withdraw(5000.00);
            System.out.println("Withdraw 5000: OK");        // skipped
        } catch (InsufficientFundsException e) {
            System.out.println("Withdraw 5000 failed: " + e.getMessage());
            System.out.println("Short by: " + e.getShortfall());
        }
        System.out.println(acc);
        try {
            acc.deposit(-50);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}

/*
 * Exercise 4: Trade-offs of throwing an exception vs returning false
 *
 * Throwing an exception (new version)
 *
 * Pros:
 *  - Can't be ignored. InsufficientFundsException is checked, so the compiler
 *    forces the caller to handle it.
 *  - Carries information. It gives a clear message and extra data, such as
 *    getShortfall() ("short by 4350.0").
 *  - Separates error handling from normal logic. The happy path stays in try,
 *    and the failure handling sits in catch.
 *  - Keeps the account safe. The balance only changes after all checks pass,
 *    so the object is never left in a bad state.
 *  - Works across layers. The exception can pass up through several methods
 *    to the one that knows how to handle it.
 *
 * Cons:
 *  - More code. Every call needs try/catch or throws, which adds clutter.
 *  - Heavier for routine failures. If insufficient funds happens all the time,
 *    exceptions are overkill.
 *  - Beginners can misuse it. An empty catch block would hide the failure,
 *    which is worse than before.
 */

