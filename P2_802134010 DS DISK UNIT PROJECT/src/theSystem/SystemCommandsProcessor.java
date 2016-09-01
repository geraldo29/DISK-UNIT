package theSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import exceptions.ExistingDiskException;
import exceptions.NonExistingDiskException;
import DiskUnit.DiskUnit;
import operandHandlers.OperandValidatorUtils;
import listsManagementClasses.ListsManager;
import systemGeneralClasses.Command;
import systemGeneralClasses.CommandActionHandler;
import systemGeneralClasses.CommandProcessor;
import systemGeneralClasses.FixedLengthCommand;
import systemGeneralClasses.SystemCommand;
import systemGeneralClasses.VariableLengthCommand;
import stack.IntStack;


/**
 * 
 * @author Pedro I. Rivera-Vega && Geraldo Lopez
 *
 */
public class SystemCommandsProcessor extends CommandProcessor { 
	
	
	//NOTE: The HelpProcessor is inherited...

	// To initially place all lines for the output produced after a 
	// command is entered. The results depend on the particular command. 
	private ArrayList<String> resultsList; 
	private ArrayList<String> disklist;

	SystemCommand attemptedSC; 
	// The system command that looks like the one the user is
	// trying to execute. 
	boolean bsize = true;
	boolean stopExecution;
	public static boolean isMounted= false;
	static int index = -1;
	// This field is false whenever the system is in execution
	// Is set to true when in the "administrator" state the command
	// "shutdown" is given to the system.
	
	////////////////////////////////////////////////////////////////
	// The following are references to objects needed for management 
	// of data as required by the particular octions of the command-set..
	// The following represents the object that will be capable of
	// managing the different lists that are created by the system
	// to be implemented as a lab exercise. 
	private ListsManager listsManager = new ListsManager(); 

	/**
	 *  Initializes the list of possible commands for each of the
	 *  states the system can be in. 
	 */
	public SystemCommandsProcessor() {
		
		// stack of states
		currentState = new IntStack(); 
		listsManager.listFilesforFolder(DiskUnit.f);

		
		// The system may need to manage different states. For the moment, we
		// just assume one state: the general state. The top of the stack
		// "currentState" will always be the current state the system is at...
		currentState.push(GENERALSTATE); 

		// Maximum number of states for the moment is assumed to be 1
		// this may change depending on the types of commands the system
		// accepts in other instances...... 
		createCommandList(1);    // only 1 state -- GENERALSTATE

		// commands for the state GENERALSTATE
	
		// the command to create a new list is treated here as a command of variable length
		// as in the case of command testoutput, it is done so just to illustrate... And
		// again, all commands can be treated as of variable length command... 
		// One need to make sure that the corresponding CommandActionHandler object
		// is also working (in execute method) accordingly. See the documentation inside
		// the CommandActionHandler class for testoutput command.
		add(GENERALSTATE, SystemCommand.getFLSC("createdisk name int int", new CreateProcessor())); 
		add(GENERALSTATE, SystemCommand.getFLSC("deletedisk name", new deleteDisk())); 
		add(GENERALSTATE, SystemCommand.getFLSC("exit", new ShutDownProcessor())); 
		add(GENERALSTATE, SystemCommand.getFLSC("showdisks", new showdisks())); 
		add(GENERALSTATE, SystemCommand.getFLSC("help", new HelpProcessor())); 
		//add(GENERALSTATE, SystemCommand.getVLSC("sum int_list", new AddNumbersProcessor()));
		add(GENERALSTATE, SystemCommand.getVLSC("mount name", new mountProcessor())); 
		add(GENERALSTATE, SystemCommand.getVLSC("unmount name", new unmountProcessor())); 
		add(GENERALSTATE, SystemCommand.getFLSC("loadfile name name", new loadfile())); 
		add(GENERALSTATE, SystemCommand.getFLSC("cd name", new cd())); 
		add(GENERALSTATE, SystemCommand.getFLSC("cp name", new cp())); 

		add(GENERALSTATE, SystemCommand.getFLSC("mkdir name", new mkdir())); 
		add(GENERALSTATE, SystemCommand.getFLSC("rmdir name ", new rmdir())); 
		add(GENERALSTATE, SystemCommand.getFLSC("rm name", new rm())); 
		add(GENERALSTATE, SystemCommand.getFLSC("ls", new ls())); 
		add(GENERALSTATE, SystemCommand.getFLSC("cat name", new cat())); 



		// need to follow this pattern to add a SystemCommand for each
		// command that has been specified...
		// ...
				
		// set to execute....
		stopExecution = false; 

	}
		/**
		 * gets the results
		 * @return arraylist of strings 
		 */
	public ArrayList<String> getResultsList() { 
		return resultsList; 
	}
	
