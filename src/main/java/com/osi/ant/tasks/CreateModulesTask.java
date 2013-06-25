package com.osi.ant.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

import com.osi.ant.types.JBossModule;

/**
 * Ant task to create JBoss Modules.
 * @author Simon Zambrovski
 * @author David Hosier
 */
public class CreateModulesTask extends Task {

	private File toDir;
	private boolean overwrite = true;
	private boolean createMainSlot = true;
	private List<JBossModule> moduleList = new ArrayList<JBossModule>();

	public CreateModulesTask() {
	}

	public void addConfiguredModule(JBossModule module) {
		moduleList.add(module);
	}

	/**
	 * Checks directory.
	 */
	private void checkOutputDirectory() {
		if (toDir == null) {
			throw new BuildException("Must specify the todir attribute");
		}
		if (!toDir.exists()) {
			log("Creating destination dir: " + toDir);
			toDir.mkdirs();
		} else {
			log("Using destination dir: " + toDir);
		}
	}

	/**
	 * Creates the modules.
	 * @param module module to create.
	 */
	private void createModule(final JBossModule module) {
		final String modulePath = module.getName().replace('.', File.separatorChar);
		final File moduleDir = new File(new File(toDir, modulePath), module.getSlot());
		log("Creating module directory: " + moduleDir, Project.MSG_DEBUG);
		moduleDir.mkdirs();

		// copy files
		final Set<DirectoryScanner> directoryScanners = module.getDirectoryScanners();
		for (DirectoryScanner ds : directoryScanners) {
			final File baseDir = ds.getBasedir();
			for (String file : ds.getIncludedFiles()) {
				try {
					FileUtils.getFileUtils().copyFile(new File(baseDir, file), new File(moduleDir, file), null,
							overwrite);
				} catch (IOException e) {
					throw new BuildException("Failed to copy file: " + file, e);
				}
			}
		}

		// create module descriptor
		final File descriptor = new File(moduleDir, "module.xml");
		PrintStream ps = null;
		try {
			FileUtils.getFileUtils().createNewFile(descriptor);
			ps = new PrintStream(new FileOutputStream(descriptor));
			log("Writing module descriptor: " + descriptor, Project.MSG_DEBUG);
			module.generateDescriptor(ps);
		} catch (IOException e) {
			throw new BuildException("Failed to create module descriptor file: " + descriptor, e);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		if (this.createMainSlot && !module.isMainSlot()) {
			final File mainModuleDir = new File(new File(toDir, modulePath), JBossModule.MAIN_SLOT);
			log("Creating module directory: " + mainModuleDir, Project.MSG_DEBUG);
			mainModuleDir.mkdirs();
			// create module descriptor
			final File mainSlotDescriptor = new File(mainModuleDir, "module.xml");
			ps = null;
			try {
				FileUtils.getFileUtils().createNewFile(mainSlotDescriptor);
				ps = new PrintStream(new FileOutputStream(mainSlotDescriptor));
				log("Writing module descriptor: " + mainSlotDescriptor, Project.MSG_DEBUG);
				module.generateMainSlotDescriptor(ps);
			} catch (IOException e) {
				throw new BuildException("Failed to create module descriptor file: " + descriptor, e);
			} finally {
				if (ps != null) {
					ps.close();
				}
			}
		}

	}

	@Override
	public void execute() throws BuildException {
		checkOutputDirectory();
		for (JBossModule module : moduleList) {
			createModule(module);
		}
	}

	/**
	 * Sets a flag controlling, if existing modules should be overwritten.
	 * @param overwrite true, for overriding, defaults to true.
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * Set the destination directory.
	 * @param destDir the destination directory.
	 */
	public void setTodir(File destDir) {
		this.toDir = destDir;
	}

	/**
	 * Sets a flag controlling the creation of the main slot, if a slot is set.
	 * @param createMainSlot if false, the creation is omitted. defaults to true.
	 */
	public void setCreateMainSlot(boolean createMainSlot) {
		this.createMainSlot = createMainSlot;
	}
}
