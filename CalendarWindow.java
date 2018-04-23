import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarWindow extends JFrame  {

	byte monat;// = (byte) (LocalDate.now().getMonth().getValue() - 1);
	short jahr; //= (short) LocalDate.now().getYear();
//	MyTableModel tableModel;
	MonthPanel monthPanel;
	JLabel currentMonth;
	JButton links;
	JButton rechts;
	Appointments termine;
	String speicherort;
//	JSlider jahrWaehlen;
	JComboBox monatWaehlen;
	JTextField jahrWaehlen;

	public CalendarWindow(Appointments termine, short anzuzeigendesJahr, byte anzuzeigenderMonat, String speicherort) {

		this.termine = termine;
		this.monat = anzuzeigenderMonat;
		this.jahr = anzuzeigendesJahr;
		
		Container contentPane = getContentPane(); // Ohne diese Zeile gehört die obere Leiste mit zum Fenster
		Container oben = new JPanel();
		oben.setLayout(new GridBagLayout());
		setSize(403, 494); // x * y in Pixeln
		this.setMinimumSize(new Dimension(403, 300));
		setTitle("Kalender"); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Programm mit Klick auf "X" terminieren
		
		currentMonth = new JLabel(DateUtil.MONTH[monat] + " " + jahr);

		links = new JButton("<");
		rechts = new JButton(">");

//		JButton waehlen = new JButton("Wählen...");
		JButton heute = new JButton("heute");
		
		
//		jahrWaehlen = new JSlider();
//		jahrWaehlen.setMaximum(2100);
//		jahrWaehlen.setMinimum(1900);
//		jahrWaehlen.setMajorTickSpacing(50);
//		jahrWaehlen.setMinorTickSpacing(5);
//		jahrWaehlen.setPaintTicks(true);
//		jahrWaehlen.setPaintLabels(true);
		monatWaehlen = new JComboBox(DateUtil.MONTH);
		jahrWaehlen = new JTextField();
		
		monthPanel = new MonthPanel(CalendarWindow.this, termine);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4, 4, 4, 4);
		
		c.gridx = 1;
		c.gridy = 0;
		oben.add(links, c);

		c.gridx = 2;
		oben.add(currentMonth, c);
		
		c.gridx = 3;
		oben.add(rechts, c);
		
		c.gridy = 1;
		c.gridx = 1;
		oben.add(heute, c);
		
		c.gridx = 2;
//		oben.add(waehlen, c);
		oben.add(jahrWaehlen, c);
		
		c.gridx = 3;
		oben.add(monatWaehlen, c);
		
		contentPane.add(oben, BorderLayout.NORTH);
		contentPane.add(monthPanel, BorderLayout.CENTER);
		
		jahrWaehlen.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent a) {
				
//				if(Integer.parseInt(jahrWaehlen.getText()) >= 1900 && Integer.parseInt(jahrWaehlen.getText()) < 2100) {
					try {
					jahr = Short.parseShort(jahrWaehlen.getText());
					updateView();
					} catch ( Exception e) {
						JOptionPane.showMessageDialog(CalendarWindow.this, "Ungültiges Jahr.");
					}
//				}
			}
		});
		
		monatWaehlen.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				
				monat = (byte) monatWaehlen.getSelectedIndex();
				updateView();
			}
		});
		
//		jahrWaehlen.addChangeListener(new ChangeListener() {
//
//			public void stateChanged(ChangeEvent e) {
//				
//				jahr = (short)(jahrWaehlen.getValue() >= 2099 ? 2099 : jahrWaehlen.getValue());
//				updateView();
//			}
//		});
		
		heute.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				monat = (byte) (LocalDate.now().getMonth().getValue() -1);
				jahr = (short)(LocalDate.now().getYear());
				updateView();
			}
		});

		links.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (monat == 0) {
					monat = 11;
					jahr--;
				} else
					monat--;			
				updateView();
			}
		});

		rechts.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (monat == 11) {
					monat = 0;
					jahr++;
				} else
					monat++;
				updateView();
			}
		});

//		waehlen.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//					
//				Container datumAngeben = new JPanel();
//				datumAngeben.setLayout(new GridLayout(2, 2));
//				JLabel monat = new JLabel("Monat");
//				JTextField monatInput = new JTextField();
//				JLabel jahr = new JLabel("Jahr");
//				JTextField jahrInput = new JTextField();
//				datumAngeben.add(monat);
//				datumAngeben.add(monatInput);
//				datumAngeben.add(jahr);
//				datumAngeben.add(jahrInput);
//				
//				if (JOptionPane.showConfirmDialog(CalendarWindow.this, datumAngeben, "Datum angeben", JOptionPane.OK_CANCEL_OPTION,
//						JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
//
//					if (!jahrInput.getText().matches("[0-9]+") || !monatInput.getText().matches("[0-9]+") || 
//							Integer.parseInt(jahrInput.getText()) < 1900 || Integer.parseInt(jahrInput.getText()) > 2099 || 
//							Integer.parseInt(monatInput.getText()) < 1 || Integer.parseInt(monatInput.getText()) > 12) {
//						JOptionPane.showMessageDialog(CalendarWindow.this, "Ungültige Eingabe.");
//					} else {
//						CalendarWindow.this.monat = (byte) ((Byte.parseByte(monatInput.getText()))-1);
//						CalendarWindow.this.jahr = Short.parseShort(jahrInput.getText());
//						CalendarWindow.this.updateView();
//					}
//				}
//			}
//		});
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {

				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(speicherort));
					out.writeObject(termine);
					out.writeShort(jahr);
					out.writeByte(monat);
					out.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Es ist ein Fehler beim Speichern der Termine aufgetreten.");
				}
			}
			
		});

		updateView();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void updateView() {
		
		//tableModel.updateModel(monat, jahr);
		monthPanel.setMonth(monat, jahr);
		monthPanel.repaint();
		currentMonth.setText(DateUtil.MONTH[monat] + " " + jahr);
		rechts.setEnabled(!(monat == 11 && jahr == 2099));
		links.setEnabled(!(monat == 0 && jahr == 1900));
		jahrWaehlen.setText(""+jahr);
		monatWaehlen.setSelectedIndex(monat);
	}
}
