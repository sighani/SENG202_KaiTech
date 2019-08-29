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

    //Other.
    requires org.joda.money;
    requires commons.io;
}