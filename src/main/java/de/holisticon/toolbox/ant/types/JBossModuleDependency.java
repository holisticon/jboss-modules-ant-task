package de.holisticon.toolbox.ant.types;

import org.apache.tools.ant.BuildException;

/**
 * Represents a module dependency on another module.
 * 
 * @author Simon Zambrovski
 * @author David Hosier
 */
public class JBossModuleDependency {

	enum Services {
		NONE("none"), IMPORT("import"), EXPORT("export");
		String value;

		Services(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		static Services byValue(String value) {
			for (Services service : values()) {
				if (service.getValue().equals(value)) {
					return service;
				}
			}
			return null;
		}
	}

	private String name;
	private String slot = null;
	private Boolean export = null;
	private Boolean optional = null;
	private Services services = null;

	public JBossModuleDependency() {
	}

	public JBossModuleDependency(final String name, final String slot) {
		setName(name);
		if (!JBossModule.MAIN_SLOT.equals(slot)) {
			setSlot(slot);
		}
	}

	/**
	 * Sets module name.
	 * 
	 * @param name
	 *            name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets module slot.
	 * 
	 * @param slot
	 *            slot to set.
	 */
	public void setSlot(String slot) {
		this.slot = slot;
	}

	/**
	 * Sets if the module exports.
	 * 
	 * @param export
	 */
	public void setExport(boolean export) {
		this.export = export;
	}

	/**
	 * Sets if the module is optional.
	 * 
	 * @param optional
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * Sets services.
	 * 
	 * @param services
	 *            none, import or export.
	 */
	public void setServices(String services) {
		final Services value = Services.byValue(services);
		if (value == null) {
			throw new BuildException(String.format("Wrong value for services attribute specified: %s. Allowed values are 'none', 'import' and 'export'.",
					services));
		}
	}

	/**
	 * Attribute getter.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Attribute getter.
	 * 
	 * @return slot or null.
	 */
	public String getSlot() {
		return slot;
	}

	/**
	 * Attribute getter.
	 * 
	 * @return export or null.
	 */
	public Boolean getExport() {
		return export;
	}

	/**
	 * Attribute getter.
	 * 
	 * @return optional or null.
	 */
	public Boolean getOptional() {
		return optional;
	}

	/**
	 * Attribute getter.
	 * 
	 * @return services or null.
	 */
	public Services getServices() {
		return services;
	}

}
