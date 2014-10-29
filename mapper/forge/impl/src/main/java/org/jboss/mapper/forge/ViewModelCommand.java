package org.jboss.mapper.forge;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.inject.Inject;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ViewModelCommand extends AbstractMapperCommand  {
	public static final String NAME = "view-model";
	public static final String DESCRIPTION = "View the model for a Java class.";
	
	@Inject
	@WithAttributes(label = "Model Name", required = true, description = "Name of the model type")
	private UIInput<String> modelName;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(modelName);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		Model model = loadModel(project, modelName.getValue());
	    UIOutput output = context.getUIContext().getProvider().getOutput();
	    model.print(output.out());
		return Results.success();
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
