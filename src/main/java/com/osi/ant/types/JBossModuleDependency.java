package com.osi.ant.types;

public class JBossModuleDependency {
	
	private String name;
	private String slot = null;
	private Boolean export = null;
	private Boolean optional = null;

	public JBossModuleDependency() {}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSlot(String slot) {
		this.slot = slot;
	}
	
	public void setExport(boolean export) {
		this.export = export;
	}
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public String getName() {
		return name;
	}


	public String getSlot() {
		return slot;
	}


	public Boolean getExport() {
		return export;
	}


	public Boolean getOptional() {
		return optional;
	}
}
