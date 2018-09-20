package uk.co.johnlewispartnership.msproject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MSProject {
	protected Element project;
	protected List<Task> tasks;
	protected List<Milestone> milestones;
	protected SimpleDateFormat format;
	protected Date startDate;
	protected Date endDate;
	
    private static Logger logger = Logger.getLogger(MSProject.class.getCanonicalName());

	public MSProject(File file) {
		tasks = new ArrayList<>();
		milestones = new ArrayList<>();
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(file);
			project = document.getDocumentElement();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.severe(e.getMessage());
		}

		Element start = (Element) project.getElementsByTagName("StartDate").item(0);
		Element end = (Element) project.getElementsByTagName("FinishDate").item(0);		

		try {
			startDate = format.parse(start.getTextContent());
			endDate = format.parse(end.getTextContent());
		} catch (DOMException | ParseException e) {
			logger.severe(e.getMessage());
		}

		Element tasksNode = (Element) project.getElementsByTagName("Tasks").item(0);
		NodeList taskElements = tasksNode.getElementsByTagName("Task");

		int position = 0;
		while (position < taskElements.getLength()) {
			position = buildTaskTree(taskElements, position, 1, tasks);
			
		}

		for (int i = 0; i < taskElements.getLength(); i++) {
			Task task = Task.createTask((Element) taskElements.item(i));
			if (task != null && task.isMilestone()) {
				milestones.add(new Milestone(task));
			}
		}
	}
	
	protected int buildTaskTree(NodeList taskElements, int position, int level, List<Task> taskList) {
		Task task = Task.createTask((Element) taskElements.item(position));

		if (task != null && !task.isMilestone()) {
			int taskLevel = task.getLevel();
			if (taskLevel == level) {
				taskList.add(task);
			} else if (taskLevel > level) {
				Task parent = taskList.get(taskList.size() - 1);
				while (position < taskElements.getLength()) {
					int newPosition = buildTaskTree(taskElements, position, level + 1, parent.getChildTasks());
					if (position == newPosition) {
						return position;
					} else {
						position = newPosition;
					}					
				}
			} else {
				return position;
			}
		}
		
		return position + 1;
	}

	public List<Milestone> getMilestones() {
		return milestones;
	}

	public List<Task> getTasks() {
		return tasks;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
}
