-------------------------------------------
  INSTALLATION/BUILD/COMPILE INSTRUCTIONS  
-------------------------------------------

Make sure you have the following installed:
javafx-sdk-17.0.1
mysql-connector-java-8.0.27.jar

How to install the project in Eclipse:
1) Open Eclipse
2) Create a new Java Project, the name doesn't matter
3) Copy the contents of the 'SQL-Library' folder directly into the project folder (choose 'Overwrite All')
4) Right click on the project, navigate to Build Path -> Configure Build Path. Then choose the 'Libraries' tab
5) Under 'Modulepath', click on 'JavaFX', and click 'Edit'. 
6) Click 'User Libraries', then click 'New' and call it 'JavaFX'
7) Click Add External JARs, and navigate to your Javafx installation location in the file explorer. Navigate into Javafx/javafx-sdk-17.0.1/lib, and select all of the jar files. Click 'Open'
8) Click 'Apply and Close', and then 'Finish'
9) Under 'Classpath', click Edit, and then choose your mysql-connector-java-8.0.27.jar file
10) Click 'Apply and Close'
11) Click the Run button, and choose 'Main - application' when the 'Select Java Application' window appears
12) You should get an error, but now the run configuration is available to edit. Go to 'Run Configurations' under the Run button, and navigate to the 'Arguments' tab.
13) For Program arguments, type your "database URL" "user" "password" like this: "jdbc:mysql://localhost:3306/library?useSSL=false" "root" "cs4347libraryproject2001"
14) For VM arguments, type the following:
--module-path "D:\Javafx\javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml
But replace the file path with the path to your javafx/lib installation
15) Press 'Apply' and then 'Run' to build and run the program

How to run the library.jar file:
1) Open the 'script.bat' file included in our submission
2) Change the file path for --module-path to the 'lib' folder inside of your Javafx installation
3) Change the three program arguments at the end to your (1)DB url, (2)user, and (3)password. Make sure the arguments are in quotes
4) Save 'script.bat' and run it



-------------------------------------------
   ADDITIONAL INFORMATION/DEPENDENCIES
-------------------------------------------

Operating System:
- Windows

Language:
- Java 17

Frameworks and Libraries
- JavaFX 17.0.1 (see note below)
- MySQL Connector: Java-8.0.27

IDE
- Eclipse

[Note about JavaFX]
In order for JavaFX to run, you must install the 
JavaFX SDK from the website given below. Set the
following values in the provided dropdown lists:
JavaFX version: 17.0.1 [LTS]
Operating System: Windows
Architecture: x64 or x86 (depends on device)
Type: SDK

Website to JavaFX download:
https://gluonhq.com/products/javafx/