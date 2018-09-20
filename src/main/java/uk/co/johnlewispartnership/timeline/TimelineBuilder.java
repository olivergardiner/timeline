package uk.co.johnlewispartnership.timeline;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;

import uk.co.johnlewispartnership.gantt.TimeBar;
import uk.co.johnlewispartnership.gantt.poi.POITimeBar;
import uk.co.johnlewispartnership.msproject.MSProject;
import uk.co.johnlewispartnership.msproject.Milestone;
import uk.co.johnlewispartnership.msproject.Task;

import static uk.co.johnlewispartnership.poi.ShapeHelper.text;

public class TimelineBuilder {
	protected MSProject project;
	
	protected double pageWidth;
	protected double pageHeight;

	protected double x0;
	protected double y0;
	protected double width;
	
	protected static final double MAX_RADIUS = 50;
	protected static final double MIN_RADIUS = 5;
	
	protected Theme theme;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TimelineBuilder.class.getCanonicalName());

	public TimelineBuilder(MSProject project) {
		this.project = project;
		
		theme = new Theme();
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public  void writeSlides(FileOutputStream output) throws IOException {
		XMLSlideShow pptx = new XMLSlideShow();
		
		buildSlide(pptx.createSlide());
		
		try {
			pptx.write(output);
		} finally {
			pptx.close();
		}
	}

	protected void buildSlide(XSLFSlide slide) {
		Dimension pageSize = slide.getSlideShow().getPageSize();
		pageWidth = pageSize.width;
		pageHeight = pageSize.height;
		
		width = pageWidth * 0.9;
		x0 = (pageWidth - width) / 2;
		y0 = 100;
		
		double labelWidth = theme.getStreamLabelWidth() + theme.getTaskLabelWidth();

		TimeBar bar = new POITimeBar(project.getStartDate(), project.getEndDate(), slide);
		bar.setTextSize(theme.getBarTextSize());
		bar.drawTimeBar(x0 + labelWidth, y0, width - labelWidth, theme.getTimeBarHeight(),theme.getBarColor());
		
		buildTasks(slide, bar, project.getTasks(), x0, y0 + theme.getTimeBarHeight() + theme.getTaskSpacing());
		
		for (Milestone milestone: project.getMilestones()) {
			bar.drawMilestone(x0 + 100.0, y0, theme.getMilestoneSize(), milestone.getDate(), milestone.getName(), theme.getMilestoneColor());
		}
	}
	
	protected void buildTasks(XSLFSlide slide, TimeBar bar, List<Task> taskList, double x, double y) {		
		double taskOffset = theme.getTaskSpacing() + theme.getTaskHeight();
		double streamSpacing = theme.streamSpacing;
		double labelWidth = theme.getStreamLabelWidth() + theme.getTaskLabelWidth();
		
		for (Task task: taskList) {
			double streamHeight = task.getChildTasks().size() * taskOffset;
			Color streamColor = theme.getNextStreamColor();
			
			XSLFTextBox stream = text(slide, x, y, theme.getStreamLabelWidth(), streamHeight, task.getName(), theme.getStreamTextSize());
			stream.setFillColor(streamColor);
			XSLFTextBox streamPlan = text(slide, x + theme.getStreamLabelWidth(), y, width - theme.getStreamLabelWidth(), streamHeight, "", theme.getTaskTextSize());
			streamPlan.setFillColor(streamColor);
			
			for (Task subTask: task.getChildTasks()) {
				text(slide, x + theme.getStreamLabelWidth(), y + theme.getTaskSpacing() / 2, theme.getTaskLabelWidth(), theme.getTaskHeight(), subTask.getName(), theme.getTaskTextSize()).setLineColor(streamColor.darker());
				bar.drawBar(x + labelWidth, y + theme.getTaskSpacing() / 2, theme.getTaskHeight(), subTask.getStartDate(), subTask.getEndDate(), streamColor.darker());
				y += taskOffset;
			}
			
			y += streamSpacing;
		}
	}
}
