import java.io.Serializable;

public class Link<T extends Appointment> implements Serializable{

	private T termin;
	private Link<T> next;

	public Link(T termin, Link<T> next) {
		this.termin = termin;
		this.next = next;
	}

	public void setNext(Link<T> next) {
		this.next = next;
	}

	public Link<T> getNext() {
		return next;
	}

	public T getTermin() {
		return this.termin;
	}

	public void setTermin(T termin) {
		if(termin.isValid())
			this.termin = termin;
		else
			System.out.println("Der Termin konnte nicht geändert werden.");
	}
}
