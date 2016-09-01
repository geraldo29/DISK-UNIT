package DiskUnit;

public class BlocksManager {


private int firstFLB;
private int flIndex;

public int getFreeBN() throws Exception { 
   int bn = 0; 
   if (firstFLB == 0) 
      throw new Exception("Full");

   // disk has space 
   if (flIndex != 0) { 
     // bn = firstFLB[flIndex];      // you need to figure out how to do this....
      flIndex--; 
   }   
   else {                                  // the current root node in the tree is the one to be returned
      bn = firstFLB; 
     // firstFLB = firstFLB[0];      // you need to figure out how to do this....
    //  flIndex = n-1;                   // where n is as described above
   } 
   return bn;          // the index of the free block that is taken...
}
//Whenever a block is freed, it is put in the free block structure. The idea of an algorithm for that is as follows. 
public void registerFB(int bn) { 
   if (firstFLB == 0)  { 
      firstFLB = bn; 
   //   firstFLB[0] = 0; 
      flIndex = 0; 
   }  
   else if (flIndex == bn-1) {      // the root node in the tree is full
    //  bn[0] = firstFLB; 
      flIndex = 0; 
      firstFLB = bn; 
   }  
   else { 
      flIndex++; 
    //  firstFLB[flIndex] = bn; 
   } 
}     

	
	
}
