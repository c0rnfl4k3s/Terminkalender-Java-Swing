//import javax.swing.*;
import javax.swing.table.*;

public class MyTableModel extends AbstractTableModel {
	
	private Appointments termine;
	private byte monat;
	private short jahr;
	
	public MyTableModel(Appointments termine, byte monat, short jahr) {
		
		this.termine = termine;
		this.monat = monat;
		this.jahr = jahr;
	}

	public int getColumnCount() {
		
		return 2;
	}

	public int getRowCount() {
		
		return DateUtil.daysOfMonth(monat, jahr);
	}

	public Object getValueAt(int row, int col) {
		
		if(col == 0)
			return row+1 + ".";
		else
		switch(termine.count((byte)row, monat, jahr)){
			case 0: 
				return null;
			case 1: 
				return "1 Termin"; 
			default: 
				return "" + termine.count((byte)row, monat, jahr) + " Termine";
		}
	}

	public boolean isCellEditable(int row, int col) {
		
		return false;
	}
	
	public String getColumnName(int col) {
		return col == 0 ? "Tag" : "Termine";
	}
	
	public void updateModel(byte monat, short jahr) {
		this.monat = monat;
		this.jahr = jahr;
		super.fireTableDataChanged();
	}
	
}
