package listsManagementClasses;

import java.io.File;
import java.util.ArrayList;

import listsManagementClasses.DiskManager;
/**
 * This class defines the type of object that manages the different 
 * lists being managed by the system for the lab session 
 * 
 * @author pedroirivera-vega
 *
 */
public class ListsManager {
	private static ArrayList<DiskManager> lists; 
	
	public ListsManager() { 
		lists = new ArrayList<>(); 
	}

	/**
	 * Find the index of the position where a list with a given name is. 
	 * If no such list it returns -1; otherwise, it returns the index of
	 * the position where it is located in the list of lists....
	 * @param name
	 * @return
	 */
	public int getListIndex(String name) { 
		for (int i=0; i<lists.size(); i++) 
			if (lists.get(i).getName().equals(name)) 
				return i; 
		return -1; 
	}

	/**
	 * Creates a new named list with the given name. 
	 * @param lName the name of the new list. 
	 *     PRE: the name given is assumed to be a valid name for 
	 *     a list. 
	 */
	public void createNewList(String lName) {
		lists.add(new DiskManager(lName)); 
	}
	/**
	 * adds element to list
	 * @param listIndex index of where to be added
	 * @param value element to be added 
	 * @throws IndexOutOfBoundsException
	 */
	public void addElement(int listIndex, int value) 
			throws IndexOutOfBoundsException 
	{ 
		// PRE: the listIndex is assumed to be a valid index for the
		// list of lists....
	//	lists.get(listIndex).add(value); 		
	}
	
	/**
	 * removes element from lists and returns it 
	 * @param listIndex index 
	 * @param index index 
	 * @return element deleted
	 * @throws IndexOutOfBoundsException
	 */
	public int removeElement(int listIndex, int index) 
			throws IndexOutOfBoundsException 
	{
		return index;
		// PRE: the listIndex is assumed to be a valid index for the
		// list of lists....
		//return lists.get(listIndex).remove(index); 				
	}
		
	/**
	 * add element to list
	 * @param listIndex
	 * @param index
	 * @param value
	 * @throws IndexOutOfBoundsException
	 */
	public void addElement(int listIndex, int index, int value) 
			throws IndexOutOfBoundsException 
	{ 
		// PRE: the listIndex is assumed to be a valid index for the
		// list of lists....
//		lists.get(listIndex).add(index, value); 		
	}

	/**
	 * gets element from an index of the list
	 * @param listIndex
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public int getElement(int listIndex, int index) 
			throws IndexOutOfBoundsException 
	{
		return index;
		// PRE: the listIndex is assumed to be a valid index for the
		// list of lists....
	//	return lists.get(listIndex).get(index); 				
	}
	/**
	 * get size of the list index
	 * @param listIndex
	 * @return
	 */
	public int getSize(int listIndex) 
	{
		// PRE: the listIndex is assumed to be a valid index for the
		// list of lists....
		return lists.get(listIndex).size(); 				
	}
	/**
	 * get the block size 
	 * @param listIndex
	 * @return block size 
	 */
	public int getBlockSize(int listIndex){
		String name = lists.get(listIndex).getName();
		return lists.get(listIndex).blockSize(name);
	} 
	/**
	 * returns the capacity of the disk 
	 * @param listIndex disk
	 * @return capacity
	 */
	public int getCapacity(int listIndex){
		String name = lists.get(listIndex).getName();
		return lists.get(listIndex).Capacity(name);
	}
	/**
	 * returns the name of the disk 
	 * @param listIndex index of disk 
	 * @return name 
	 */
	public String getName(int listIndex) {
		// PRE: the listIndex is assumed to be a valid index for the
		// list of lists....
		return lists.get(listIndex).getName(); 
	}
	/**
	 * return number of list 
	 * @return number of lists
	 */
	public int getNumberOfLists() { 
		return lists.size(); 
	}
	/**
	 * checks if name is in the list 
	 * @param name name to be checked
	 * @return true if it is inside the list . false otherwise
	 */
	public boolean nameExists(String name) { 
		int index = getListIndex(name); 
		return index != -1; 
	} 
	
	/**
	 * loads the file of the disks 
	 * @param folder folder to be read 
	 */
	public static void listFilesforFolder (File folder){

		for (File fileentry : folder.listFiles() ){
			if (fileentry.isDirectory()){
				listFilesforFolder(fileentry);
			}
			else {
				
				lists.add(new DiskManager(fileentry.getName()));
				
			}
		}
		
	}

}
