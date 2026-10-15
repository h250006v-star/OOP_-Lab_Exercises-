public class ArrayAccess {
    static void show (int [] arr, int index) {
        try {
            System.out.println ( "arr[" + index + "] = " + arr[index]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println ("Index " + index + " is out of bounds for array of length " + arr.length);
        }
    }

    public static void main (String [] args) {
        int [] arr = {1, 2, 3, 4, 5};
        show (arr, 2);
        show (arr, 5);
    }
}
