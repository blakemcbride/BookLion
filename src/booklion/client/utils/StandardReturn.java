package booklion.client.utils;

import java.io.Serializable;

/**
 * @author Blake McBride
 */
public class StandardReturn implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS = 0;
	private int returnCode;  //  0=success
	private String msg;
	
	public StandardReturn() {	//  default constructor required by the system
		this.returnCode = SUCCESS;
		this.msg = "";
	}
	
	public StandardReturn(int code) {
		this.returnCode = code;
		this.msg = "";
	}
	
	public StandardReturn(String msg) {
		if (msg == null  ||  msg.isEmpty()) {
			returnCode = SUCCESS;
			this.msg = "";
		} else {
			this.returnCode = -1;
			this.msg = msg;
		}
	}
		
	public StandardReturn(int code, String msg) {
		this.returnCode = code;
		this.msg = msg;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
