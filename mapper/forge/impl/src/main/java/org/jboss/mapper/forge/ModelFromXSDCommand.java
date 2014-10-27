package org.jboss.mapper.forge;

import java.io.File;

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
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
import org.xml.sax.InputSource;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;


public class ModelFromXSDCommand extends AbstractProjectCommand {
	
	@Inject ProjectFactory _projectFactory;
	
	@Inject
	@WithAttributes(label = "Schema Path", required = true, description = "Path to schema in project")
	UIInput<String> schemaPath;
	
	@Inject
	@WithAttributes(label = "Package Name", required = true, description = "Package name for generated model classes")
	UIInput<String> packageName;

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(schemaPath).add(packageName);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Project project = getSelectedProject(context);
		FileResource<?> schemaFile = project.getRootDirectory().getChildOfType(
				FileResource.class, "src/main/resources/" + schemaPath.getValue());
		
		SchemaCompiler sc = XJC.createSchemaCompiler();
		InputSource is = new InputSource(schemaFile.getResourceInputStream());
		is.setSystemId(schemaFile.getFullyQualifiedName());
		
		sc.parseSchema(is);
		sc.forcePackageName(packageName.getValue());
		
		S2JJAXBModel s2 = sc.bind();
		JCodeModel jcm = s2.generateCode(null, null);
		jcm.build(new File(project.getRoot().getChild("src/main/java").getFullyQualifiedName()));
		
		return Results.success("Model classes created for " + schemaPath.getValue());
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
				.description("Generate model classes from XML schema")
				.name("generate-from-xsd")
	            .category(Categories.create("Data Mapper"));
	}
}
