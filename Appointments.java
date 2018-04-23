import java.io.Serializable;

public class Appointments implements Serializable{

	private Link<Appointment>[][] buckets = (Link<Appointment>[][]) new Link[200][366];

	public boolean add(Appointment ap) {

		if (!ap.isValid())
			return false;

		short y = berechneJahr(ap.getYear());
		short d = berechneTag(ap.getDay(), ap.getMonth(), ap.getYear());
		
		if(ap.isAllday() || buckets[y][d] == null || ap.getStart() <= buckets[y][d].getTermin().getStart()) { // Vorne anfügen, wenn der Termin ganztägig ist, oder er der bisher erste an dem tag ist

			// Termin am Anfang der Liste einfügen (aktuell eingetragener Termin in
			// der Position wird 'next' vom Neuen)
			buckets[y][d] = new Link<Appointment>(ap, buckets[y][d]);
		} else {
			
			Link<Appointment> lauf = buckets[y][d];
			
			try {
				
				while(lauf.getNext().getTermin().isAllday() 
						|| ap.getStart() > lauf.getNext().getTermin().getStart()) { // läuft bis ans ende der vorangestellten ganztägigen Termine
					
					lauf = lauf.getNext();
					if(lauf.getNext() == null)
						break;
				}
			} catch (NullPointerException n) {
			}

			
//			while(lauf.getNext() != null && ap.getStart() > lauf.getNext().getTermin().getStart())
//				lauf = lauf.getNext();
			
			lauf.setNext(new Link<Appointment>(ap, lauf.getNext()));
			
		}
		
		
		
		
		
		
		
		
		return true;
	}

	public void remove(Appointment ap) { 

		short y = berechneJahr(ap.getYear());
		short d = berechneTag(ap.getDay(), ap.getMonth(), ap.getYear());
		
		if (ap.isValid() && buckets[y][d] != null) { // buckets[y][d] entspricht dem zu entfernenden Appointment-Objekt
			
			Link<Appointment> lauf = buckets[y][d];
			
			if (lauf.getTermin() == ap)
				buckets[y][d] = lauf.getNext();
			else
				while(lauf.getNext() != null){ // Es wird davon ausgegangen, dass keine identischen
					if(lauf.getNext().getTermin() == ap) {  // Termine mehrfach in der Liste existieren, 
						lauf.setNext(lauf.getNext().getNext()); // bzw. nur einer von ihnen entfernt werden muss.
						break;
					}
					lauf = lauf.getNext();
				}
		}
	}
	
//	public void sortAppointments(byte day, byte month, short year) {
//		
//		short d = berechneTag(day, month, year);
//		short y = berechneJahr(year);
//		Link<Appointment> lauf1 = buckets[y][d]; // vorderstes Listenelement des zu sortierenden Tages
//		
//		if(lauf1 == null || lauf1.getNext() == null)
//			return;
//		
//		//////////alle ganztägigen nach vorne//////////
//		while(lauf1 != null) { // solange 'lauf1' die Liste nicht koimplett durchlaufen hat
//			
//			if(lauf1.getTermin().isAllday()) {
//				
//				Appointment tmp = lauf1.getTermin();
//				remove(tmp);
//				add(tmp);
//			}
//			
//			lauf1 = lauf1.getNext();
//		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		
//		Link<Appointment> lastNotAllday = lauf1; // als Grenzenmarkierung
//		
//		for(;lauf1.getNext() != null; lauf1 = lauf1.getNext()) { 
//			
//			
//			if(lauf1.getNext().getTermin().isAllday()) { // Ganztägige Termine nach hinten schieben
//				
//				
//				while(!( lastNotAllday.getNext() == null || lastNotAllday.getNext().getTermin().isAllday())) {
//					lastNotAllday = lastNotAllday.getNext();
//				}
//				
//				Link<Appointment> target = lauf1.getNext();
//				target.setNext(lastNotAllday.getNext());
//				lastNotAllday.setNext(target);
//				lauf1.setNext(lauf1.getNext().getNext());
//				lastNotAllday = target;
//				
//			} else { // frühster Termin nach vorne, etc
//				
//				
//				
//			}
//			
//		}
		
//		if(lauf1.getTermin().isAllday()) {
//			
//			lauf1.setNext(lastNotAllday.getNext());
//			lastNotAllday.setNext(lauf1);
//		}
//		
//	}

	public void removeAll(byte day, byte month, short year) {

		if (DateUtil.checkDate(day, month, year))
			buckets[berechneJahr(year)][berechneTag(day, month, year)] = null;
	}

	public int count(byte tag, byte monat, short jahr) { // Zählt Termine an einem Tag

		int ret = 0;
		if (DateUtil.checkDate(tag, monat, jahr)) {
			Link<Appointment> lauf = buckets[berechneJahr(jahr)][berechneTag(tag, monat, jahr)];
			while (lauf != null) {
				ret++;
				lauf = lauf.getNext();
			}
		}
		return ret;
	}

	public void print(byte day, byte month, short year) {

		if (DateUtil.checkDate(day, month, year)) {
			Link<Appointment> lauf = buckets[(berechneJahr(year))][berechneTag(day, month, year)];
			while (lauf != null) {
				System.out.println(lauf.getTermin().toString());
				lauf = lauf.getNext();
			}
		}
	}

	public void printAll() {

		for (short i = 0; i < 200; i++)
			for (short j = 0; j < 366; j++)
				if (buckets[i][j] != null) {
					// Man könnte hier auch einfach die print-methode aufrufen und über die getter
					// das Datum des Termins im bucket übergeben, aber dann würde es nicht immer
					// funktionieren, nachdem das Datum eines Termins nachträglich geändert wurde.
					Link<Appointment> lauf = buckets[i][j];
					while(lauf != null){
						System.out.println(lauf.getTermin().toString());
						lauf = lauf.getNext();
					}
				}
	}

	public boolean check() {

		for (short i = 0; i < 200; i++) // Jahre durchlaufen
			for (short j = 0; j < 366; j++) // Tage pro Jahr durchlaufen
				if (buckets[i][j] != null) { // terminfreie Tage überspringen
					Link<Appointment> lauf = buckets[i][j];
					while (lauf != null) { // Terminliste des Tages durchlaufen
						if (i != berechneJahr(lauf.getTermin().getYear())
								|| j != berechneTag(lauf.getTermin().getDay(), lauf.getTermin().getMonth(),
										lauf.getTermin().getYear()))
							return false;
						lauf = lauf.getNext();
					}
				}
		return true; // Wenn kein Termin in einem falschen Bucket gefunden wurde
	}

	public short berechneTag(short day, byte month, short year) {

		// Berechnung der Stelle an zweiter Array-Dimension
		for (byte i = 0; i < month; i++)
			day += DateUtil.daysOfMonth(i, year);
		return day;
	}

	public short berechneJahr(short year) {

		// Berechnung der Stelle an erster Array-Dimension
		return (short) (year - 1900);
	}
	
	public Link<Appointment> getBucket(byte day, byte month, short year) {
		
		return this.buckets[berechneJahr(year)][berechneTag(day, month, year)];
	}
}
