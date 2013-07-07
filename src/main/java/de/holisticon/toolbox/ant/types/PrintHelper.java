package de.holisticon.toolbox.ant.types;

import java.io.PrintStream;
import java.util.List;

/**
 * Helper for printing module information.
 * @author Simon Zambrovski
 */
public final class PrintHelper {

	private final PrintStream ps;
	private boolean isModule;

	private PrintHelper(final PrintStream ps, final String name, final String slot, final String targetName,
			final String targetSlot) {
		this.ps = ps;
		ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		isModule = (targetSlot == null);

		if (isModule) {
			if (JBossModule.MAIN_SLOT.equals(slot)) {
				ps.println(String.format("<module xmlns=\"urn:jboss:module:1.0\" name=\"%s\">", name));
			} else {
				ps.println(String.format("<module xmlns=\"urn:jboss:module:1.0\" name=\"%s\" slot=\"%s\">", name, slot));
			}
		} else {
			if (JBossModule.MAIN_SLOT.equals(targetSlot)) {
				ps.print(String.format("<module-alias xmlns=\"urn:jboss:module:1.1\" name=\"%s\" target-name=\"%s\"/>",
						name, targetName));
			} else {
				ps.print(String
						.format("<module-alias xmlns=\"urn:jboss:module:1.1\" name=\"%s\" target-name=\"%s\" target-slot=\"%s\"/>",
								name, targetName, targetSlot));
			}
		}
	}

	/**
	 * Constructs a printer for a module written to a print writer.
	 * @param ps print writer
	 * @param name module name
	 * @param slot module slot
	 * @return print helper.
	 */
	public static PrintHelper module(final PrintStream ps, final String name, final String slot) {
		return new PrintHelper(ps, name, slot, null, null);
	}

	/**
	 * Constructs a printer for a module alias written to a print writer.
	 * @param ps print writer
	 * @param name module name
	 * @param slot module slot
	 * @param targetModule target module name
	 * @param targetSlot target module slot
	 * @return print helper.
	 */
	public static PrintHelper moduleAlias(final PrintStream ps, final String name, final String slot,
			final String targetModule, final String targetSlot) {
		return new PrintHelper(ps, name, slot, targetModule, targetSlot);
	}

	/**
	 * Prints dependencies info.
	 * @param dependencies list of dependencies to print.
	 */
	public final void printModuleDepenendencies(final List<JBossModuleDependency> dependencies) {
		if (!dependencies.isEmpty()) {
			ps.println("\t<dependencies>");
			for (JBossModuleDependency dependency : dependencies) {
				ps.print(String.format("\t\t<module name=\"%s\" ", dependency.getName()));
				if (dependency.getSlot() != null) {
					ps.print(String.format("slot=\"%s\" ", dependency.getSlot()));
				}
				if (dependency.getExport() != null) {
					ps.print(String.format("export=\"%s\" ", dependency.getExport()));
				}
				if (dependency.getOptional() != null) {
					ps.print(String.format("optional=\"%s\" ", dependency.getOptional()));
				}
				if (dependency.getServices() != null) {
					ps.print(String.format("services=\"%s\" ", dependency.getServices()));
				}
				ps.println("/>");
			}
			ps.println("\t</dependencies>");
		}
	}

	/**
	 * Prints resources info.
	 * @param files list of file paths to print.
	 */
	public final void printModuleResources(final List<String> files) {
		if (!files.isEmpty()) {
			ps.println("\t<resources>");
			for (String file : files) {
				ps.println(String.format("\t\t<resource-root path=\"%s\" />", file));
			}
			ps.println("\t</resources>");
		}
	}

	/**
	 * Writes a specific footer for module
	 */
	public final void printModuleFooter() {
		if (isModule) {
			ps.println("</module>");
		}
	}
}
