package uk.org.whitecottage.gantt.poi;

import static uk.org.whitecottage.poi.ShapeHelper.box;
import static uk.org.whitecottage.poi.ShapeHelper.ellipse;
import static uk.org.whitecottage.poi.ShapeHelper.text;

import java.awt.Color;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.xslf.usermodel.XSLFSlide;

import uk.org.whitecottage.gantt.Period;
import uk.org.whitecottage.gantt.TimeBar;

public class XSLFTimeBar extends TimeBar {
	protected double periodWidth;
	protected double width;
	protected XSLFSlide slide;
		
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(XSLFTimeBar.class.getCanonicalName());

	public XSLFTimeBar(Date start, Date end, XSLFSlide slide) {
		super(start, end);
		this.slide = slide;
		
		textSize = 6.0;
	}

	public XSLFTimeBar(Date start, Date end, Precision precision) {
		super(start, end, precision);
		
		textSize = 6.0;
	}

	public void drawTimeBar(double x0, double y0, double width, double height, Color color) {
		createPeriods();
		
		this.width = width;
		periodWidth = width / periods.size();
		double x = x0;
		
		for (Period period: periods) {
			box(slide, x, y0, periodWidth, height, period.getLabel(), textSize).setFillColor(color);
			x += periodWidth;
		}
	}
	
	public void drawBar(double x0, double y0, double height, Date startDate, Date endDate, Color color) {
		long barLength = barEnd.getTimeInMillis() - barStart.getTimeInMillis();
		double taskLength = ((double)(endDate.getTime() - startDate.getTime()) / ((double) barLength));
		double taskStart = ((double)(startDate.getTime() - barStart.getTimeInMillis()) / ((double) barLength));
		
		box(slide, x0 + taskStart * width, y0, taskLength * width, height).setFillColor(color);
	}

	public void drawMilestone(double x0, double y0, double size, Date date, String name, Color color) {
		long barLength = barEnd.getTimeInMillis() - barStart.getTimeInMillis();
		double taskStart = ((double)(date.getTime() - barStart.getTimeInMillis()) / ((double) barLength));
		
		ellipse(slide, x0 + taskStart * width, y0 - size, size, size).setFillColor(color);
		text(slide, x0 + taskStart * width, y0 - size, 200.0, size, name, textSize);
	}
}
