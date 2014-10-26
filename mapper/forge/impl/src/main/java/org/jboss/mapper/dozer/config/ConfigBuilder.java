package org.jboss.mapper.dozer.config;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class ConfigBuilder {
	// JAXB classes for Dozer config model
	private JAXBContext _jaxbCtx;
	private Mappings _mapConfig;
	
	private ConfigBuilder() {
		this(new Mappings());
	}
	
	private ConfigBuilder(Mappings mapConfig) {
		_mapConfig = mapConfig;
	}
	
	public static ConfigBuilder newConfig(String sourceModel, String targetModel) {
		ConfigBuilder config = new ConfigBuilder();
		config.addClassMapping(sourceModel, targetModel);
		return config;
	}
	
	public static ConfigBuilder loadConfig(String path) {
		return null;
	}
	
	public void saveConfig(OutputStream output) throws Exception {
		Marshaller m = getJAXBContext().createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(_mapConfig, output);
	}
	
	// Adds a <class-a> and <class-b> mapping definition to the dozer config.
	// If multiple fields within a class are being mapped, this should only
	// be called once.
	public void addClassMapping(String fromClass, String toClass) {
		Mapping map = new Mapping();
		org.jboss.mapper.dozer.config.Class classA = new org.jboss.mapper.dozer.config.Class();
		org.jboss.mapper.dozer.config.Class classB = new org.jboss.mapper.dozer.config.Class();
		classA.setContent(fromClass);
		classB.setContent(toClass);
		map.setClassA(classA);
		map.setClassB(classB);
		_mapConfig.getMapping().add(map);
	}
	
	public static void main(String[] args) throws Exception {
		/*
		
		// Builds data models off imported type models.  Simulates a user 
		// importing a source and target model in the editor.
		Node source = createSourceNode();
		Node target = createTargetNode();
		
		// We know we will be mapping the top-level types, so as soon as the 
		// user defines the source and target, go ahead and add a class-level
		// mapping for those types.
		addClassMapping(source.type, target.type);
		
		// The following calls mimic a user creating mappings between source 
		// and target fields in the editor.
		
		// User maps header values
		map(source.get("header").get("customerNum"),
		    target.get("custId"));
		map(source.get("header").get("orderNum"),
			    target.get("orderId"));
		map(source.get("header").get("status"),
			    target.get("priority"));
		
		// User maps line items
		map(source.get("orderItems").get("item").get("quantity"),
			    target.get("lineItems").get("amount"));
		map(source.get("orderItems").get("item"),
			    target.get("lineItems"));
		map(source.get("orderItems").get("item").get("id"),
			    target.get("lineItems").get("itemId"));
		map(source.get("orderItems").get("item").get("price"),
			    target.get("lineItems").get("cost"));
		
		// Print out the completed mapping config as XML
		JAXBContext jc = JAXBContext.newInstance("org.dozer.config");
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(mapConfig, System.out);
		*/
	}
	/*
	// Simulates a user defining a mapping from source to target node
	public void map(Node source, Node target) {
		// Only add a class mapping if one has not been created already
		if (requiresClassMapping(source.parent, target.parent)) {
			addClassMapping(source.parent.type, target.parent.type);
		}
		
		// Add field mapping details for the source and target
		addFieldMapping(source, target);
	}
	
	
	
	// Return an existing mapping which includes the specified node's parent
	// as a source or target.  This basically fetches the mapping definition 
	// under which a field mapping can be defined.
	public Mapping getClassMapping(Node node) {
		Mapping mapping = null;
		
		for (Mapping m : mapConfig.getMapping()) {
			if ((m.getClassA().getContent().equals(node.type)
					|| m.getClassB().getContent().equals(node.type))) {
				mapping = m;
				break;
			}
		}
		return mapping;
	}
	
	// Add a field mapping to the dozer config.
	static void addFieldMapping(Node source, Node target) {
		boolean sourceClassMapping = getClassMapping(source.parent) != null;
		boolean targetClassMapping = getClassMapping(target.parent) != null;
		
		Mapping mapping = sourceClassMapping ? getClassMapping(source.parent)
				: getClassMapping(target.parent);
		
		Field field = new Field();
		field.setA(createField(source, !sourceClassMapping));
		field.setB(createField(target, !targetClassMapping));
		mapping.getFieldOrFieldExclude().add(field);
	}
	
	static boolean requiresClassMapping(Node source, Node target) {
		// If a class mapping already exists, then no need to add a new one
		if (getClassMapping(source) != null || getClassMapping(target) != null) {
			return false;
		}
		
		return true;
	}
	
	static FieldDefinition createField(Node node, boolean includeParentPrefix) {
		FieldDefinition fd = new FieldDefinition();
		String name = node.name;
		if (includeParentPrefix) {
			fd.setContent(node.parent.name + "." + name);
		} else {
			fd.setContent(name);
		}
		return fd;
	}
	
	// Abstract view of a type model.  I'm guessing something similar exists
	// with the Eclipse Java parser and is used to create the tree view in the
	// editor.
	static class Node {
		String name;
		String type;
		Node parent;
		HashMap<String, Node> children = new HashMap<String, Node>();
		boolean isCollection;
		
		Node addChild(String name, String type) {
			Node n = new Node();
			n.parent = this;
			n.name = name;
			n.type = type;
			children.put(name, n);
			return n;
		}
		
		public void print() {
			printNode(this, 0);
		}
		
		public Node get(String nodeName) {
			return children.get(nodeName);
		}
		
		private void printNode(Node node, int depth) {
			System.out.println(format(node, depth));
			for (Node child : node.children.values()) {
				printNode(child, depth + 1);
			}
		}
		
		private String format(Node node, int depth) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < depth; i++) {
				sb.append("  ");
			}
			sb.append(node.children.isEmpty() ? "- " : "* ");
			sb.append(node.name + " : " + node.type);
			return sb.toString();
		}
				
	}
	
	// Creates a data model corresponding to the ABCOrder class.
	static Node createSourceNode() {
		final String ABC_ORDER_TYPE = "org.example.order.abc.ABCOrder";
		final String STRING_TYPE = "java.lang.String";
		// build top level order
		Node abcOrder = new Node();
		abcOrder.name = "ABCOrder";
		abcOrder.type = ABC_ORDER_TYPE;
		
		// build header
		Node header = abcOrder.addChild("header", ABC_ORDER_TYPE + ".Header");
		header.addChild("status", STRING_TYPE);
		header.addChild("customerNum", STRING_TYPE);
		header.addChild("orderNum", STRING_TYPE);
		
		// add line items
		Node orderItems = abcOrder.addChild("orderItems", ABC_ORDER_TYPE + ".OrderItems");
		Node itemList = orderItems.addChild("item", ABC_ORDER_TYPE + ".OrderItems.Item");
		itemList.isCollection = true;
		itemList.addChild("id", STRING_TYPE);
		itemList.addChild("price", "float");
		itemList.addChild("quantity", "short");
		return abcOrder;
	}
	
	// Creates a data model corresponding to the XYZOrder class.
	static Node createTargetNode() {
		final String XYZ_ORDER_TYPE = "org.example.order.xyz.XYZOrder";
		final String STRING_TYPE = "java.lang.String";
		// build top level order
		Node xyzOrder = new Node();
		xyzOrder.name = "XYZOrder";
		xyzOrder.type = XYZ_ORDER_TYPE;
		
		xyzOrder.addChild("priority", STRING_TYPE);
		xyzOrder.addChild("custId", STRING_TYPE);
		xyzOrder.addChild("orderId", STRING_TYPE);
		

		Node lineItems = xyzOrder.addChild("lineItems", "org.example.order.xyz.LineItem");
		lineItems.isCollection = true;
		lineItems.addChild("amount", "float");
		lineItems.addChild("cost", "short");
		lineItems.addChild("itemId", STRING_TYPE);
		
		return xyzOrder;
	}
	*/
	
	private synchronized JAXBContext getJAXBContext() {
		if (_jaxbCtx == null) {
			try {
				_jaxbCtx = JAXBContext.newInstance("org.jboss.mapper.dozer.config");
			} catch (JAXBException jaxbEx) {
				throw new RuntimeException(jaxbEx);
			}
		}
		return _jaxbCtx;
	}
}
