package DiskUnit;

import exceptions.InvalidBlockException;
import exceptions.InvalidBlockNumberException;

public class InodesManager {

	public static byte [] inodebytes;
	private int firstFreeInode;
	
	public InodesManager(DiskUnit disk){
		//load the disk 
	}
	
	/**
	 * Removes the first free inode from the list
	 * @return free inode 
	 */
	public int popFreeInode(){
		if(firstFreeInode!=0){
			int vtr =  firstFreeInode;
			firstFreeInode = Utils.getIntFromBytesArray(inodebytes, firstFreeInode*9);
			Utils.copyIntToBytesArray(inodebytes, vtr* 9, 0);
			return vtr;
		}
		return 0;
	}
	
	public static void writeToDisk(DiskUnit du, byte[] inodesBytes){
		int blockSize =du.getBlockSize();
		int numberofInodeBlocks = inodesBytes.length / blockSize;
		if(inodesBytes.length % blockSize != 0)
			numberofInodeBlocks++;
		
		VirtualDiskBlock vlb = new VirtualDiskBlock(blockSize);
		
		int bytesArrayIndex = 0;
		for(int blockindex = 1; blockindex <= numberofInodeBlocks; blockindex++){
			for (int i =0;i<blockindex&& bytesArrayIndex <= inodesBytes.length;i++)
				vlb.setElement(i, inodesBytes[bytesArrayIndex++]);
		try {
			du.write(blockindex, vlb);
		} catch (InvalidBlockNumberException | InvalidBlockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static int numberofBlockForInodes(int nBlocks){
		return Math.max(1, (int) (.010)*nBlocks);
	}
	public static int numberofInodes(int nBlocks, int bSize){
		return (numberofBlockForInodes(nBlocks) * bSize)/9;
	}
}
