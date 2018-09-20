package uk.co.johnlewispartnership.poi;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.apache.poi.sl.usermodel.ShapeType;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

public class ShapeHelper {
	private static final double FONT_HEIGHT_FACTOR = 1.0;
	private static final double FONT_WIDTH_FACTOR = 0.6;	
	
	private ShapeHelper() {
	}
	
	public static XSLFConnectorShape line(XSLFSlide slide, double x1, double y1, double x2, double y2) {
		double w = x2 - x1;
		double h = y2 - y1;
		double x = x1;
		double y = y1;
		
		XSLFConnectorShape line = slide.createConnector();
		
		if (w < 0.0) {
			w = -w;
			x -= w;
			line.setFlipHorizontal(true);
		}
		
		if (h < 0.0) {
			h = -h;
			y -= h;
			line.setFlipVertical(true);
		}
		
		line.setAnchor(new Rectangle2D.Double(x, y, w, h));
		line.setLineColor(Color.black);
		
		return line;
	}
	
	public static XSLFTextBox text(XSLFSlide slide, double x, double y, double w, double h, String text, double size) {
		
		XSLFTextBox simpleText = slide.createTextBox();
		simpleText.setVerticalAlignment(VerticalAlignment.MIDDLE);
		simpleText.setTopInset(0);
		simpleText.setBottomInset(0);
		simpleText.clearText();
		
		XSLFTextParagraph paragraph = simpleText.addNewTextParagraph();
		paragraph.setTextAlign(TextAlign.LEFT);
		
		XSLFTextRun run = paragraph.addNewTextRun();
		run.setText(text);
		run.setFontSize(size);
		
		simpleText.setAnchor(new Rectangle2D.Double(x, y, w, h));
		
		return simpleText;
	}

	public static XSLFTextBox simpleText(XSLFSlide slide, double x, double y, String text, double size) {
		
		XSLFTextBox simpleText = slide.createTextBox();
		simpleText.setVerticalAlignment(VerticalAlignment.MIDDLE);
		simpleText.setTopInset(0);
		simpleText.setBottomInset(0);
		simpleText.clearText();
		
		XSLFTextParagraph paragraph = simpleText.addNewTextParagraph();
		paragraph.setTextAlign(TextAlign.CENTER);
		
		XSLFTextRun run = paragraph.addNewTextRun();
		run.setText(text);
		run.setFontSize(size);
		
		double w = size * text.length() * FONT_WIDTH_FACTOR;
		double h = size * FONT_HEIGHT_FACTOR;
		
		simpleText.setAnchor(new Rectangle2D.Double((x - w / 2), (y - h / 2), w, h));
		
		return simpleText;
	}

	public static XSLFTextBox simpleText(XSLFSlide slide, double x, double y, String text, double size, double rotation, TextAlign align) {
		
		XSLFTextBox simpleText = slide.createTextBox();
		simpleText.setVerticalAlignment(VerticalAlignment.MIDDLE);
		simpleText.setTopInset(0);
		simpleText.setBottomInset(0);
		simpleText.clearText();
		
		XSLFTextParagraph paragraph = simpleText.addNewTextParagraph();
		paragraph.setTextAlign(align);
		
		XSLFTextRun run = paragraph.addNewTextRun();
		run.setText(text);
		run.setFontSize(size);
		
		double w = size * text.length() * FONT_WIDTH_FACTOR;
		double h = size * FONT_HEIGHT_FACTOR;
				
		simpleText.setAnchor(new Rectangle2D.Double((x - w / 2), (y - h / 2), w, h));
		
		simpleText.setRotation(rotation);
		
		return simpleText;
	}

	public static XSLFTextBox box(XSLFSlide slide, double x, double y, double w, double h, String text, double size) {
		
		XSLFTextBox simpleText = slide.createTextBox();
		simpleText.setVerticalAlignment(VerticalAlignment.MIDDLE);
		simpleText.setTopInset(0);
		simpleText.setBottomInset(0);
		simpleText.clearText();
		
		XSLFTextParagraph paragraph = simpleText.addNewTextParagraph();
		paragraph.setTextAlign(TextAlign.CENTER);
		
		XSLFTextRun run = paragraph.addNewTextRun();
		run.setText(text);
		run.setFontSize(size);
		
		simpleText.setAnchor(new Rectangle2D.Double(x, y, w, h));
		simpleText.setLineColor(Color.BLACK);
		
		return simpleText;
	}

	public static XSLFTextBox box(XSLFSlide slide, double x, double y, double w, double h) {
		
		XSLFTextBox simpleText = slide.createTextBox();

		simpleText.setAnchor(new Rectangle2D.Double(x, y, w, h));
		simpleText.setLineColor(Color.BLACK);
		
		return simpleText;
	}

	public static XSLFAutoShape circle(XSLFSlide slide, double x, double y, double d) {
		
		return ellipse(slide, x, y, d, d);
	}

	public static XSLFAutoShape ellipse(XSLFSlide slide, double x, double y, double w, double h) {
		
		XSLFAutoShape ellipse = slide.createAutoShape();
		
		ellipse.setShapeType(ShapeType.ELLIPSE);
		ellipse.setAnchor(new Rectangle((int) (x - w / 2), (int) (y - w / 2), (int) w, (int) h));
		ellipse.setLineColor(Color.black);
		
		
		return ellipse;
	}
}
