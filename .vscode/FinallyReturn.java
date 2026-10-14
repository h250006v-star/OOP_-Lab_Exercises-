public class FinallyReturn {
    static int test() {
        try {
            System.out.println("in try");
            return 1;
        } finally {
            System.out.println("in finally");
        }
    }
    // Extra: what if finally ALSO returns? (bad practice - it overrides!)
    @SuppressWarnings("finally")
    static int override() {
        try {
            return 1;
        } finally {
            return 2;
        }
    }
    // Extra: the returned value is fixed BEFORE finally runs
    static int fixedValue() {
        int x = 10;
        try {
            return x;
        } finally {
            x = 99;
            System.out.println("finally changed x to " + x);
        }
    }
    public static void main(String[] args) {
        System.out.println("test() returned " + test());
        System.out.println("override() returned " + override());
        System.out.println("fixedValue() returned " + fixedValue());
    }
}

