package com.osi.ant.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

import com.osi.ant.types.JBossModule;

public class JBossModules extends Task {
	
	private File toDir;
	private boolean overwrite = true;
	private List<JBossModule> moduleList = new ArrayList<JBossModule>();
	
	public JBossModules() {}

	@Override
	public void execute() throws BuildException {
		checkOutputDirectory();
		for (JBossModule module : moduleList) {
			createModule(module);
		}
	}

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
	
	private void createModule(JBossModule module) {
		String modulePath = module.getName().replace('.', File.separatorChar);
		File moduleDir = new File(toDir, modulePath);
		moduleDir = new File(moduleDir, module.getSlot());
		log("Creating module directory: " + moduleDir, Project.MSG_DEBUG);
		moduleDir.mkdirs();
		
		DirectoryScanner ds = module.getDirectoryScanner();
		File baseDir = ds.getBasedir();
		for (String file : ds.getIncludedFiles()) {
			try {
				FileUtils.getFileUtils().copyFile(new File(baseDir, file), new File(moduleDir, file), null, overwrite);
			} catch (IOException e) {
				throw new BuildException("Failed to copy file: " + file, e);
			}
		}
		
		File descriptor = new File(moduleDir, "module.xml");
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
	}

    /**
     * Set the destination directory.
     * @param destDir the destination directory.
     */
    public void setTodir(File destDir) {
        this.toDir = destDir;
    }
    
    public void setOverwrite(boolean overwrite) {
    	this.overwrite = overwrite;
    }

    public void addConfiguredModule(JBossModule module) {
    	moduleList.add(module);
    }
}