	// INNER CLASSES -- ONE FOR EACH VALID COMMAND --
	/**
	 *  The following are inner classes. Notice that there is one such class
	 *  for each command. The idea is that enclose the implementation of each
	 *  command in a particular unique place. Notice that, for each command, 
	 *  what you need is to implement the internal method "execute(Command c)".
	 *  In each particular case, your implementation assumes that the command
	 *  received as parameter is of the type corresponding to the particular
	 *  inner class. For example, the command received by the "execute(...)" 
	 *  method inside the "LoginProcessor" class must be a "login" command. 
	 *
	 */
	
	private class ShutDownProcessor implements CommandActionHandler { 
		public ArrayList<String> execute(Command c) { 
			resultsList = new ArrayList<String>(); 
			resultsList.add("SYSTEM IS SHUTTING DOWN!!!!");
			stopExecution = true;
			return resultsList; 
		}
	}

	
	/**
	 * class to create a disk 
	 * @author Geraldo Lopez
	 *
	 */
	
	private class CreateProcessor implements CommandActionHandler {
		@Override
		public ArrayList<String> execute(Command c) {

			resultsList = new ArrayList<String>(); 
			disklist = new ArrayList<String> ();
			FixedLengthCommand fc = (FixedLengthCommand) c;
			String name = fc.getOperand(1);
			disklist = listFilesforFolder(DiskUnit.f);
			
			int cap = Integer.parseInt(fc.getOperand(2));
			int size = Integer.parseInt(fc.getOperand(3));
			resultsList.add(cap + "      size :"+size);
			bsize = true;
						
			if (size < 32){
				resultsList.add("Disk can't be created less than 32 blocks or size has to be a power of 2: " + size);
				bsize = false;
			}
			if (!OperandValidatorUtils.isValidName(name))
				resultsList.add("Invalid name formation: " + name); 
			else if (nameExists(name, disklist))
				resultsList.add("This disk already exist: " + name);
			else
				if(bsize){

				try {
					DiskUnit.createDiskUnit(name, cap, size);
					listsManager.createNewList(name);
				} catch (InvalidParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExistingDiskException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}

				else {
					resultsList.add("disk wasn't created");
					
				}
			return resultsList; 
		} 
		
	}
	/**
	 * mounts the disk
	 * @author Geraldo Lopez
	 *
	 */
	private class mountProcessor implements CommandActionHandler {

		@Override
		public ArrayList<String> execute(Command c) {
			
			resultsList = new ArrayList<String>(); 
			disklist = new ArrayList<String>();
			disklist = listFilesforFolder(DiskUnit.f);

			VariableLengthCommand vlc = (VariableLengthCommand) c; 
			String name = vlc.getItemsForOperand(1).get(0);

			
			if (!OperandValidatorUtils.isValidName(name))
			resultsList.add("Invalid name formation: " + name); 
			else if (!nameExists(name, disklist))
				resultsList.add("This disk doesnt exist: " + name);
			else if(isMounted)
				resultsList.add("A disk is already mounted :" + disklist.get(index));
			else
				try {
					DiskUnit.mount(name);
					resultsList.add("Disk Mounted");
					isMounted =true;
					index = getIndex(name,disklist);
				} catch (NonExistingDiskException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			return resultsList; 
		}
		
	}
	/**
	 * unmounts the disk
	 * @author Geraldo Lopez
	 *
	 */
	private class unmountProcessor implements CommandActionHandler {
		@Override
		public ArrayList<String> execute(Command c) {

			resultsList = new ArrayList<String>(); 
			disklist = new ArrayList<String>();
			VariableLengthCommand vlc = (VariableLengthCommand) c; 
			String name = vlc.getItemsForOperand(1).get(0);

			//FixedLengthCommand fc = (FixedLengthCommand) c;
			//String name = fc.getOperand(1); 
			disklist = listFilesforFolder(DiskUnit.f);
			DiskUnit dU = new DiskUnit(name);
			if (!isMounted)
				resultsList.add("You have to mount a disk first");
			else if (index != getIndex(name,disklist))
				resultsList.add("Unmount the disk that is mounted : " + disklist.get(index));
			else{
			if (!OperandValidatorUtils.isValidName(name))
				resultsList.add("Invalid name formation: " + name); 
			else if (!nameExists(name, disklist))
				resultsList.add("This disk doesnt exist: " + name);
			else {
				dU.shutdown();
				resultsList.add("Disk Unmounted");
				isMounted =false;
				index = -1;
				
			}
				
			}
			return resultsList; 
		} 
		
	}


/**
 * deletes the disk
 * @author Geraldo Lopez
 *
 */
	private class deleteDisk implements CommandActionHandler {

