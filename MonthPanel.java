import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JPanel;

public class MonthPanel extends JPanel {

	Appointments termine;
	byte month;
	short year;
	int breiteTag;
	int hoeheTag;
	CalendarWindow cw;
	Day[] tage = new Day[31];
	int start;
	int anzahlReihen;

	public MonthPanel(CalendarWindow cw, Appointments termine) {

		this.termine = termine;
		this.cw = cw;
		this.month = cw.monat;
		this.year = cw.jahr;
		this.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {

					if (MonthPanel.this.hitTest(e.getX(), e.getY()) >= 0)
						new DayWindow(cw, termine, MonthPanel.this.hitTest(e.getX(), e.getY()), month, year);
				}
			}
		});
		
		setMonth(cw.monat, cw.jahr);

		// Adapterklassen
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent arg0) {
				
				adjustLayout();
			}
		});
	}

	public void setMonth(byte month, short year) { // wird in CW bei Veränderungen aufgerufen

		this.month = month;
		this.year = year;
		start = DateUtil.firstDayOfMonth(month, year);
		anzahlReihen = (start + DateUtil.daysOfMonth(month, year)) == 28 ? 4
				: ((start + DateUtil.daysOfMonth(month, year)) >= 36 ? 6 : 5);

		adjustLayout();
	}

	public void adjustLayout() {

		breiteTag = (this.getWidth() - 4) / 7;
		hoeheTag = (this.getHeight() - 36) / (anzahlReihen);
		int laufx = 1 + start * breiteTag;
		int laufy = 33;

		for (int i = 0; i < DateUtil.daysOfMonth(month, year); i++) {

			if ((i + start) % 7 == 0) {
				laufx = 1;
				if (start != 0 || i > 0)
					laufy += hoeheTag;
			}
			tage[i] = new Day();
			tage[i].x = laufx;
			tage[i].y = laufy;
			tage[i].terminVorhanden = termine.getBucket((byte) i, month, year) != null;
			laufx += breiteTag;
		}
		
		repaint();
	}

	protected void paintComponent(final Graphics g) {

		super.paintComponent(g);
		g.setFont(new Font("Wochentage und Zahlen", 17, 18)); // passt gut
		
		boolean aktuell = year == (short)(LocalDate.now().getYear()) && month == (short)(LocalDate.now().getMonthValue()-1);

		for (int i = 0; i < 7; i++) { // Wochentage
			g.drawString(DateUtil.DAY[i], this.getWidth() / 7 * i + this.getWidth() / 14 - (7 + i), 20);
		}
		for (int i = 0; i < DateUtil.daysOfMonth(month, year); i++) { // Tage nummerieren
			if(aktuell && i+1 == LocalDate.now().getDayOfMonth()) {
				g.setColor(new Color(150, 150, 150));
				g.fillRect(tage[i].x + 1, tage[i].y, breiteTag, hoeheTag);
				g.setColor(Color.BLACK);
			}
			g.drawRect(tage[i].x + 1, tage[i].y, breiteTag, hoeheTag);
			if (tage[i].terminVorhanden) {
				g.setColor(Color.BLUE);
				g.drawRect(tage[i].x + 3, tage[i].y + 2, breiteTag - 4, hoeheTag - 4);
			}
			else if (tage[i].x > this.getWidth() / 7 * 5 - 10)
				g.setColor(Color.RED);
			g.drawString((i < 9 ? " " : "") + (i + 1), tage[i].x + this.getWidth() / 14 - 10,
					tage[i].y + this.getHeight() / (2 * anzahlReihen) + 5);
			g.setColor(Color.BLACK);
		}
	}

	public class Day { // Als "struct" - Ersatz

		public boolean terminVorhanden = false;
		public int x = 0;
		public int y = 0;

		public Day() { // nicht wirklich gebraucht

		}
	}

	public byte hitTest(int x, int y) { // wird bei Doppelklick aufgerufen

		for (byte i = 0; i < DateUtil.daysOfMonth(month, year); i++) {
			if (tage[i].x < x && tage[i].x + breiteTag > x && tage[i].y < y && tage[i].y + hoeheTag > y)
				return i;
		}
		return -1;
	}
}
