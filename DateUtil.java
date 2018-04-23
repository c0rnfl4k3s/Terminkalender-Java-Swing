
public class DateUtil {

	public static final String[] MONTH = { "Januar", "Februar", "Maerz", "April", "Mai", "Juni", "Juli", "August",
			"September", "Oktober", "November", "Dezember" };
	public static final String[] DAY = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };

	public static boolean isLeapYear(short year) {
		
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
		// durch 4 teilbar und kein volles Jahrhundert, oder ein
		// "Ausnahme-Jahrhundert"
	}

	public static byte daysOfMonth(byte month, short year) {
		
		assert month < 12 && month >= 0;
		switch (month) {
		case 3:
		case 5:
		case 8:
		case 10:
			return 30;
		case 1:
			return (byte) (isLeapYear(year) ? 29 : 28);
		default:
			return 31;
		} // keine breaks notwendig, da Methode bei return verlassen wird
	}

	public static byte firstDayOfYear(short year) {
		
		 return (byte) ((5 * (((year - 1) & 3)) + 4 * ((year - 1) % 100) + 6 * ((year - 1) % 400)) % 7);
		 // '& 3' ist das Gleiche wie '% 4'
	}

	public static byte firstDayOfMonth(byte month, short year) {
		
		 short tmp = firstDayOfYear(year);
		
		 for (byte i = 0; i < month; i++) {
			 tmp += daysOfMonth(i, year);
		 }
		
		 return (byte)(tmp % 7);
	}

	public static void printMonth(byte month, short year) {
		
		System.out.println(MONTH[month] + " " + year);
		for (int i = 0; i < DAY.length; i++) {
			System.out.print(DAY[i] + " ");
		}
		System.out.println();
		// Alternativ: System.out.println("Mo Di Mi Do Fr Sa So");

		for (int i = 0; i < firstDayOfMonth(month, year); i++) // am richtigen
																// Wochentag
																// anfangen
			System.out.print("   ");
		for (int i = 1; i <= daysOfMonth(month, year); i++) {
			if (i < 10) // zusätzliches Leerzeichen vor die einzelnen Ziffern
				System.out.print(" ");
			System.out.print(i + " ");
			if ((i + firstDayOfMonth(month, year)) % 7 == 0) // Zeilenumbruch
																// nach jedem
																// Sonntag
				System.out.println();
		}
	}

	public static boolean checkDate(byte day, byte month, short year) {
		
		return (year >= 1900 && year <= 2099 && month >= 0 && month < 12 && day >= 0 && day < daysOfMonth(month, year));
		// Ist das Datum im Intervall, und existiert es überhaupt?
	}
}
