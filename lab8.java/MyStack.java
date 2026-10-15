import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
 
public class MyStack<T> {
    private final List<T> items = new ArrayList<>();
 
    public void push(T item) { items.add(item); }              // add to the end (the "top")
 
    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        return items.remove(items.size() - 1);                 // remove from the end
    }
 
    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return items.get(items.size() - 1);                    // look, do not remove
    }
 
    public boolean isEmpty() { return items.isEmpty(); }
 
    public static void main(String[] args) {
        MyStack<Integer> s = new MyStack<>();
        s.push(1); s.push(2); s.push(3);
        System.out.println("peek: " + s.peek());
        System.out.println("pop:  " + s.pop());
        System.out.println("pop:  " + s.pop());
        System.out.println("isEmpty? " + s.isEmpty());
        System.out.println("pop:  " + s.pop());
        System.out.println("isEmpty? " + s.isEmpty());
        try {
            s.pop();
        } catch (EmptyStackException e) {
            System.out.println("Cannot pop an empty stack");
        }
 
        MyStack<String> words = new MyStack<>();   // same class, different type
        words.push("a"); words.push("b");
        System.out.println("String stack pop: " + words.pop());
    }
}

