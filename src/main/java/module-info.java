module kaitech {
    //Standard JRE modules.
    requires java.xml;

    //JavaFX
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    //Exported so JavaFX can access.
    exports kaitech.controller;

    opens kaitech.controller to javafx.fxml;

    //Other.
    requires org.joda.money;
}