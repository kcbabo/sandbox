package org.jboss.mapper.forge;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

public class CreateMappingCommand extends AbstractMapperCommand  {
	
	public static final String NAME = "create-mapping";
	public static final String DESCRIPTION = "Create a new mapping definition";

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
		ConfigBuilder config = loadConfig(project);
		config.addClassMapping(sourceModel.getValue(), targetModel.getValue());
		saveConfig(project);
		
		return Results.success("Created mapping configuration.");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

}
