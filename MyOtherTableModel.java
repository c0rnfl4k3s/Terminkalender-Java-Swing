import javax.swing.table.AbstractTableModel;

public class MyOtherTableModel extends AbstractTableModel {

	private Appointments termine;
	private byte tag;
	private byte monat;
	private short jahr;

	public MyOtherTableModel(Appointments termine, byte tag, byte monat, short jahr) {

		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
		this.termine = termine;
	}

	public int getColumnCount() {

		return 3;
	}

	public int getRowCount() {

		return termine.count(tag, monat, jahr);
	}

	public Object getValueAt(int row, int col) {

		Link<Appointment> lauf = termine.getBucket(tag, monat, jahr);
		for(int i = 0; i < row ; i++) // Um bis zur aktuell betrachteten row zu kommen
			lauf = lauf.getNext();
		
		int start = lauf.getTermin().getStart();
		int length = lauf.getTermin().getLength();
		
		switch(col) {
		case 0: 
			return row+1;
		case 1:
			return lauf.getTermin().isAllday() ? "ganztägig" : (start / 60) + ":" // To-String Methode für Arme
					+ ((lauf.getTermin().getStart() % 60) < 10 ? "0" : "") + (start % 60)
					+ " - " + (start/60 + (((start%60) + length)/60)) + ":"
							+ (((length+ (start%60))%60) < 10 ? "0" : "") + ((length+ (start%60))%60);
		default:
			return lauf.getTermin().getSubject();
		}
	}

	public boolean isCellEditable(int row, int col) {

		return false;
	}

	public String getColumnName(int col) {
		
		switch (col) {
		case 0:
			return "#";
		case 1:
			return "Dauer";
		default:
			return "Betreff";
		}
	}
	
	public void updateModel(Appointments termine, byte tag, byte monat, short jahr) {
		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
		this.termine = termine;
		super.fireTableDataChanged();
	}

}
