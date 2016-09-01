package DiskUnit;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;
import java.util.Random;
import exceptions.EmptyStackException;
import exceptions.InvalidBlockNumberException;
import exceptions.NonExistingDiskException;
import exceptions.InvalidBlockException;
import exceptions.ExistingDiskException;


public class DiskUnit {

	 private final static int DEFAULT_CAPACITY = 1024;  // default number of blocks 	
	  private final static int DEFAULT_BLOCK_SIZE = 256; // default number of bytes per block
	  private int capacity;     	// number of blocks of current disk instance
	  private int blockSize; 	// size of each block of current disk instance
	  private int inodeSize;
	  private int nInodes;
	  
	private static int  firstBlock;
	private static int numIn;
	public static byte[] bytes;
	public RandomAccessFile disk;  //this is a java class for working with readable files 
	public static File f =  new File("src" + File.separator + "DiskUnits" + File.separator);
	
	
	
	/**
	 * creates a instance of disk unit and creates a new file that 
	 * can be written or read if 
	 * @param name to be given 
	 */


	public DiskUnit(String name) {

		if (!f.exists()){
			f.mkdirs();
		}
		try {
			
			File fi = new File(f,name);

			disk = new RandomAccessFile(fi, "rw");
			
		} catch (IOException e) {
			System.err.println("Unable to start the disk");
			System.exit(1);
		}

	}

