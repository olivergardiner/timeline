package uk.org.whitecottage.timeline;

import static uk.org.whitecottage.poi.ShapeHelper.*;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;

import uk.org.whitecottage.gantt.TimeBar;
import uk.org.whitecottage.gantt.poi.XSLFTimeBar;
import uk.org.whitecottage.msproject.MSProject;
import uk.org.whitecottage.msproject.Milestone;
import uk.org.whitecottage.msproject.Task;

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
		
		createSlides(pptx, project.getTasks());
		
		try {
			pptx.write(output);
		} finally {
			pptx.close();
		}
	}
	
	protected void createSlides (XMLSlideShow pptx, List<Task> taskList) {
		buildSlide(pptx.createSlide(), taskList);
		
		for (Task task: taskList) {
			List<Task> childTaskList = task.getChildTasks();
			if (childTaskList.size() > 0) {
				createSlides(pptx, childTaskList);
			}
		}
	}

	public  void writeSlides(FileOutputStream output, int part, int subPart) throws IOException {
		XMLSlideShow pptx = new XMLSlideShow();
		
		List<Task> taskList = null;
		
		if (part == 0) {
			taskList = project.getTasks();
		} else {
			taskList = project.getTasks().get(part - 1).getChildTasks();
			if (subPart > 0) {
				taskList = taskList.get(subPart -1).getChildTasks();
			}
		}
		
		buildSlide(pptx.createSlide(), taskList);
		
		try {
			pptx.write(output);
		} finally {
			pptx.close();
		}
	}

	protected void buildSlide(XSLFSlide slide, List<Task> taskList) {
		Dimension pageSize = slide.getSlideShow().getPageSize();
	
		pageSize.width = pageSize.height * 16 / 9;
		slide.getSlideShow().setPageSize(pageSize);
		
		pageWidth = pageSize.width;
		pageHeight = pageSize.height;
		
		width = pageWidth * 0.9;
		x0 = (pageWidth - width) / 2;
		y0 = 100;
		
		double labelWidth = theme.getStreamLabelWidth() + theme.getTaskLabelWidth();
		
		TimeBar bar = new XSLFTimeBar(project.getStartDate(), project.getEndDate(), slide);
		bar.setPrecision(TimeBar.Precision.MONTHS);
		bar.setTextSize(theme.getBarTextSize());
		bar.drawTimeBar(x0 + labelWidth, y0, width - labelWidth, theme.getTimeBarHeight(),theme.getBarColor());
				
		buildTasks(slide, bar, taskList, x0, y0 + theme.getTimeBarHeight() + theme.getStreamSpacing() + theme.getTaskSpacing());
		
		int offset = 0;
		for (Milestone milestone: project.getMilestones()) {			
			bar.drawMilestone(x0 + theme.getStreamLabelWidth() + theme.getTaskLabelWidth(), y0 - offset * theme.getMilestoneSize(), theme.getMilestoneSize(), milestone.getDate(), milestone.getName(), theme.getMilestoneColor());
			offset++;
			offset %= 3;
		}
	}
	
	protected void buildTasks(XSLFSlide slide, TimeBar bar, List<Task> taskList, double x, double y) {		
		double taskOffset = theme.getTaskSpacing() + theme.getTaskHeight();
		double streamSpacing = theme.streamSpacing;
		double labelWidth = theme.getStreamLabelWidth() + theme.getTaskLabelWidth();
		
		for (Task task: taskList) {
			double streamHeight = task.getChildTasks().size() * taskOffset;
			Color streamColor = theme.getNextStreamColor();
			
			String streamText = task.getName();
			XSLFTextBox stream = text(slide, x, y, theme.getStreamLabelWidth(), streamHeight, streamText, theme.getStreamTextSize());
			stream.setFillColor(streamColor);
			XSLFTextBox streamPlan = text(slide, x + theme.getStreamLabelWidth(), y, width - theme.getStreamLabelWidth(), streamHeight, "", theme.getTaskTextSize());
			streamPlan.setFillColor(streamColor);
			
			double barOffset = (theme.getTaskHeight() - theme.getTaskBarHeight()) / 2;
			
			for (Task subTask: task.getChildTasks()) {
				String taskText = truncate(subTask.getName(), theme.getTaskTextSize(), 2 * theme.getTaskLabelWidth());
				text(slide, x + theme.getStreamLabelWidth(), y + theme.getTaskSpacing() / 2, theme.getTaskLabelWidth(), theme.getTaskHeight(), taskText, theme.getTaskTextSize()).setLineColor(streamColor.darker());
				bar.drawBar(x + labelWidth, y + barOffset + theme.getTaskSpacing() / 2, theme.getTaskBarHeight(), subTask.getStartDate(), subTask.getEndDate(), streamColor.darker());
				y += taskOffset;
			}
			
			y += streamSpacing;
		}
	}
}
