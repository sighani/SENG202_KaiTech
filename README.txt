KAITECH
--------
KaiTech is a point-of-sale and business management system designed with food trucks in mind.

GETTING STARTED
----------------
To run KaiTech, Java 11 and Maven are required to be installed on the machine.
1. In a terminal while inside the downloaded folder run `mvn package`.
2. Either run `launch.bat` for Windows, or `launch.sh` for MacOS or Linux.

When the app opens, you will be prompted to select a pin. This must be a 4 digit pin. Enter the pin
and click the "Done" button. If successful, it should say: "Pin was successfully changed." Click the
main menu button to go to the main menu. See the User Manual for a detailed description of how to
use KaiTech.

Sample data may be found in `resources\data`.

EDITING THE PROJECT
-------------------
If you wish to edit the project, import it into your favourite IDE
(such as Intellij or Eclipse) as a Maven project. The main class can be found at
`src\main\java\kaitech\controller\Main`.

To rebuild the project, run `mvn build`.

 *  Intellij guide:
    ---------------
    To open/build the project, there are two options. After extracting the zip, you can right
    click the folder xxx and there is an option 'Open Folder as IntelliJ IDEA Project'. If
    that option is unavailable, then open IntelliJ and:
    File > Open > xxx

    After either option, right click pom.xml on the left of IntelliJ, and click 'Add As Maven Project'.
    Afterwards, you can go into the Main class in `src/main/java/kaitech/controller`, right click it and
    click Run 'Main.main()'

    If there is an error 'Project SDK not defined', then go to: File > Project Structure. There should be
    a Project tab under Project Settings, where you can select an SDK. Click Apply, then OK after making
    a selection.

 *  Eclipse guide:
    --------------
    To build it, firstly open a workspace in Eclipse that contains
    the project folder. Click File->New->Java Project then
    untick the box "Use default location". Click Browse and choose the location of where
    the project folder has been installed, click the <<PROJECT_FOLDER_NAME>> folder, click OK,
    then click Finish. If there are any errors in the files, then right click the project folder
    on the left, hover over Configure, and select Configure -> Convert to Maven. After a few
    seconds, all the errors should be resolved.