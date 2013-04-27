There are two different interfaces for using NW2PNG:

* A basic command-line interface which can be used for scripted tests or automated tasks
* A GUI which can be used by desktop users easily

To distribute NW2PNG, the idea is to distribute all content in one ZIP. This ZIP contains build scripts, tests, libraries, source code, and ready-to-use built files (JARs). The ZIP also contains an example ClassImages.txt file which can easily be modified.

Once you have an up-to-date copy of the repository (all source code is up-to-date), you can build the application with [Apache Ant](http://ant.apache.org/). Ant is used by most IDEs (NetBeans, Eclipse, etc.) to package Java source code into JAR files, so you may already have it already without knowing it. If you don't have it, use [these instructions](http://blogs.oracle.com/rajeshthekkadath/entry/installing_ant_on_windows) (Windows) to install it.

Once you have it installed, open  a command prompt and `cd` into the `build` directory. Run the following commands in order:

* `ant clean` – this removes old compiled files (`build/temp` and the JAR files in the main directory)
* `ant` – this will recompile all the source files and create the appropriate JAR files in the main directory

After running these commands, it's ready to distribute.

For testing/development, you can, of course, still build and run from your IDE. You will just need to make sure to add the appropriate libraries (in the `lib` folder) to the project's configuration.
