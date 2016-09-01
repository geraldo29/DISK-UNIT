package listsManagementClasses;

import java.io.IOException;
import java.util.ArrayList;

import theSystem.SystemCommandsProcessor;
import exceptions.NonExistingDiskException;
import DiskUnit.DiskUnit;

/**
 * disk manager class
 * @author Geraldo Lopez
 *
 */
public class DiskManager {

	private String name = null; 
	
	/**
	 * constructor for disk manager
	 * @param name
	 */
	public DiskManager(String name) { 
		
		this.name = name; 
	}
	/**
	 * get name method
	 * @return
	 * name
	 */
	public String getName() {
		return name;
	}

	/**
	 * return size method 
	 * @return size
	 */
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * return capacity of disk
	 * @param name disk name
	 * @return capacity 
	 */
	public int Capacity(String name){
		
		int bsize = 0;
		if (!SystemCommandsProcessor.isMounted){
		try {
			//DiskUnit 
			
			 bsize = DiskUnit.mount(name).getCapacity();
		} catch (NonExistingDiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DiskUnit.shutdown(name);
		
		}else 
		{
			SystemCommandsProcessor.isMounted = false;
			try {
				//DiskUnit 
				
				 bsize = DiskUnit.mount(name).getCapacity();
			} catch (NonExistingDiskException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DiskUnit.shutdown(name);
			SystemCommandsProcessor.isMounted=true;
		}
		return bsize;
		
		
		 
	}
	
	/**
	 * return blocksize of disk name 
	 * @param name disk name 
	 * @return  blocksize of disk
	 */
	public int blockSize(String name){
		int bsize = 0;
		if (!SystemCommandsProcessor.isMounted){
		try {
			//DiskUnit 
			
			 bsize = DiskUnit.mount(name).getBlockSize();
		} catch (NonExistingDiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DiskUnit.shutdown(name);
		
		}else 
		{
			SystemCommandsProcessor.isMounted = false;
			try {
				//DiskUnit 
				
				 bsize = DiskUnit.mount(name).getBlockSize();
			} catch (NonExistingDiskException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DiskUnit.shutdown(name);
			SystemCommandsProcessor.isMounted=true;
		}
		return bsize;
	}
	
}
