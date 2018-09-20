package uk.co.johnlewispartnership.gantt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public abstract class TimeBar {
	public enum Precision {
		YEARS,
		QUARTERS,
		MONTHS,
		WEEKS
	}

	protected Date start;
	protected Date end;
	protected Calendar barStart;
	protected Calendar barEnd;
	protected Precision precision;
	protected double textSize = 1.0;
	
	protected List<Period> periods;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TimeBar.class.getCanonicalName());

	public TimeBar(Date start, Date end) {
		this.start = start;
		this.end = end;
		precision = Precision.MONTHS;
		
		applyPrecision();
	}

	public TimeBar(Date start, Date end, Precision precision) {
		this.start = start;
		this.end = end;
		this.precision = precision;
		
		applyPrecision();
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
		
		applyPrecision();
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
		
		applyPrecision();
	}

	public Precision getPrecision() {
		return precision;
	}

	public void setPrecision(Precision precision) {
		this.precision = precision;

		applyPrecision();
	}
	
	protected void applyPrecision() {
		barStart = Calendar.getInstance();
		barStart.setTime(start);
		
		barEnd = Calendar.getInstance();
		barEnd.setTime(end);
		
		switch (precision) {
		case MONTHS:
			barStart.set(Calendar.DAY_OF_MONTH, 1);
			barEnd.set(Calendar.DAY_OF_MONTH, barEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		case QUARTERS:
			barStart.set(Calendar.DAY_OF_MONTH, 1);
			int quarter = barStart.get(Calendar.MONTH);
			barStart.set(Calendar.MONTH, quarter * 3);
			barEnd.set(Calendar.DAY_OF_MONTH, barEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
			int roll = 2 - barEnd.get(Calendar.MONTH) % 3;
			while (roll-- > 0) {
				barEnd.add(Calendar.MONTH, 1);
			}
			break;
		case WEEKS:
			barStart.set(Calendar.DAY_OF_WEEK, 0);
			barEnd.set(Calendar.DAY_OF_WEEK, barEnd.getActualMaximum(Calendar.DAY_OF_WEEK));
			break;
		case YEARS:
			barStart.set(Calendar.DAY_OF_YEAR, 1);
			barEnd.set(Calendar.DAY_OF_YEAR, barEnd.getActualMaximum(Calendar.DAY_OF_YEAR));
			break;
		default:
			break;		
		}
	}

	public double getTextSize() {
		return textSize;
	}

	public void setTextSize(double textSize) {
		this.textSize = textSize;
	}
	
	protected void createPeriods() {
		periods = new ArrayList<>();
		
		Calendar calendar = (Calendar) barStart.clone();
				
		while (calendar.before(barEnd)) {
			Period period = new Period();
			switch (precision) {
			case MONTHS:
				period.setLabel(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT_FORMAT, Locale.getDefault()));
				calendar.add(Calendar.MONTH, 1);
				break;
			case QUARTERS:
				period.setLabel("Q" + (calendar.get(Calendar.MONTH) / 3 + 1));
				calendar.add(Calendar.MONTH, 3);
				break;
			case WEEKS:
				period.setLabel("Week " + calendar.get(Calendar.WEEK_OF_YEAR));
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
				break;
			case YEARS:
				period.setLabel(calendar.getDisplayName(Calendar.YEAR, Calendar.LONG_FORMAT, Locale.getDefault()));
				calendar.add(Calendar.YEAR, 1);
				break;
			default:
				break;
			
			}
			
			periods.add(period);
		}		
	}

	public abstract void drawTimeBar(double x0, double y0, double width, double height, Color color);
	
	public abstract void drawBar(double x0, double y0, double height, Date startDate, Date endDate, Color color);
	
	public abstract void drawMilestone(double x0, double y0, double size, Date date, String name, Color color);
}
