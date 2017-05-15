package com.laamware.ejercito.doc.web.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Clase de utilidades para el manejo de fechas.
 * 
 * @author jgarcia@controltechcg.com
 * @since Feb 6, 2017
 *
 */
public final class DateUtil {

	/**
	 * Permite establecer la hora inicial o la hora final del día a una fecha.
	 * 
	 * @author jgarcia@controltechcg.com
	 * @since Feb 6, 2017
	 *
	 */
	public enum SetTimeType {
		/**
		 * Hora inicial.
		 */
		START_TIME,
		/**
		 * Hora final.
		 */
		END_TIME;
	}

	/**
	 * Establece en la instancia de fecha, la hora extrema del día, según el
	 * tipo seleccionado.
	 * 
	 * @param date
	 *            Instancia de fecha.
	 * @param timeType
	 *            Tipo de hora extrema del día.
	 * @return La instancia de fecha ingresada con una nueva hora, según lo
	 *         indicado en el parámetro del tipo. Si se indicó el tipo
	 *         {@code SetTimeType#START_TIME} la hora queda establecida como
	 *         00:00:00; si se indicó el tipo {@code SetTimeType#END_TIME} la
	 *         hora queda establecida como 23:59:59.
	 */
	public static <T extends java.util.Date> T setTime(T date, SetTimeType timeType) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);

		if (timeType.equals(SetTimeType.START_TIME)) {
			setTime(calendar, 00, 00, 00);
		} else {
			setTime(calendar, 23, 59, 59);
		}

		date.setTime(calendar.getTimeInMillis());
		return date;
	}

	/**
	 * Establece la hora indicada en la instancia del calendario. Los
	 * milisegundos quedan en valor cero.
	 * 
	 * @param calendar
	 *            Instancia de calendario.
	 * @param hourOfDay
	 *            Hora del día (00 a 23).
	 * @param minute
	 *            Minuto (00 a 59).
	 * @param second
	 *            Segundo (00 a 59).
	 */
	private static void setTime(Calendar calendar, int hourOfDay, int minute, int second) {
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 00);
	}
}
