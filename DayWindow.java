import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class DayWindow extends JDialog {
	
	byte tag;
	byte monat;
	short jahr;
	Appointments termine;
	MyOtherTableModel tagesplanModel;
	CalendarWindow cw;

	public DayWindow(CalendarWindow cw, Appointments termine, byte tag, byte monat, short jahr) {

		super((JFrame) null, "Tagesansicht", true);
		this.cw = cw;
		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
		this.termine = termine;
		Container contentPane = getContentPane(); // NACHFRAGEN!
		Container unten = new JPanel();
		Container oben = new JPanel();
		oben.setLayout(new GridBagLayout());
		unten.setLayout(new FlowLayout());
		JLabel header = new JLabel(tag+1 + ". " + DateUtil.MONTH[monat] + " " + jahr);
		header.setFont(new Font(header.getText(), 0, 30));
		JButton adden = new JButton("Hinzufügen...");
		JButton del = new JButton("Löschen");
//		JButton sort = new JButton("Sortieren");
		
		tagesplanModel = new MyOtherTableModel(termine, tag, monat, jahr);
		JTable tagesplan = new JTable(tagesplanModel);
		JScrollPane tagesplanSP = new JScrollPane(tagesplan);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		
		oben.add(header, c); // FRAGEN: Kann ich das Label ohne eine verschachtelte JPanel zentrieren?
		contentPane.add(oben, BorderLayout.NORTH);
		contentPane.add(tagesplanSP, BorderLayout.CENTER);
		unten.add(adden);
		unten.add(del);
//		unten.add(sort);
//		
//		sort.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				
//				termine.sortAppointments(tag, monat, jahr);
//			}
//		});
		
		contentPane.add(unten, BorderLayout.SOUTH);
		
		adden.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				new AppointmentWindow(DayWindow.this, termine, tag, monat, jahr);
			}
		});
		
		del.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				switch(tagesplan.getSelectedRow()) {
				
				case -1: 
					JOptionPane.showMessageDialog(DayWindow.this, "Sie haben keinen Termin ausgewählt.");
					break;
				case 0: 
					termine.remove(termine.getBucket(tag, monat, jahr).getTermin());
					break;
				default: // Es wird auf den Aufruf der Remove-Methode verzichtet, um die Liste nicht doppelt durchlaufen zu müssen.
					Link<Appointment> lauf = termine.getBucket(tag, monat, jahr); // Der Code wäre dann zwar kompakter, aber auch viel ineffizienter.
					for(int i = 0; i < tagesplan.getSelectedRow()-1; i++)
						lauf = lauf.getNext();
					lauf.setNext(lauf.getNext().getNext());
				}
				
				DayWindow.this.updateView();
			}
		});
		
		tagesplan.addMouseListener(new MouseAdapter() { // funktioniert

			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
					Link<Appointment> lauf = termine.getBucket(tag, monat, jahr);
					for( int i = 0; i < tagesplan.getSelectedRow(); i++) {
						lauf = lauf.getNext();
					}
					new AppointmentWindow(DayWindow.this, termine, lauf.getTermin());
				}
			}
		});
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(400, 500);
		setVisible(true);
	}

	public void updateView() {
		
		tagesplanModel.updateModel(termine, tag, monat, jahr);
		cw.updateView();
	}
}
