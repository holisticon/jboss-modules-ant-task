package com.osi.ant.types;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

/**
 * JBoss module
 * @author Simon Zambrovski
 * @author David Hosier
 */
public class JBossModule {

	public static final String MAIN_SLOT = "main";

	private String name;
	private String slot = MAIN_SLOT;
	private FileSet fileSet = new FileSet();
	public final List<JBossModuleDependency> dependencies = new ArrayList<JBossModuleDependency>();
	public final List<FileSet> includedFilesets = new ArrayList<FileSet>();

	public JBossModule() {
	}

	/**
	 * (Ant) adds a dependency to a different module.
	 * @param dependency module dependency.
	 */
	public void addConfiguredDependency(final JBossModuleDependency dependency) {
		dependencies.add(dependency);
	}

	/**
	 * (Ant) adds a dependency to resources specified by this fileset.
	 * @param filesetRef fileset with resources.
	 */
	public void addConfiguredResourceFileSet(final FileSet filesetRef) {
		includedFilesets.add(filesetRef);
	}

	public void generateDescriptor(final PrintStream ps) {

		// add all own
		final List<String> resources = new ArrayList<String>();

		if (isEmbeddedFileSet()) {
			resources.addAll(Arrays.asList(this.fileSet.getDirectoryScanner().getIncludedFiles()));
		}
		// add all referenced
		for (FileSet fsRefs : this.includedFilesets) {
			resources.addAll(Arrays.asList(fsRefs.getDirectoryScanner().getIncludedFiles()));
		}
		final PrintHelper helper = PrintHelper.module(ps, name, slot);
		helper.printModuleResources(resources);
		helper.printModuleDepenendencies(this.dependencies);
		helper.printModuleFooter();
	}

	public void generateMainSlotDescriptor(PrintStream ps) {
		PrintHelper.moduleAlias(ps, name, MAIN_SLOT, name, slot);
	}

	/**
	 * Retrieves true, if the local attribute dir is used.
	 * @return true if fileset embedded.
	 */
	private boolean isEmbeddedFileSet() {
		return this.fileSet.getDir() != null;
	}

	/**
	 * Returns module name property.
	 * @return module name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns slot name or main, if no slot provided.
	 * @return slot name.
	 */
	public String getSlot() {
		return slot;
	}

	/**
	 * Sets name.
	 * @param name name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets slot name.
	 * @param slot slot.
	 */
	public void setSlot(String slot) {
		if (!"".equals(slot)) {
			this.slot = slot;
		}
	}

	/**
	 * Sets the file set directory.
	 * @param dir shortcut for including a fileset reference with one directory.
	 */
	public void setDir(String dir) {
		this.fileSet.setDir(new File(dir));
	}

	/**
	 * Sets the file set includes.
	 * @param includes shortcut for including a fileset reference with one directory
	 */
	public void setIncludes(String includes) {
		this.fileSet.setIncludes(includes);
	}

	/**
	 * Sets the file set includes.
	 * @param includes shortcut for including a fileset reference with one directory
	 */
	public void setExcludes(String excludes) {
		this.fileSet.setExcludes(excludes);
	}

	/**
	 * Retrieves directory scanners for all configures filesets.
	 * @return directory scanners.
	 */
	public Set<DirectoryScanner> getDirectoryScanners() {
		final Set<DirectoryScanner> scanners = new HashSet<DirectoryScanner>();
		if (isEmbeddedFileSet()) {
			scanners.add(this.fileSet.getDirectoryScanner());
		}

		for (FileSet subset : this.includedFilesets) {
			scanners.add(subset.getDirectoryScanner());
		}

		return scanners;
	}

	/**
	 * Returns true, if the slot is a main slot.
	 * @return true if slot is main, false otherwise.
	 */
	public boolean isMainSlot() {
		return MAIN_SLOT.equals(slot);
	}

}
