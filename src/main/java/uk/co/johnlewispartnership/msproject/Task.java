package uk.co.johnlewispartnership.msproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class Task {
	protected Element taskElement;
	
	protected String name;
	protected Date startDate;
	protected Date endDate;
	protected int level;
	
	protected List<Task> childTasks;

	protected SimpleDateFormat format;

	private static Logger logger = Logger.getLogger(Task.class.getCanonicalName());

    protected Task(Element task) {
		this.taskElement = task;
		childTasks = new ArrayList<>();
		
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		name = task.getElementsByTagName("Name").item(0).getTextContent();
		
		Element startElement = (Element) task.getElementsByTagName("Start").item(0);
		Element endElement = (Element) task.getElementsByTagName("Finish").item(0);		

		try {
			startDate = format.parse(startElement.getTextContent());
			endDate = format.parse(endElement.getTextContent());
		} catch (DOMException | ParseException e) {
			logger.severe(e.getMessage());
		}
		
		Element levelElement = (Element) task.getElementsByTagName("OutlineLevel").item(0);
		level = Integer.parseInt(levelElement.getTextContent());
	}
	
	public int getLevel() {
		return level;
	}

	public List<Task> getChildTasks() {
		return childTasks;
	}

	public static Task createTask(Element task) {
		String isNull = task.getElementsByTagName("IsNull").item(0).getTextContent();
		if ("1".equals(isNull)) {
			return null;
		}
		
		return new Task(task);
	}

	public String getName() {
		return name;
	}
	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public boolean isMilestone() {
		return startDate.equals(endDate);
	}
}
