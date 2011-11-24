package com.osi.ant.types;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

public class JBossModule extends FileSet {
	
	private String name;
	private String slot = "main";
	public List<JBossModuleDependency> dependencies = new ArrayList<JBossModuleDependency>();

	public JBossModule() {}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	
	public void setSlot(String slot) {
		this.slot = slot;
	}
	
	public String getSlot() { return slot; }
	
	public void addConfiguredDependency(JBossModuleDependency dependency) {
		dependencies.add(dependency);
	}
	
	public void generateDescriptor(PrintStream ps) {
		ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		ps.println(String.format("<module xmlns=\"urn:jboss:module:1.0\" name=\"%s\">", name));
		ps.println();
		ps.println("\t<resources>");
		DirectoryScanner ds = getDirectoryScanner();
		for (String file : ds.getIncludedFiles()) {
			ps.println(String.format("\t\t<resource-root path=\"%s\" />", file));
		}
		ps.println("\t</resources>");
		ps.println();
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
				ps.println("/>");
			}
			ps.println("\t</dependencies>");
		}
		ps.println("</module>");
	}
}
