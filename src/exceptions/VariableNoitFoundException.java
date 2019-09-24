/**
 * 
 */
package exceptions;

/**
 * @author authoro
 *	thrown when a enviroment variable is set by the query which does not exist 
 */
public class VariableNoitFoundException extends Exception {
	
	public VariableNoitFoundException() {
		super();
	}

	public VariableNoitFoundException(String msg) {
		super(msg);
	}
	
}
