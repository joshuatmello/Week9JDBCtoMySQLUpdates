package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {
	
	//This is my service layer that applies business rules. 
	
	//initializes an object of ProjectDao class. 
	private ProjectDao projectDao = new ProjectDao();
	

	/**
	 * This method is called by method createProject() of the I/O layer
	 * which creates a project that is then sent to the initialized 
	 * ProjectDao class object using the addProject() method below. 
	 */
	
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
		
	}

}
