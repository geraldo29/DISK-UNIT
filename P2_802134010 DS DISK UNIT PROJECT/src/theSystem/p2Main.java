/**
 * 
 */
package theSystem;

import java.io.File;
import java.io.IOException;

import systemGeneralClasses.SystemController;

/**
 * @author Pedro I. Rivera-Vega
 *
 */
public class p2Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException  {
		File f =  new File("src" + File.separator + "DiskUnits" + File.separator);

		if (!f.exists()){
			f.mkdirs();
		}
		SystemController system = new SystemController(); 
		system.start(); 
		// the system is shutting down...
		System.out.println("+++++ SYSTEM SHUTDOWN +++++"); 
	}

}