	public ArrayList<String> execute(Command c) {


	resultsList = new ArrayList<String>();
	disklist = new ArrayList<String>();
	
	disklist = listFilesforFolder(DiskUnit.f);

	FixedLengthCommand fc = (FixedLengthCommand) c;

	String name = fc.getOperand(1);

	if (!nameExists(name, disklist))
		resultsList.add("This disk doesnt exist: " + name);
	else if (isMounted)
		isMounted = false;
	else {
		
		try {
			DeleteFile(DiskUnit.f, name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		


	}

	return resultsList;

	}

	}
	
	/**
	 * shows the disk
	 * @author geraldo lopez
	 *
	 */
	
	private class showdisks implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {
		
			resultsList = new ArrayList<String>();
			disklist = new ArrayList<String>();
			disklist = listFilesforFolder(DiskUnit.f);

			int nLists = listsManager.getNumberOfLists();

			if (nLists == 0)
				resultsList.add("There are no disk in the system at this moment.");


			else {


			resultsList.add("Names of the existing disk with their respective sizes are: ");

			for (int i=0; i<nLists; i++)
				if (index != getIndex(disklist.get(i),disklist))
					resultsList.add("\t"+listsManager.getName(i)+"    Number of blocks : " + listsManager.getCapacity(i)+ "    Size for each block: " + listsManager.getBlockSize(i)+"   NOT mounted");
				else if (index == getIndex(disklist.get(i),disklist))
					resultsList.add("\t"+listsManager.getName(i)+"    Number of blocks : " + listsManager.getCapacity(i)+ "    Size for each block: " + listsManager.getBlockSize(i)+ "  Mounted");
			}
				 
			return resultsList;

		

		
		}
		}
	
	/**
	 * loads the file 
	 * Attempts to read a new file into the current directory in the current working disk unit. The first operand is the name of the new file; how the system will record it in the current directory. If such name already exists in the current directory, and if it corresponds to an existing data file, then the current content of such file is erased and replaced by the content of the file being read. If the given name is new, then a new file with that name is created and its content will be a copy of the actual content of the file being read. The second operand is the name of the file to read. Such file must exist in the same directory where the program is being executed. If no such file, then the command ends with appropriate message. If the disk unit does not have enough space for the new file, the command also ends with a message. 
	 * @author geraldo lopez
	 *
	 */
	private class loadfile implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		
		
		
		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......

		String filename = fc.getOperand(1);
		String ftr = fc.getOperand(2);
		

		return resultsList;

		}

		
	}
	
	/**
	 * Changes the current directory to the new directory. If the new directory is specified as two consecutive dots (¨..¨), then the new current directory is set to the parent directory of the actual current directory. The parent of the root directory is itself. If the name given is a valid name in the current directory, but it corresponds to a data file, or if that name does not exist in the current directory, then the execution of the command ends after displaying appropriate message. 

	 * @author Geraldo Lopez
	 *
	 */
	
	private class cd implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		
		

		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......

		String filename = fc.getOperand(1);
		if (!nameExists(filename, disklist))
			resultsList.add("This directory doesnt exist: " + filename);
		else
		if(isMounted){
			
			resultsList.add("Moved directory");
			}
			else resultsList.add("Mount a disk first");

		

