/*
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		if (getFile(project, DEFAULT_DOZER_PATH).exists()) {
			config = ConfigBuilder.loadConfig(
					getFile(project, DEFAULT_DOZER_PATH).getUnderlyingResourceObject());
		} else {
			config = ConfigBuilder.newConfig();
		}
		getMapperContext(project).setConfig(config);
		return config;
	}
	
	protected void saveConfig(Project project) throws Exception {
		getMapperContext(project).getConfig().saveConfig(
				getFile(project, DEFAULT_DOZER_PATH).getResourceOutputStream());
	}
	
	protected FileResource<?> getFile(Project project, String path) {
		DirectoryResource root = project.getFacet(ResourcesFacet.class).getResourceDirectory();
		return root.getChildOfType(FileResource.class, path);
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
