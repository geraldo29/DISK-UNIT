package DiskUnit;


public class iNodes<E>{
	
	
	public static final int INODESIZE = 9;

	int type;
	int size;
	int firstBlock;
	
	
	
	public iNodes(int t, int s, int fb){
		type = t;
		size = s;
		firstBlock = fb;
	}

	public int getType() {
		return type;
	}
 
	public void setType(int type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFirstBlock() {
		return firstBlock;
	}

	public void setFirstBlock(int firstBlock) {
		this.firstBlock = firstBlock;
	}
	public void firstFreeInode(){
		
		
		
	}
	
	
}