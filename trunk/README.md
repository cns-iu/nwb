# NWB, Sci2, and EpiC

This source tree contains several different tools, including Network Work
Bench, the Science of Science (Sci2) tool, and Epidemiology Cyberinfrastructure
(EpiC).

You can build Sci2 using a combination of Ant and Maven. The other two tools
have not been converted to use Maven yet, and have a fairly tricky build
process.

## Building CIShell (prerequisite)

To build Sci2 from source, you will first need to build CIShell. CIShell is
available from GitHub at https://github.com/CIShell/CIShell . Unfortunately, the
first time you build CIShell, it takes FOREVER as Maven downloads itself, the
Tycho plug-in builder, and a good part of Eclipse. But be patient! Anyway, You
should be able to build CIShell by running:

    git clone git://github.com/CIShell/CIShell.git
    cd CIShell
    mvn clean install
    
This will install CIShell's libraries and things into your local Maven
repository, so that they can be used by the Sci2 build. In the future, we may be
able to provide CIShell's components in a public p2 repository, so that you can
build Sci2 without having to take this step.

## Building Sci2

There are two kinds of plug-ins in Sci2. One kind can be built by Maven, the
other kind unfortunately still requires Ant. This may be fixed in a future
release. Let's start with the Maven side.

### Sci2 Maven Build

If all is well, this should be pretty easy. Simply change to the directory
containing this README file, and run

    mvn clean install

This should build all the Java-based plug-ins in NWB, Sci2, and EpiC, and
create a runnable version of the Sci2 tool. This runnable tool is located in
sci2/deployment/edu.iu.sci2.releng/target/products/. Unfortunately, the runnable
version doesn't yet contain all the algorithms it should have. It's missing some
important ones, like the ability to use the GUESS tool to visualize networks.
These extra algorithms are added in the next step:

### Finishing the Sci2 Build with Ant

There are Ant build files that complete the Sci2 tool, by adding some important
plug-ins that aren't built by Maven.

#### Adding Ant-Contrib

Before this will work, you will need to add the 
[Ant-Contrib](http://ant-contrib.sourceforge.net/)
library to Ant. You do this by downloading
the library, unzipping it, and placing the jar in one of 
[several locations](http://ant.apache.org/manual/install.html#optionalTasks), 
the most straightforward of which is ANT_INSTALLATION/lib/. In Eclipse, 
you can add the jar to Ant's classpath from "Window -> Preferences -> 
Ant -> Runtime -> Classpath".

#### Running Ant

There are two ant build files you need to run. The first one is located in
ant-parent/, and its task is to build and collect all the ant-built plugins in
the source tree (with a few exceptions, see the file itself for more). To do
this step, run

    ant -f ant-parent/build.xml
    
Now all the plugins should be present in the ant-parent/plugins directory.

The second script processes the Maven-built, runnable versions of Sci2, which
lack these plugins. This ant script,
sci2/deployment/edu.iu.sci2.releng/addAntBuiltPlugins.xml, consults a file
(sci2-nonpde-plugins.txt in that same directory) and adds all the plugins named
there into the zip files produced by the Maven build. The resulting complete
runnable tools are then placed in
sci2/deployment/edu.iu.sci2.releng/target/products-final/. The script also makes
a change to the configuration/config.ini file in each product, so that the tools
will notice the newly-added plug-ins. To run the step, run

    ant -f sci2/deployment/edu.iu.sci2.releng/addAntBuiltPlugins.xml

So now you should have a complete build of Sci2!  Yay!

-Thomas Smith, January 12 2012