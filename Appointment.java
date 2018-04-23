import java.io.Serializable;

public class Appointment implements Serializable {

	private String subject = "";
	private byte day;
	private byte month;
	private short year;
	private boolean allday;
	private short start;
	private short length;

	public Appointment(String subject, byte day, byte month, short year, boolean allday, short start, short length)
			throws IllegalStateException, IllegalArgumentException {

		if (!DateUtil.checkDate(day, month, year)) 
			throw new IllegalArgumentException("Ungültiges Datum.");
		if (start + length > 1440)
			throw new IllegalArgumentException("Termin überschreitet Mitternacht.");
		if (allday && (start != 0 || length != 0))
			throw new IllegalStateException("Ganztägige Termine haben keinen Zeitraum.");
		if (!allday && length <= 0)
			throw new IllegalStateException("Ein nicht-ganztägiger Termin muss einen Zeitraum haben.");

		this.subject = subject;
		this.day = day;
		this.month = month;
		this.year = year;
		this.allday = allday;
		this.start = start;
		this.length = length;
	}

	public Appointment(Appointment ap) { 
		if (ap != null) {
			this.subject = ap.subject;
			this.month = ap.month;
			this.year = ap.year;
			this.allday = ap.allday;
			this.start = ap.start;
			this.length = ap.length;
		}
	}

	public String toString() {
		return subject + ": "+ (day + 1) + "." + (month + 1) + "." + year
				+ (allday
					? ", ganztägig."
					: ", von " + (start / 60) + ":" + ((start%60) < 10 ? "0" : "") + (start%60) + " Uhr bis "
							+ (start/60 + (((start%60) + length)/60)) + ":"
							+ (((length+ (start%60))%60) < 10 ? "0" : "") + ((length+ (start%60))%60) + " Uhr.");
		
		// Das Gleiche in lesbar und ineffizient:
//		String ret = subject + ": "+ (day + 1) + "." + (month + 1) + "." + year;
//		if(allday)
//			ret += ", ganztägig";
//		else {
//			short hoursFrom = (short)(start / 60);
//			short minutesFrom = (short)(start%60);
//			short hoursTo = (short)(start/60);
//			short minutesTo = (short)((length+ minutesFrom)%60);
//			short addTo = (short)((minutesFrom + length)/60);
//			
//			hoursTo += addTo;
//			
//			ret += ", von " + hoursFrom + ":" + (minutesFrom < 10 ? "0" : "") + minutesFrom + " Uhr bis "
//					+ hoursTo + ":" + (minutesTo < 10 ? "0" : "") + minutesTo + " Uhr.";
//			
//		}
//		return ret;
	}
	
	public boolean isValid(){
		return DateUtil.checkDate(this.day,this.month, this.year) && (start + length < 1440) && ( (allday && start == 0 && length == 0) 
				|| (!allday && length != 0));
	}
	
	//getter und setter
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setDatum(byte day, byte month, short year){
		if(DateUtil.checkDate(day, month, year)){
			this.day = day;
			this.month = month;
			this.year = year;
		} else
			System.out.println("Ungültiges Datum. Das Datum konnte daher nicht geändert werden.");
	}

	public byte getDay() {
		return day;
	}

	public byte getMonth() {
		return month;
	}

	public short getYear() {
		return year;
	}

	public boolean isAllday() {
		return allday;
	}

	public void setAllday(boolean allday) {
		this.allday = allday;
	}

	public short getStart() {
		return start;
	}

	public void setTime(short start, short length) {
		this.start = start;
		this.length = length;
	}

	public short getLength() {
		return length;
	}
}
