package com.osi.ant.types;

/**
 * Represents a list of module dependencies on another modules.
 * 
 * @author Simon Zambrovski
 */
public class JBossModuleDependenciesFromList {

	private String moduleList;
	private String separator = ";";

	public JBossModuleDependenciesFromList() {
	}

	public String getModuleList() {
		return moduleList;
	}

	public void setModuleList(String moduleList) {
		this.moduleList = moduleList;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
