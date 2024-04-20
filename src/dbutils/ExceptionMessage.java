package dbutils;

import java.sql.BatchUpdateException;

import org.hibernate.exception.ConstraintViolationException;

import booklion.client.utils.StandardReturn;

/**
 * @author Blake McBride
 */
public class ExceptionMessage {

	
	public static StandardReturn getReturn(Exception e) {
		try {
			if (e instanceof ConstraintViolationException) {
				BatchUpdateException bue = (BatchUpdateException) e.getCause();
	//			return new StandardReturn("Constraint violation occurred; record not saved.");
				return new StandardReturn(bue.getNextException().getMessage());
			} else
				return new StandardReturn("Error processing record; record not saved.");
		} catch (Throwable t) {
			return  new StandardReturn("Error processing record; record not saved.");
		}
	}

}
