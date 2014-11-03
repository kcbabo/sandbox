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

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Model {

	private String name;
	private String type;
	private Model parent;
	private HashMap<String, Model> children = new HashMap<String, Model>();
	private boolean isCollection;
	
	public Model(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public Model addChild(String name, String type) {
		Model n = new Model(name, type);
		n.parent = this;
		n.name = name;
		n.type = type;
		children.put(name, n);
		return n;
	}

	public void print(PrintStream out) {
		printModel(this, 0, out);
	}

	public Model get(String nodeName) {
		return children.get(nodeName);
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public Model getParent() {
		return parent;
	}
	
	public boolean isCollection() {
		return isCollection;
	}
	
	public Model setIsCollection(boolean isCollection) {
		this.isCollection = isCollection;
		return this;
	}
	
	public List<String> listFields() {
		List<String> fields = new LinkedList<String>();
		return listFields(fields, this.children.values(), "");
	}
	
	public List<String> listFields(List<String> fieldList, Collection<Model> fields, String prefix) {
		for (Model field : fields) {
			fieldList.add(prefix + field.getName());
			listFields(fieldList, field.children.values(), prefix + field.getName() + ".");
		}
		return fieldList;
	}

	private void printModel(Model node, int depth, PrintStream out) {
		out.println(format(node, depth));
		for (Model child : node.children.values()) {
			printModel(child, depth + 1, out);
		}
	}

	private String format(Model node, int depth) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < depth; i++) {
			sb.append("  ");
		}
		sb.append(node.children.isEmpty() ? "- " : "* ");
		sb.append(node.name + " : " + node.type);
		return sb.toString();
	}

}
