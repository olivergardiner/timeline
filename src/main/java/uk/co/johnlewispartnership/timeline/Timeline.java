package uk.co.johnlewispartnership.timeline;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.johnlewispartnership.msproject.MSProject;

/**
 * @author Oliver
 *
 */
public class Timeline {
	private static Logger logger = Logger.getLogger(Timeline.class.getCanonicalName());
	
	public static void main(String[] args) {
		String path = "project.pod.xml";
		if (args.length == 1) {
			path = args[0];
		}
		
        TimelineBuilder timeline = new TimelineBuilder(new MSProject(new File(path)));

		try (
			FileOutputStream output = new FileOutputStream(new File(path + ".pptx"));
		) {
			timeline.writeSlides(output);				
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
