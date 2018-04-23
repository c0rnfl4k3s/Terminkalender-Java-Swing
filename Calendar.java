import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.io.*;
import java.time.LocalDate;


public class Calendar {
	
	
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		Appointments termine;
		short anzuzeigendesJahr;
		byte anzuzeigenderMonat;
		String speicherort = "KalenderTermine.ser";

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(speicherort));
			termine = (Appointments)in.readObject();
			anzuzeigendesJahr = in.readShort();
			anzuzeigenderMonat = in.readByte();
			in.close(); 
			
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, "Es ist ein Fehler beim Einlesen der Termindatei aufgetreten.\n"
					+ "Das Programm wird nun mit Testdaten ausgeführt.");
			
			termine = new Appointments();
			anzuzeigendesJahr = (short) LocalDate.now().getYear();
			anzuzeigenderMonat = (byte) (LocalDate.now().getMonth().getValue() - 1);
			
			Appointment sport = new Appointment("Sport", (byte) 27, (byte) 1, (short) 2099, false, (short) 600, (short) 85);
			Appointment gespraech = new Appointment("Vorstellungsgespräch", (byte) 27, (byte) 1, (short) 2099, false,
					(short) 1005, (short) 90);
			Appointment klausur = new Appointment("Klausur", (byte) 27, (byte) 1, (short) 2099, false, (short) 480,
					(short) 135);
			Appointment bday = new Appointment("Geburtstag", (byte) 27, (byte) 1, (short) 2099, true, (short) 0, (short) 0);
			Appointment silvester = new Appointment("Silvester", (byte) 30, (byte) 11, (short) 2099, true, (short) 0,
					(short) 0);
			Appointment bandprobe = new Appointment("Bandprobe", (byte) 2, (byte) 0, (short) 2099, false, (short) 1020,
					(short) 180);
			Appointment arzt = new Appointment("Arzt", (byte) 13, (byte) 5, (short) 2099, false, (short) 800, (short) 60);
			Appointment bib = new Appointment("Deadline Bibliotheksrückgabe", (byte) 13, (byte) 5, (short) 2099, true,
					(short) 0, (short) 0);
			Appointment besichtigung = new Appointment("Wohnungsbesichtigung", (byte) 9, (byte) 6, (short) 2099, false,
					(short) 820, (short) 70);
			Appointment konzert = new Appointment("Billy Talent Live", (byte) 19, (byte) 9, (short) 2099, false,
					(short) 1020, (short) 150);
			
			termine.add(sport);
			termine.add(gespraech);
			termine.add(klausur);
			termine.add(bday);
			termine.add(silvester);
			termine.add(bandprobe);
			termine.add(arzt);
			termine.add(bib);
			termine.add(besichtigung);
			termine.add(konzert);
		}
		
		new CalendarWindow(termine, anzuzeigendesJahr, anzuzeigenderMonat, speicherort);
	}
}
