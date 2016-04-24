package uk.co.sleonard.unison.datahandling;


public class DBException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3897976586072883356L;

	private String message;

	private Exception cause;

	public DBException(Exception e) {
		message = e.getMessage();
		cause = e;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Exception getCause() {
		return cause;
	}

}
