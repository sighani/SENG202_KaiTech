To run the app, open a terminal in the same directory as the jar. Then run the command
'java -jar KaiTech-1.0.jar'

To open/build the project, there are two options. After extracting the zip, you can right
click the folder xxx and there is an option 'Open Folder as IntelliJ IDEA Project'. If
that option is unavailable, then open IntelliJ and:
File > Open > xxx

After either option, right click pom.xml on the left of IntelliJ, and click 'Add As Maven Project'.
Afterwards, you can go into the Main class in src/main/java/kaitech/controller, right click it and
click Run 'Main.main()'

If there is an error 'Project SDK not defined', then go to: File > Project Structure. There should be
a Project tab under Project Settings, where you can select an SDK. Click Apply, then OK after making
a selection.