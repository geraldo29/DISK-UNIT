package lists;
/**
 * linked list
 * @author Pedro I river
 *
 * @param <E>
 */
public interface LinkedList<E> {
	
	int length(); 

	/**
	 * 
	 * @param target
	 * @return
	 * @throws NodeOutOfBoundsException
	 */
	Node<E> getNodeBefore(Node<E> target) throws NodeOutOfBoundsException; 
	
	/**
	 * 
	 * @param target
	 * @return
	 * @throws INodeOutOfBoundsException
	 */
	Node<E> getNodeAfter(Node<E> target) throws NodeOutOfBoundsException; 
	
	/**
	 *  @return reference to the first node of the linked list
	 *  @throws INodeOutOfBoundsException if the linked list is empty
	 */
	Node<E> getFirstNode() throws NodeOutOfBoundsException; 
	
	/**
	 * 
	 * @return
	 * @throws INodeOutOfBoundsException
	 */
	Node<E> getLastNode() throws NodeOutOfBoundsException; 
	
	/**
	 * 
	 * @param nuevo
	 */
	void addFirstNode(Node<E> nuevo); 
	
	/**
	 * 
	 * @param target
	 * @param nuevo
	 */
	void addNodeAfter(Node<E> target, Node<E> nuevo); 
	
	/**
	 * 
	 * @param target
	 * @param nuevo
	 */
	void addNodeBefore(Node<E> target, Node<E> nuevo); 
	
	/**
	 * 
	 * @return
	 * @throws INodeOutOfBoundsException
	 */
	Node<E> removeFirstNode() throws NodeOutOfBoundsException; 
	
	/**
	 * 
	 * @return
	 * @throws INodeOutOfBoundsException
	 */
	Node<E> removeLastNode() throws NodeOutOfBoundsException; 
	
	/**
	 * 
	 * @param target
	 */
	void removeNode(Node<E> target); 
	
	/**
	 * 
	 * @param target
	 * @return
	 * @throws INodeOutOfBoundsException
	 */
	Node<E> removeNodeAfter(Node<E> target) throws NodeOutOfBoundsException; 
	
	/**
	 * 
	 * @param target
	 * @return
	 * @throws INodeOutOfBoundsException
	 */
	Node<E> removeNodeBefore(Node<E> target) throws NodeOutOfBoundsException; 
	
	/**
	 * Creates a new node instance of the type of nodes that the linked list
	 * uses. The new node will have all its instance fields initialized to
	 * null. The new node is not linked to the list in any way.
	 * @return reference to the new node instance. 
	 */
	Node<E> createNewNode(); 

}
