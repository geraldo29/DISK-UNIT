package DiskUnit;

import interfaces.Position;
import interfaces.Tree;

/**
 * abstract tree class
 * @author Geraldo Lopez
 *
 * @param <E>
 */
public abstract class AbstractTree<E> implements Tree<E> {
/**
 * checks if empty
 */
	public boolean isEmpty(){
		return size()==0;
		
	}
	/**
	 * checks if it has kids
	 */
	public boolean isInternal(Position <E> p){
		return this.numChildren(p)>0;
	}
	/**
	 * checks if position p has no kids
	 */
	public boolean isExternal(Position<E> p){
		return numChildren(p)==0;
	}
	/**
	 * checks if position is the root
	 */
	public boolean isRoot(Position<E> p){
		return p ==root();
	}
	
}
