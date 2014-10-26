package org.jboss.mapper.forge;

import java.io.File;
import java.io.FileInputStream;

import org.jboss.forge.addon.ui.annotation.Command;
import org.jboss.forge.addon.ui.annotation.Option;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.xml.sax.InputSource;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

public class TestCommand {
	
	/*

	@Command(value = "Mapper: Generate Java from XSD",  categories = {"Data Mapper"})
	public void listInterceptors(
			final UIContext context, final UIOutput output) {
		//XJC.createSchemaCompiler().parseSchema(new InputSource(new FileInputStream));
	}
	*/
	
	public static void main(String[] args) throws Exception {
		SchemaCompiler sc = XJC.createSchemaCompiler();
		
		File f = new File("/tmp/beanmapping.xsd");
		System.out.println("****" + sc == null);
		InputSource is = new InputSource(new FileInputStream(f));
		is.setSystemId(f.getAbsolutePath());
		sc.parseSchema(is);
		sc.forcePackageName("org.foo");
		
		S2JJAXBModel s2 = sc.bind();
		JCodeModel jcm = s2.generateCode(null, null);
		jcm.build(new File("/tmp/schemaout"));
	}
}
