package org.jboss.mapper.forge;

import java.io.File;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.mapper.dozer.config.ConfigBuilder;

public class CreateMappingCommand extends AbstractProjectCommand  {

	@Inject ProjectFactory _projectFactory;
	
	private static final String DEFAULT_DOZER_PATH = "dozerBeanMapping.xml";
	
	@Inject
	@WithAttributes(label = "Source Model", required = true, description = "Name of the source model type")
	UIInput<String> sourceModel;
	
	@Inject
	@WithAttributes(label = "Target Model", required = true, description = "Name of the target model type")
	UIInput<String> targetModel;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(sourceModel).add(targetModel);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		DirectoryResource root = project.getFacet(ResourcesFacet.class).getResourceDirectory();
		FileResource<?> dozerConfig = root.getChildOfType(FileResource.class, DEFAULT_DOZER_PATH);
		ConfigBuilder cb = ConfigBuilder.newConfig(sourceModel.getValue(), targetModel.getValue());
		cb.saveConfig(dozerConfig.getResourceOutputStream());
		return Results.success("Created mapping configuration.");
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return _projectFactory;
	}

	@Override
	public Metadata getMetadata(UIContext context) {
		return Metadata.forCommand(ModelFromXSDCommand.class)
				.description("Create Mapping")
				.name("create-mapping")
	            .category(Categories.create("Data Mapper"));
	}
}
