package DiskUnitTester;

import java.io.IOException;

import exceptions.InvalidBlockException;
import exceptions.InvalidBlockNumberException;
import exceptions.NonExistingDiskException;
import DiskUnit.DiskUnit;
import DiskUnit.VirtualDiskBlock;

public class Tester1 {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvalidBlockException 
	 * @throws InvalidBlockNumberException 
	 */
	public static void main(String[] args) throws IOException, InvalidBlockNumberException, InvalidBlockException {
		DiskUnit d = null;
		try {
			d = DiskUnit.mount("ggg");
		} catch (NonExistingDiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // edit the name of the disk to mount
		
	    showDiskContent(d); 
		
		showFileInDiskContent(d);
		d.shutdown(); 
	}

		
	/**
	 * shows the content of the file in the disk 
	 * @param d the disk unit to show 
	 */
	
	private static void showFileInDiskContent(DiskUnit d) { 
		VirtualDiskBlock vdb = new VirtualDiskBlock(d.getBlockSize()); 
		
		System.out.println("\nContent of the file begining at block 1"); 
		int bn = 1; 
		while (bn != 0) { 
			try {
				d.read(bn, vdb);
			} catch (InvalidBlockNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBlockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			showVirtualDiskBlock(bn, vdb);
			bn = getNextBNFromBlock(vdb);			
		}
		
	}

	/**
	 * show content of the disk and its capacity and block size 
	 * @param d the disk to show 
	 */
	
	private static void showDiskContent(DiskUnit d) { 
		
		System.out.println("Capacity of disk is: " + d.getCapacity()); 
		System.out.println("Size of blocks in the disk is: " + d.getBlockSize()); 
		
		VirtualDiskBlock block = new VirtualDiskBlock(d.getBlockSize()); 
		for (int b = 0; b < d.getCapacity(); b++) { 
			try {
				d.read(b, block);
			} catch (InvalidBlockNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBlockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			showVirtualDiskBlock(b, block); 
		}
		
	}
	/**
	 * show content of the virtual disk block 
	 * @param b the number 
	 * @param block to show 
	 */

	private static void showVirtualDiskBlock(int b, VirtualDiskBlock block) {
	    System.out.print(" Block "+ b + "\t"); 
	    for (int i=0; i<block.getCapacity(); i++) {
	    	char c = (char) block.getElement(i); 
	    	if (Character.isLetterOrDigit(c))
	    		System.out.print(c); 
	    	else
	    		System.out.print('-'); 
	    }
	    System.out.println(); 
	}

	
	/**
	 * copies value to the next block 
	 * @param vdb the virtual disk block
	 * @param value the value to copy 
	 */
	public static void copyNextBNToBlock(VirtualDiskBlock vdb, int value) { 
		int lastPos = vdb.getCapacity()-1;

		for (int index = 0; index < 4; index++) { 
			vdb.setElement(lastPos - index, (byte) (value & 0x000000ff)); 	
			value = value >> 8; 
		}

	}
	
	/**
	 * gets the next value from a block
	 * @param vdb the virtual disk block 
	 * @return value from block
	 */
	
	private static int getNextBNFromBlock(VirtualDiskBlock vdb) { 
		int bsize = vdb.getCapacity(); 
		int value = 0; 
		int lSB; 
		for (int index = 3; index >= 0; index--) { 
			value = value << 8; 
			lSB = 0x000000ff & vdb.getElement(bsize-1-index);
			value = value | lSB; 
		}
		return value; 

	}

}