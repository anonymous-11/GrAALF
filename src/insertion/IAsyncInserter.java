/**
 * 
 */
package insertion;

/**
 * @author author
 *
 */
public interface IAsyncInserter {
	
	public void run();

	public void reloadConnection(boolean byForce);
}
