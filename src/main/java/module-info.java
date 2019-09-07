module kaitech {
    //Standard JRE modules.
    requires java.xml;
    requires java.sql;

    //JavaFX
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    //Exported so JavaFX can access.
    exports kaitech.controller;

    opens kaitech.controller to javafx.fxml;
    opens kaitech.model to javafx.base;

    //Other.
    requires org.joda.money;
    requires commons.io;
    requires org.apache.commons.lang3;
}