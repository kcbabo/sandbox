package org.jboss.mapper.forge;


public class MapperContext {
	
	private Model sourceModel;
	private Model targetModel;
	private ConfigBuilder config;
	
	public Model getSourceModel() {
		return sourceModel;
	}
	
	public void setSourceModel(Model sourceModel) {
		this.sourceModel = sourceModel;
	}
	
	public Model getTargetModel() {
		return targetModel;
	}
	
	public void setTargetModel(Model targetModel) {
		this.targetModel = targetModel;
	}
	
	public ConfigBuilder getConfig() {
		return config;
	}
	
	public void setConfig(ConfigBuilder config) {
		this.config = config;
	}
	
}
