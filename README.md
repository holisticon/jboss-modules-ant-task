JBoss Modules Ant Task
======================

The JBoss Modules Ant task provides an easy way to create one or more JBoss Modules modules via Ant without resorting to using copy tasks and versioning module.xml files. See [this blog post](http://relation.to/16904.lace) for more information about JBoss Modules.

Information
-----------
The Ant task supports creating a module directory structure as well as the creation of a module.xml file for the specified module. 

Examples
--------
The following example creates a module named com.osi.foo with a slot of 1.0 located at the path specified by ${jboss.home}/modules. A module descriptor will also be created that specifies an optional exported dependency on com.osi.bar.

	<modules todir="${jboss.home}/modules" overwrite="true">
	  <module name="com.osi.foo" slot="1.0" dir="ivy-lib">
	    <include name="foo.jar" />
	    <dependency name="com.osi.bar" export="true" optional="true" />
	  </module>
	</modules>

The style of the Ant task is intentionally meant to look like a copy task since that is its main function. The module type is an extension of FileSet which allows you to easily specify the files to be included in the module.

The modules task supports the following attributes:

* *todir*: the base directory in which modules will be created
* *overwrite*: indicates whether or not to overwrite existing module artifacts if the destination already exists

The module type is an extension of FileSet and is used to specify one or more modules to create in the directory specified by modules' todir attribute. Module files can be specified as would normally be done with a FileSet by using either includes/excludes attributes or include/exclude nested elements. In addition to the regular FileSet options, the module type adds the following attributes:

* *name*: the name of the module. The Ant task will create a directory hierarchy based on the specified dot-separated name. Each segment of the name separated by a dot will be turned into a path element for the module directory.
* *slot*: an optional slot name. The slot name is used to differentiate multiple versions of the same module. The slot name is used as the final path name in the directory hierarchy created for the module. If not specified, the slot defaults to main.

Additionally, the Ant task supports specifying dependencies with zero or more nested dependency elements. See the Descriptor Support section for more information.

Descriptor Support
------------------
The dependency element is used to specify modules that are dependencies for the module being defined by the module type. It currently supports the following attributes:

* *name*: the name of the module that is being defined as a dependency. This is a required attribute.
* *slot*: an optional attribute used to define the slot to which the dependency belongs. No slot is included in the descriptor if this attribute is omitted. This is done in case JBoss Modules changes the default slot value in the future.
* *export*: an optional boolean attribute which indicates whether or not the dependency is exported with the module in which the dependency is defined.
* *optional*: an optional boolean attribute which indicates whether or not the dependency is optional. If this attribute is omitted, it has the effect of making dependency required.

See the [JBoss Modules Descriptor](https://docs.jboss.org/author/display/MODULES/Module+descriptors) documentation for information regarding the format of module descriptors. The Ant task does not currently support any features supported by JBoss Modules that are not explicitly detailed in this document (i.e. main-class).

Installation and Usage
----------------------
Releases can be grabbed from the Downloads section of this GitHub repo. The easiest way to install is to drop the file into ~/.ant/lib. To use the task in your build files, use the following taskdef:

`<taskdef resource="com/osi/antlib.xml" />`
