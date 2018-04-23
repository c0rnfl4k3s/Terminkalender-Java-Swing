import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentWindow extends JDialog {

	public AppointmentWindow(DayWindow dw, Appointments termine, byte tag, byte monat, short jahr) {

		super((JFrame) null, "Neuer Termin", true);
		JLabel betreffLabel = new JLabel("Betreff");
		JLabel tagLabel = new JLabel("Tag");
		JLabel monatLabel = new JLabel("Monat");
		JLabel jahrLabel = new JLabel("Jahr");
		JLabel beginn = new JLabel("Beginn");
		JLabel ende = new JLabel("Ende");
		JTextField betreffInput = new JTextField();
		JTextField tagInput = new JTextField(""+(tag + 1));
		JTextField monatInput = new JTextField(""+(monat + 1));
		JTextField jahrInput = new JTextField(""+jahr);
		JTextField beginnInput = new JTextField();
		JTextField endeInput = new JTextField();
		JCheckBox ganztaegig = new JCheckBox("ganztägig");
		JButton abbrechen = new JButton("Abbrechen");
		JButton ok = new JButton("OK");
		ok.setBackground(new Color(20, 100, 150));

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(4, 4, 4, 4);

		c.gridx = 1;
		c.gridy = 0;
		contentPane.add(betreffLabel, c);
		c.gridy = 1;
		contentPane.add(tagLabel, c);
		c.gridy = 2;
		contentPane.add(monatLabel, c);
		c.gridy = 3;
		contentPane.add(jahrLabel, c);
		c.gridy = 4;
		contentPane.add(ganztaegig, c);
		c.gridy = 5;
		contentPane.add(beginn, c);
		c.gridy = 6;
		contentPane.add(ende, c);
		c.gridx = 2;
		c.gridy = 0;
		contentPane.add(betreffInput, c);
		c.gridy = 1;
		contentPane.add(tagInput, c);
		c.gridy = 2;
		contentPane.add(monatInput, c);
		c.gridy = 3;
		contentPane.add(jahrInput, c);
		c.gridy = 5;
		contentPane.add(beginnInput, c);
		c.gridy = 6;
		contentPane.add(endeInput, c);
		c.gridy = 7;
		contentPane.add(abbrechen, c);
		c.gridx = 3;
		contentPane.add(ok, c);
		
		getRootPane().setDefaultButton(ok); // OK wird auch von der Enter-Taste aktiviert

		abbrechen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				AppointmentWindow.this.dispose();
			}
		});

		ganztaegig.addActionListener(new ActionListener() { 

			@Override
			public void actionPerformed(ActionEvent e) {

				beginnInput.setEnabled(!beginnInput.isEnabled());
				endeInput.setEnabled(!endeInput.isEnabled());
			}
		});

		ok.addActionListener(new ActionListener() 
		{

			public void actionPerformed(ActionEvent e) 
			{
				
				if (!tagInput.getText().matches("[0-9]+") || !jahrInput.getText().matches("[0-9]+") || !monatInput.getText().matches("[0-9]+")
						|| !DateUtil.checkDate((byte)(Integer.parseInt(tagInput.getText())-1), (byte)(Integer.parseInt(monatInput.getText())-1),
							(short)(Integer.parseInt(jahrInput.getText())))) 
				{
					JOptionPane.showMessageDialog(AppointmentWindow.this, "Ungültiges Datum.");
				}
				else {
					if (ganztaegig.isSelected()) {
						termine.add((new Appointment(betreffInput.getText(),
								(byte)(Byte.parseByte(tagInput.getText())-1), (byte)(Byte.parseByte(monatInput.getText())-1),
								Short.parseShort(jahrInput.getText()), true, (short) 0, (short) 0)));
						dw.updateView();
						AppointmentWindow.this.dispose();
					} else {
						if (!isTimeInputValid(beginnInput.getText(), endeInput.getText())) {
							JOptionPane.showMessageDialog(AppointmentWindow.this,
									"Ungültige Uhrzeit. Bitte beachten Sie, dass nur Zeitangaben im Format \"HH:MM\" "
									+ "\n(mit 'H' und 'M' als Ziffern zwischen 0 und 9) akzeptiert werden.");
						} else {
							termine.add(new Appointment(betreffInput.getText(),
											(byte) (Byte.parseByte(tagInput.getText()) - 1),
											(byte) (Byte.parseByte(monatInput.getText()) - 1),
											Short.parseShort(jahrInput.getText()), false,
											timeConverter(beginnInput.getText()),
											(short) (timeConverter(endeInput.getText())
													- timeConverter(beginnInput.getText()))));
							dw.updateView();
							
							AppointmentWindow.this.dispose();
						}
					}
				}
			}
			
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public AppointmentWindow(DayWindow dw, Appointments termine, Appointment termin) {

		super((JFrame) null, "Termin bearbeiten", true);
		JLabel betreffLabel = new JLabel("Betreff");
		JLabel tagLabel = new JLabel("Tag");
		JLabel monatLabel = new JLabel("Monat");
		JLabel jahrLabel = new JLabel("Jahr");
		JLabel beginn = new JLabel("Beginn");
		JLabel ende = new JLabel("Ende");
		JTextField betreffInput = new JTextField(termin.getSubject());
		JTextField tagInput = new JTextField("" + (termin.getDay() + 1));
		JTextField monatInput = new JTextField("" + (termin.getMonth() + 1));
		JTextField jahrInput = new JTextField("" + termin.getYear());
		JCheckBox ganztaegig = new JCheckBox("ganztägig");
		JTextField beginnInput = new JTextField();
		JTextField endeInput = new JTextField();
		
		if(!termin.isAllday()) {
			beginnInput.setText(
					(termin.getStart() / 60) + ":" + ((termin.getStart() % 60) < 10 ? "0" : "") + (termin.getStart() % 60));
			endeInput.setText(
					(termin.getStart() / 60 + (((termin.getStart() % 60) + termin.getLength()) / 60)) + ":"
							+ (((termin.getLength() + (termin.getStart() % 60)) % 60) < 10 ? "0" : "")
							+ ((termin.getLength() + (termin.getStart() % 60)) % 60));
		} else {
			ganztaegig.setSelected(true);
			beginnInput.setEnabled(false);
			endeInput.setEnabled(false);
		}
		
		JButton abbrechen = new JButton("Abbrechen");
		JButton ok = new JButton("OK");
		ok.setBackground(new Color(20, 100, 150));

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(4, 4, 4, 4);

		c.gridx = 1;
		c.gridy = 0;
		contentPane.add(betreffLabel, c);
		c.gridy = 1;
		contentPane.add(tagLabel, c);
		c.gridy = 2;
		contentPane.add(monatLabel, c);
		c.gridy = 3;
		contentPane.add(jahrLabel, c);
		c.gridy = 4;
		contentPane.add(ganztaegig, c);
		c.gridy = 5;
		contentPane.add(beginn, c);
		c.gridy = 6;
		contentPane.add(ende, c);
		c.gridx = 2;
		c.gridy = 0;
		contentPane.add(betreffInput, c);
		c.gridy = 1;
		contentPane.add(tagInput, c);
		c.gridy = 2;
		contentPane.add(monatInput, c);
		c.gridy = 3;
		contentPane.add(jahrInput, c);
		c.gridy = 5;
		contentPane.add(beginnInput, c);
		c.gridy = 6;
		contentPane.add(endeInput, c);
		c.gridy = 7;
		contentPane.add(abbrechen, c);
		c.gridx = 3;
		contentPane.add(ok, c);
		
		getRootPane().setDefaultButton(ok);

		abbrechen.addActionListener(new ActionListener() { 

			@Override
			public void actionPerformed(ActionEvent e) {

				AppointmentWindow.this.dispose();
			}
		});

		ganztaegig.addActionListener(new ActionListener() { 

			@Override
			public void actionPerformed(ActionEvent e) {

				beginnInput.setEnabled(!beginnInput.isEnabled());
				endeInput.setEnabled(!endeInput.isEnabled());
			}
		});
		
		ok.addActionListener(new ActionListener() { // TABELLE UPDATEN!!!

			public void actionPerformed(ActionEvent e) {
				
				if (!tagInput.getText().matches("[0-9]+") || !jahrInput.getText().matches("[0-9]+") || !monatInput.getText().matches("[0-9]+")
						|| !DateUtil.checkDate((byte)(Integer.parseInt(tagInput.getText())-1), (byte)(Integer.parseInt(monatInput.getText())-1),
							(short)(Integer.parseInt(jahrInput.getText())))) {
					JOptionPane.showMessageDialog(AppointmentWindow.this, "Ungültiges Datum.");
				}
				else {
					if (ganztaegig.isSelected()) {
						termine.remove(termin);
						termine.add((new Appointment(betreffInput.getText(),
								(byte)(Byte.parseByte(tagInput.getText())-1), (byte)(Byte.parseByte(monatInput.getText())-1),
								Short.parseShort(jahrInput.getText()), true, (short) 0, (short) 0)));
						dw.updateView();
						AppointmentWindow.this.dispose();
					} else {
						if (!isTimeInputValid(beginnInput.getText(), endeInput.getText())) {
							JOptionPane.showMessageDialog(AppointmentWindow.this,
									"Ungültige Uhrzeit. Bitte beachten Sie, dass nur Zeitangaben im Format \"HH:MM\" "
									+ "\n(mit 'H' und 'M' als Ziffern zwischen 0 und 9) akzeptiert werden.");
						} else {
							termine.remove(termin);
							termine.add(new Appointment(betreffInput.getText(),
											(byte) (Byte.parseByte(tagInput.getText()) - 1),
											(byte) (Byte.parseByte(monatInput.getText()) - 1),
											Short.parseShort(jahrInput.getText()), false,
											timeConverter(beginnInput.getText()),
											(short) (timeConverter(endeInput.getText())
													- timeConverter(beginnInput.getText()))));
							dw.updateView();
							AppointmentWindow.this.dispose();
						}
					}
				}
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private short timeConverter(String input) {
		
		return (short)(Integer.parseInt(input.substring(0, 2)) * 60 + Integer.parseInt(input.substring(3)));
	}

	private boolean isTimeInputValid(String beginn, String ende) {

		return !(beginn.length() != 5 || !(beginn.substring(0, 2) + beginn.substring(3)).matches("[0-9]+")
				|| beginn.charAt(2) != ':' || Integer.parseInt(beginn.substring(3)) > 59
				|| ende.length() != 5 || !(ende.substring(0, 2) + ende.substring(3)).matches("[0-9]+") 
				|| ende.charAt(2) != ':' || Integer.parseInt(ende.substring(3)) > 59
				|| timeConverter(ende) - timeConverter(beginn) <= 0 // negative Terminlänge ausschließen
				|| timeConverter(ende) >= 1440 /* Überschreitung von Mitternacht ausschließen */);
	}
}
