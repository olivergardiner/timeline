package uk.org.whitecottage.msproject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
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
    
    protected class MilestoneComparator implements Comparator<Milestone> {

		@Override
		public int compare(Milestone o1, Milestone o2) {
			return o1.getDate().compareTo(o2.getDate());
		}    	
    }

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
		
		/*
		 * The MS Project format uses a flat list of tasks and uses the task level (the <OutlineLevel> tag)
		 * to indicate hierarchy level. MS Project itself creates an overall task at level 0 for the whole
		 * project (which is not an interesting node) while other open source tools emulating MS Project
		 * seem to start at level 1 without an enclosing project task. That makes level 1 the obvious place
		 * to start.
		 */
		
		int position = 0;
		
		/* 
		 * Get the first parseable task (the <NotNull> tag must not be set to "1")
		 */
		Task firstTask = null;
		while (firstTask == null && position < taskElements.getLength()) {
			firstTask = Task.createTask((Element) taskElements.item(position++));
		}
		
		/*
		 * Assume we'll be starting at level 1
		 */
		int level = 1;
		if (firstTask != null && firstTask.getLevel() != 0) {
			/*
			 * If the first task isn't at level 0 then assume we're starting at the level and reset the start
			 * point of the list. If the first task is at level 0 (e.g. MS Project style) then not resetting
			 * list means that we'll start building from the next task (which should be the first child task 
			 * of the project at level 1).
			 */
			level = firstTask.getLevel();
			position = 0;
		}
		
		int lastPosition = position - 1;
		while (position < taskElements.getLength() && position != lastPosition) {
			lastPosition = position;
			position = buildTaskTree(taskElements, position, level, tasks);			
		}

		for (int i = 0; i < taskElements.getLength(); i++) {
			Task task = Task.createTask((Element) taskElements.item(i));
			if (task != null && task.isMilestone()) {
				milestones.add(new Milestone(task));
			}
		}
		
		milestones.sort(new MilestoneComparator());
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
