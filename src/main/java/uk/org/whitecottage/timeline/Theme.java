package uk.org.whitecottage.timeline;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Theme {
	protected double scale = 0.9;
	
	protected double taskHeight = 20;
	protected double taskBarHeight = 12;
	protected double taskSpacing = 0;
	protected double streamSpacing = 3;
	protected double timeBarHeight = 15;
	protected double milestoneSize = 15;
	protected double streamLabelWidth = 70;
	protected double taskLabelWidth = 180;

	protected double barTextSize = 6.0;
	protected double streamTextSize = 8.0;
	protected double taskTextSize = 8.0;

	protected List<Color> streamColors;
	protected Color[] defaultStreamColors = 	{
			Color.decode("0xffb5a8"),
			Color.decode("0xff9cee"),
			Color.decode("0xb28dff"),
			Color.decode("0xecd4ff"),
			Color.decode("0xafcbff"),
			Color.decode("0xafa8db"),
			Color.decode("0xc4faf8"),
			Color.decode("0x85e3ff"),
			Color.decode("0xe7ffac"),
			Color.decode("0xffffd1"),
			Color.decode("0xffabab")
	};
	
	protected Color barColor = Color.decode("0x6eb5ff");
	protected Color milestoneColor = Color.decode("0xffcbc1");
	
	protected int currentStreamColor = 0;

	public Theme() {		
		streamColors = new ArrayList<>();
		
		for (int i = 0; i < defaultStreamColors.length; i++) {
			streamColors.add(defaultStreamColors[i]);
		}
	}

	public Color getNextStreamColor() {
		Color streamColor = streamColors.get(currentStreamColor++);
		currentStreamColor %= streamColors.size();
		
		return streamColor;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public double getTaskHeight() {
		return taskHeight;
	}

	public void setTaskHeight(double taskHeight) {
		this.taskHeight = taskHeight;
	}

	public double getTaskSpacing() {
		return taskSpacing;
	}

	public void setTaskSpacing(double taskSpacing) {
		this.taskSpacing = taskSpacing;
	}

	public double getTimeBarHeight() {
		return timeBarHeight;
	}

	public void setTimeBarHeight(double timeBarHeight) {
		this.timeBarHeight = timeBarHeight;
	}
	
	public double getMilestoneSize() {
		return milestoneSize;
	}

	public void setMilestoneSize(double milestoneSize) {
		this.milestoneSize = milestoneSize;
	}

	public double getBarTextSize() {
		return barTextSize;
	}

	public void setBarTextSize(double barTextSize) {
		this.barTextSize = barTextSize;
	}

	public double getStreamTextSize() {
		return streamTextSize;
	}

	public void setStreamTextSize(double streamTextSize) {
		this.streamTextSize = streamTextSize;
	}

	public double getTaskTextSize() {
		return taskTextSize;
	}

	public void setTaskTextSize(double taskTextSize) {
		this.taskTextSize = taskTextSize;
	}

	public Color getBarColor() {
		return barColor;
	}

	public void setBarColor(Color barColor) {
		this.barColor = barColor;
	}

	public Color getMilestoneColor() {
		return milestoneColor;
	}

	public void setMilestoneColor(Color milestoneColor) {
		this.milestoneColor = milestoneColor;
	}

	public double getStreamSpacing() {
		return streamSpacing;
	}

	public void setStreamSpacing(double streamSpacing) {
		this.streamSpacing = streamSpacing;
	}

	public double getStreamLabelWidth() {
		return streamLabelWidth;
	}

	public void setStreamLabelWidth(double streamLabelWidth) {
		this.streamLabelWidth = streamLabelWidth;
	}

	public double getTaskLabelWidth() {
		return taskLabelWidth;
	}

	public void setTaskLabelWidth(double taskLabelWidth) {
		this.taskLabelWidth = taskLabelWidth;
	}

	public List<Color> getStreamColors() {
		return streamColors;
	}

	public void setStreamColors(List<Color> streamColors) {
		this.streamColors = streamColors;
	}

	public double getTaskBarHeight() {
		return taskBarHeight;
	}

	public void setTaskBarHeight(double taskBarHeight) {
		this.taskBarHeight = taskBarHeight;
	}
}
