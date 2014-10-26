package org.jboss.mapper.forge;

import javax.inject.Inject;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
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
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ViewModelCommand extends AbstractProjectCommand  {

	@Inject ProjectFactory _projectFactory;
	
	@Inject
	@WithAttributes(label = "Model Name", required = true, description = "Name of the model type")
	UIInput<String> modelName;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(modelName);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		JavaSourceFacet facet = project.getFacet(JavaSourceFacet.class);
	    JavaResource javaResource = facet.getJavaResource(modelName.getValue());
	    JavaClassSource javaClass = javaResource.getJavaType();
	    UIOutput output = context.getUIContext().getProvider().getOutput();
	    output.out().println(modelName.getValue());
	    for (FieldSource field : javaClass.getFields()) {
	    	output.out().println(field.getName());
	    }
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
				.description("View Model")
				.name("view-model")
	            .category(Categories.create("Data Mapper"));
	}
}
