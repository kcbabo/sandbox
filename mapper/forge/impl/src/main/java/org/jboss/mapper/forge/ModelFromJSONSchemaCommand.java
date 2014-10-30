package org.jboss.mapper.forge;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jsonschema2pojo.SchemaMapper;

import com.sun.codemodel.JCodeModel;


public class ModelFromJSONSchemaCommand extends AbstractMapperCommand {
	
	public static final String NAME = "model-from-json-schema";
	public static final String DESCRIPTION = "Generate a Java class model from JSON Schema.";
	
	@Inject
	@WithAttributes(label = "Schema Path", required = true, description = "Path to JSON schema in project")
	UIInput<String> schemaPath;
	
	@Inject
	@WithAttributes(label = "Class Name", required = true, description = "Name used for the top-level generated class")
	UIInput<String> className;
	
	@Inject
	@WithAttributes(label = "Package Name", required = true, description = "Package name for generated model classes")
	UIInput<String> packageName;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(schemaPath).add(packageName).add(className);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		FileResource<?> schemaFile = getFile(project, schemaPath.getValue());
		
		JCodeModel codeModel = new JCodeModel();
		URL jsonSchemaUrl = schemaFile.getUnderlyingResourceObject().toURI().toURL();
		new SchemaMapper().generate(codeModel, className.getValue(), packageName.getValue(), jsonSchemaUrl);
		codeModel.build(new File(project.getRoot().getChild("src/main/java").getFullyQualifiedName()));
		
		return Results.success("Model classes created for " + schemaPath.getValue());
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