		return resultsList;

		}

		
	}
	/**
	 * Copies one internal file to another internal file. It works similar to the command loadfile, but this time the input file (name given first) is also an internal file that must be a data file in the current directory. You should figure out the rest.

	 * @author 4035a5
	 *
	 */
	private class cp implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		
		

		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......

		String filename = fc.getOperand(1);
		String filename2 = fc.getOperand(2);
		if (!nameExists(filename, disklist))
			resultsList.add("This directory doesnt exist: " + filename);
		else
		if(isMounted){
			
			resultsList.add("Copied");
			}
			else resultsList.add("Mount a disk first");

		
		
		

		return resultsList;

		}

		
	}
	/**
	 * Creates a new directory inside the current directory. If the name already exists in the current directory, then the execution of the command ends after displaying appropriate message.
	 * @author Geraldo Lopez
	 *
	 */
	
	private class mkdir implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {
		DiskUnit direc;
		resultsList = new ArrayList<String>();
		disklist = new ArrayList<String>();
		disklist = listFilesforFolder(DiskUnit.f);
		

		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......

		String filename = fc.getOperand(1);
		if (!OperandValidatorUtils.isValidName(filename))
			resultsList.add("Invalid name formation: " + filename); 
		else if (nameExists(filename, disklist))
			resultsList.add("This disk already exist: " + filename);
		if(isMounted){
			
		direc = new DiskUnit(filename);
		}
		else resultsList.add("Mount a disk first");

		return resultsList;

		}

		
	}
	/**
	 *  Removes the given directory from the current directory. If the name exists in the current directory, but it corresponds to a data file, or if no such name exists, then the execution ends after displaying appropriate message. If the name exists and corresponds to a directory, then it is removed from the current directory. That operation includes removing all files and directories that are part of the subtree rooted at the specified directory. 
	 * @author Geraldo Lopez
	 *
	 */
	
	private class rmdir implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		FixedLengthCommand fc = (FixedLengthCommand) c;

		String filename = fc.getOperand(1);
		
		if (!nameExists(filename, disklist))
			resultsList.add("This directory doesnt exist: " + filename);
		else
		if(isMounted){
			
			resultsList.add("This is a data file not a directory.");
			}
			else resultsList.add("Mount a disk first");

		

		return resultsList;

		}

		
	}
	
	
	
	/**
	 * Removes the particular file from the current directory if such data file exists in it.
	 * @author Geraldo Lopez
	 *
	 */
	private class rm implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		
		

		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......
		disklist = listFilesforFolder(DiskUnit.f);

		String filename = fc.getOperand(1);
		if (!nameExists(filename, disklist))
			resultsList.add("This disk doesnt exist: " + filename);
		else 
			if(isMounted){
				resultsList.add("Removed the file");
			
			}
			else resultsList.add("Mount a disk first");

		return resultsList;

		}

		
	}
	/**
	 * List the names and sizes of all the files and directories that are part of the current directory.
	 * @author 4035a5
	 *
	 */
	private class ls implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		
		

		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......
		disklist = new ArrayList<String>();
		disklist = listFilesforFolder(DiskUnit.f);
		if (isMounted){
		 resultsList.add("Names and sizes of files: Disk is empty");
		}
		else resultsList.add("Mount disk first");
		
		
		
		return resultsList;

		}

		
	}
	/**
	 * Displays the content of the given internal file. You should figure out the rest. 
	 * @author 4035a5
	 *
	 */
	private class cat implements CommandActionHandler {

		public ArrayList<String> execute(Command c) {

		resultsList = new ArrayList<String>();
		
		
		

		FixedLengthCommand fc = (FixedLengthCommand) c;

		// the following needs to be adapted to named lists and the

		// usage of the ListsManagerObject ......
		File file = null;
		String filename = fc.getOperand(1);
		
		
		if (!OperandValidatorUtils.isValidName(filename))
			resultsList.add("Invalid name formation: " + filename); 
		else if (!nameExists(filename, disklist))
			resultsList.add("This disk doesnt exist: " + filename);
		
		
		String path  = file.getAbsolutePath()+ file.separator+filename;

		
		File file2 = new File(path);

		file2.canRead();
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file2));

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return resultsList;

		}

		
	}
	
	
	


	/**
	 * checks if it is shutting down 
	 * @return boolean variable if it stopped
	 */
	public boolean inShutdownMode() {
		return stopExecution;
	}

	/**
	 * loads the disks to an arraylist
	 * @param folder where there are going to be loaded from
	 * @return
	 */
	public ArrayList<String> listFilesforFolder (File folder){
		ArrayList<String> resultsList2 = new ArrayList<String>();

		for (File fileentry : folder.listFiles() ){
			if (fileentry.isDirectory()){
				listFilesforFolder(fileentry);
			}
			else {
				
				resultsList2.add(fileentry.getName());
				
			}
		}
		return resultsList2;
	}

	/**
	 * checks whether the disk exists
	 * @param name name of disk
	 * @param list list to be checked
	 * @return
	 */
	public boolean nameExists(String name, ArrayList<String> list){
		for (int i = 0; i<list.size();i++){
			if (name.equals(list.get(i)))
				return true;
		}
		return false;
	}
	public int getIndex(String name, ArrayList<String> list){
		int etr = 0;
		for (int i = 0; i<list.size();i++){
			if (name.equals(list.get(i)))
				etr = i;
	}
		return etr;
	}
	
	/**
	 * method to delete file 
	 * @param file ; path of file to be deleted
	 * @param name  name of file to be deleted
	 * @throws FileNotFoundException
	 */
	public void DeleteFile(File file, String name) throws FileNotFoundException{
		String path  = file.getAbsolutePath()+ file.separator+name;
		System.out.println(path);
		Path pa = Paths.get(path);
		if (!file.exists())
			return;
		if(file.isDirectory()){
			
			for (File f : file.listFiles()){
				if (f.getAbsolutePath().equals(path)){
					System.out.println(f.getAbsolutePath());
					
					try {
						
					    Files.delete(pa);
					} catch (NoSuchFileException x) {
					    System.err.format("%s: no such" + " file or directory%n", path);
					} catch (DirectoryNotEmptyException x) {
					    System.err.format("%s not empty%n", path);
					} catch (IOException x) {
					    // File permission problems are caught here.
					    System.err.println(x);
					}
				}}
			}
		

		}
			
	}
	
	