	/**
	 * this method will close the random access file
	 */
	public void shutdown() {
		try {
			disk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Turns on an existing disk unit.
	 * 
	 * @param name
	 *            the name of the disk unit to activate
	 * @return the corresponding DiskUnit object
	 * @throws NonExistingDiskException
	 *             whenever no �disk� with the specified name is found.
	 */
	public static DiskUnit mount(String name) throws NonExistingDiskException {
		File file = new File(f, name);
		if (!file.exists())
			throw new NonExistingDiskException("No disk has name : " + name);

		DiskUnit dUnit = new DiskUnit(name);

		// get the capacity and the block size of the disk from the file
		// representing the disk
		try {
			dUnit.disk.seek(0);
			dUnit.capacity = dUnit.disk.readInt();
			dUnit.blockSize = dUnit.disk.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dUnit;
	}

	/***
	 * Creates a new disk unit with the given name. The disk is formatted as
	 * having default capacity (number of blocks), each of default size (number
	 * of bytes). Those values are: DEFAULT_CAPACITY and DEFAULT_BLOCK_SIZE. The
	 * created disk is left as in off mode.
	 * 
	 * @param name
	 *            the name of the file that is to represent the disk.
	 * @throws ExistingDiskException
	 *             whenever the name attempted is already in use.
	 */
	public static void createDiskUnit(String name) throws ExistingDiskException {
		createDiskUnit(name, DEFAULT_CAPACITY, DEFAULT_BLOCK_SIZE);
	}

	/**
	 * Creates a new disk unit with the given name. The disk is formatted as
	 * with the specified capacity (number of blocks), each of specified size
	 * (number of bytes). The created disk is left as in off mode.
	 * 
	 * @param name
	 *            the name of the file that is to represent the disk.
	 * @param capacity
	 *            number of blocks in the new disk
	 * @param blockSize
	 *            size per block in the new disk
	 * @throws ExistingDiskException
	 *             whenever the name attempted is already in use.
	 * @throws InvalidParameterException
	 *             whenever the values for capacity or blockSize are not valid
	 *             according to the specifications
	 */
	public static void createDiskUnit(String name, int capacity, int blockSize)
	throws ExistingDiskException, InvalidParameterException
	{
		bytes = new byte[24];
	    File file=new File(f, name);
	    if (file.exists())
	       throw new ExistingDiskException("Disk name is already used: " + name);
	   	
	    RandomAccessFile disk = null;
	    if (capacity < 0 || blockSize < 0 ||
	         !Utils.powerOf2(capacity) || !Utils.powerOf2(blockSize))
	       throw new InvalidParameterException("Invalid values: " +
	   		   " capacity = " + capacity + " block size = " +
	   		   blockSize);
	    // disk parameters are valid... hence create the file to represent the
	    // disk unit.
	    try {
	        disk = new RandomAccessFile(file, "rw");
	    }
	    catch (IOException e) {
	        System.err.println ("Unable to start the disk");
	        System.exit(1);
	    }

	    reserveDiskSpace(disk,capacity,blockSize);
	   	
	    // after creation, just leave it in shutdown mode - just
	    // close the corresponding file
	    try {
	        disk.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	  
	}

	/**
	 * this method will reserve the block 0 to 
	 * provide the capacity and bytes of the block
	 * @param disk
	 * @param capacity
	 * @param blockSize
	 */
	private static void reserveDiskSpace(RandomAccessFile disk, int capacity,
			int blockSize) {
		try {
			disk.setLength(blockSize * capacity);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write disk parameters (number of blocks, bytes per block) in
		// block 0 of disk space
		try {
			disk.seek(0);
			Utils.copyIntToBytesArray(bytes, 0, capacity);
			Utils.copyIntToBytesArray(bytes, 4, blockSize);
			Utils.copyIntToBytesArray(bytes, 8, 0);//first free block
			Utils.copyIntToBytesArray(bytes, 12, 0);//index free block 
			Utils.copyIntToBytesArray(bytes, 16, 0); //first free inode 
			Utils.copyIntToBytesArray(bytes, 20, 0); //number of inodes
			
			for(Byte e : bytes)
				disk.writeByte(e);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * this method will reserve the block 0-8 to 
	 * provide the capacity and bytes of the block and the first block and type of inode
	 * @param disk
	 * @param capacity
	 * @param blockSize
	 */
	private static void reserveiNodeSpace(RandomAccessFile disk, int capacity, int blockSize){
		try {
			int counter= 0;
			numIn = (int)(capacity * blockSize * .04);
			int totalIn = numIn * 9;
			
			
			disk.seek(1*blockSize);
			disk.writeByte(1);
			disk.writeInt(0);
			
			while (totalIn > blockSize){
				totalIn -= blockSize;
				counter++;
			}
			firstBlock = (counter + 1	) * blockSize;
			disk.writeInt(firstBlock);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * writes the content of block b into the disk block corresponding to
	 * blockNum; i.e., whatever is the actual content of the disk block
	 * corresponding to the specified block number (blockNum) is changed to (or
	 * overwritten by) that of b in the current disk instance. The first
	 * exception is thrown whenever the block number received is not valid for
	 * the current disk instance. The second exception is thrown whenever b does
	 * not represent a valid disk block for the current disk instance (for
	 * example, if b is null, or if that block instance does not match the block
	 * size of the current disk instance).
	 */
	public void write(int blockNum, VirtualDiskBlock b)
			throws InvalidBlockNumberException, InvalidBlockException {
		
		if(blockNum >= capacity || blockNum <0){
			throw new InvalidBlockNumberException("Block number is not valid for the current disk instance");
		}
		if (b.getCapacity()!=blockSize || b==null){
			throw new InvalidBlockException("Block instance doesnt match size or is no valid");
		}
		
		try {
			disk.seek(blockNum*blockSize);
			
			for(int i=0;i<b.getCapacity();i++)
				disk.writeByte(b.getElement(i));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * reads a given block from the disk. The content of the specified disk
	 * block (identified by its number � blockNum) is copied as the new content
	 * of the current instance of block being referenced by parameter b. Notice
	 * that b must reference an existing instance of VirtualDiskBlock, and that
	 * the current content of that instance shall be overwritten by the content
	 * of the disk block to be read. The announced exceptions are thrown as
	 * described for the write operation.
	 * 
	 * @param blockNum
	 * @param b
	 * @throws InvalidBlockNumberException
	 * @throws InvalidBlockException
	 */
	public void read(int blockNum, VirtualDiskBlock b)
			throws InvalidBlockNumberException, InvalidBlockException {
		if(blockNum >= capacity || blockNum <0){
			throw new InvalidBlockNumberException("Block number is not valid for the current disk instance");
		}
		if (b.getCapacity()!=blockSize || b==null){
			throw new InvalidBlockException("Block instance doesnt match size or is no valid");
		}
		
		try {
			disk.seek(blockNum*blockSize);
				
			for(int i=0;i<blockSize;i++)
				b.setElement(i, disk.readByte());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return returns a nonnegative integer value corresponding to the number
	 *         of valid blocks (unused + used) that the current disk instance
	 *         has.
	 */
	public int getCapacity() {
		return capacity;

	}

	/**
	 * 
	 * @return a nonnegative integer value which corresponds to the size (number
	 *         of character elements) of a block in the current disk instance.
	 */
	public int getBlockSize() {
		return blockSize;

	}

	/**
	 * Formats the disk. This operation visits every �physical block� in the
	 * disk and fills with zeroes all those that are valid. Also, it randomly
	 * marks blocks as damaged with probability 1e-5. Notice that this is done
	 * to emulate a real situation in which physical blocks are detected as not
	 * feasible to safely store data. Depending on how reliable the disk unit
	 * is, the frequency of damaged blocks is very small. Here, we assume that
	 * such frequency is 1e-5.
	 * 
	 * 
	 * 
	 * will write -1 to block if it is damaged. write zero if valid 
	 * 
	 * @throws InvalidBlockException 
	 * @throws InvalidBlockNumberException 
	 */
	public void lowLevelFormat() throws InvalidBlockNumberException, InvalidBlockException {
	
		
		try {
			Random ran = new Random();
			boolean result;
			for (int i = 1;i<capacity;i++){
				disk.seek(i*blockSize); 
				result= ran.nextInt(100000)==1;
				if(result)
					for (int j=0; j<blockSize;j++)
				
							disk.writeByte((byte)0);
						
				else
					for (int j=0;j<blockSize;j++){
						disk.writeByte((byte) 0);
					}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	/**
	 * shutdown method to close disk
	 * @param name
	 * name to close
	 */
	public static void shutdown(String name){
	File file = new File(f, name);
	if (!file.exists())
		try {
			throw new NonExistingDiskException("No disk has name : " + name);
		} catch (NonExistingDiskException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	DiskUnit dUnit = new DiskUnit(name);

	// get the capacity and the block size of the disk from the file
	// representing the disk
	
		try {
			dUnit.disk.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	
}