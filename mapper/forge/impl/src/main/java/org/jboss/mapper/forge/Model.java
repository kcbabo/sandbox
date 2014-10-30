package org.jboss.mapper.forge;

import java.io.PrintStream;
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
	
	public Model setIsCollection(boolean isCollection) {
		this.isCollection = isCollection;
		return this;
	}
	
	public List<String> listFields() {
		List<String> fields = new LinkedList<String>();
		return listFields(fields, this, "");
	}
	
	public List<String> listFields(List<String> fieldList, Model curNode, String prefix) {
		fieldList.add(prefix + curNode.getName());
		for (Model child : curNode.children.values()) {
			listFields(fieldList, child, curNode.getName() + ".");
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
