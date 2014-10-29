package org.jboss.mapper.forge;

import javax.inject.Inject;

import org.jboss.forge.addon.facets.Facet;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.facets.constraints.FacetConstraint;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class MapFieldCommand extends AbstractMapperCommand  {
	
	public static final String NAME = "map-field";
	public static final String DESCRIPTION = "Create a mapping between two fields.";
	private static final String FIELD_SEPARATOR = ".";

	@Inject
	@WithAttributes(label = "Source Field", required = true, description = "Full path of the source field")
	UIInput<String> sourceField;
	
	@Inject
	@WithAttributes(label = "Target Field", required = true, description = "Full path of the target field")
	UIInput<String> targetField;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(sourceField).add(targetField);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		ConfigBuilder config = getMapperContext(project).getConfig();
		Model source = getModel(
				getMapperContext(project).getSourceModel(), sourceField.getValue());
		Model target = getModel(
				getMapperContext(project).getTargetModel(), targetField.getValue());
		config.map(source, target);

		saveConfig(project);
		
		return Results.success("Created field mapping.");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	Model getModel(Model model, String path) {
		int sepIdx = path.indexOf(FIELD_SEPARATOR);
		if (sepIdx < 0) {
			return model.get(path);
		} else {
			String nextField = path.substring(0, sepIdx);
			return getModel(model.get(nextField), path.substring(sepIdx + 1));
		}
	}
}
