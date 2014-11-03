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

import javax.inject.Inject;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.xml.sax.InputSource;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;


public class ModelFromXSDCommand extends AbstractMapperCommand {
	
	public static final String NAME = "model-from-xsd";
	public static final String DESCRIPTION = "Generate a Java class model from XML Schema.";
	
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
		FileResource<?> schemaFile = getFile(project, schemaPath.getValue());
		
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
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	

}
