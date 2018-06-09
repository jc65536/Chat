package util;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7837418507251447190L;

	public static final boolean SERVER = true;
	private String message;
	private String sender;
	private InetAddress ip;
	private Date date;
	private boolean fromServer;

	public Message(String m, boolean s, Date d) {
	    message = m;
	    fromServer = s;
	    date = d;
    }

	public Message(String m, String u, InetAddress a, Date d) {
		this.message = m;
		this.sender = u;
		this.ip = a;
		this.date = d;
	}

	public String getMessage() {
		return this.message;
	}

	public String getSender() {
		return this.sender;
	}

	public InetAddress getSenderIP() {
		return this.ip;
	}

	public Date getDate() {
		return this.date;
	}

	@Override
	public String toString() {
	    if (fromServer)
	        return message;
	    else
	        return String.format("[%s] %s: %s", date.toString(), sender, message);
    }
}
