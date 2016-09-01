package stack;

/**
 * stack interface
 * @author 4035a5
 *
 * @param <E>
 */
public interface Stack<E> {
	public void push(E element);
	public int pop();
	public int top();
	int size();
	boolean isEmpty();
}
