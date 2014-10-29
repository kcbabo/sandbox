package org.jboss.mapper.forge;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public abstract class AbstractMapperCommand extends AbstractProjectCommand  {

	private static final String DEFAULT_DOZER_PATH = "dozerBeanMapping.xml";
	private static final String MAPPER_CATEGORY = "Data Mapper";
	private static final String MAP_CONTEXT_ATTR = "mapper.context";

	@Inject 
	protected ProjectFactory projectFactory;
	
	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

	@Override
	public Metadata getMetadata(UIContext context) {
		return Metadata.forCommand(getClass())
				.description(getDescription())
				.name(getName())
	            .category(Categories.create(MAPPER_CATEGORY));
	}
	
	protected MapperContext getMapperContext(Project project) {
		MapperContext mc = (MapperContext)project.getAttribute(MAP_CONTEXT_ATTR);
		if (mc == null) {
			mc = new MapperContext();
			project.setAttribute(MAP_CONTEXT_ATTR, mc);
		}
		return mc;
	}
	
	protected ConfigBuilder loadConfig(Project project) throws Exception {
		ConfigBuilder config;
		if (getDozerFile(project).exists()) {
			config = ConfigBuilder.loadConfig(
					getDozerFile(project).getUnderlyingResourceObject());
		} else {
			config = ConfigBuilder.newConfig();
		}
		getMapperContext(project).setConfig(config);
		return config;
	}
	
	protected void saveConfig(Project project) throws Exception {
		getMapperContext(project).getConfig().saveConfig(
				getDozerFile(project).getResourceOutputStream());
	}
	
	private FileResource<?> getDozerFile(Project project) {
		DirectoryResource root = project.getFacet(ResourcesFacet.class).getResourceDirectory();
		return root.getChildOfType(FileResource.class, DEFAULT_DOZER_PATH);
	}
	
	protected Model loadModel(Project project, String className) throws Exception {
		URL[] urls = new URL[] {
	    		new File(project.getRootDirectory().getFullyQualifiedName() + "/target/classes").toURL()};
	    
		URLClassLoader cl = null;
		Model model = null;
		try {
			cl = new URLClassLoader(urls);
			Class<?> clazz = cl.loadClass(className);
			model = ModelBuilder.fromJavaClass(clazz);
		} finally {
			if (cl != null) {
				cl.close();
			}
		}
	    
	    return model;
	    
	}
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	
}
