/**
 * 
 */
package exceptions;

/**
 * @author authoro
 *
 *         this expcetiopn is thrown when number of fields are less than
 *         expected
 *
 */
public class LowFieldNumberException extends Exception {

	
	public LowFieldNumberException(){
		super();
	}
	
	public LowFieldNumberException(String message){
		super(message);
	}
	
}
