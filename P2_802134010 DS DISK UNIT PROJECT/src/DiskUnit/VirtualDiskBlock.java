package DiskUnit;

public class VirtualDiskBlock {
	
	  private final static int CAPACITY = 256; // default number of bytes per block
	
	  int capacity;
	  byte[] block;

	  
	  
	
	/**
	 * creates a block of size equal to 256 bytes.
	 */
	
	public VirtualDiskBlock(){
		capacity = CAPACITY;
		block = new byte [CAPACITY];
	}
	/**
	 * creates a nblock of size equal to n bytes
	 * @param blockSize the capacity 
	 */
	public VirtualDiskBlock(int blockSize) {
		// TODO Auto-generated constructor stub
		capacity = blockSize;
		block = new byte [capacity];
	
	}
	
	
	/**
	 * method to return the capacity of a block
	 * @return capacity
	 */
	public int getCapacity() {
		// TODO Auto-generated method stub
		return capacity;
	}

	/**
	 * gets the element at a specific index
	 * @param i index 
	 * @return the element in the index position 
	 */
	public byte getElement(int i) {
		
		return block[i];
		
	}

	/**
	 * sets an element in a specific position 
	 * @param i index to set the element 
	 * @param b element that you will set 
	 */
	public void setElement(int i, byte b) {
		// TODO Auto-generated method stub
			block[i]= b;
	}
	


}