module owres.stockcomparer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.google.gson;
    requires java.net.http;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires owres.stockcomparer;

    opens owres.stockcomparer to javafx.fxml;
    exports owres.stockcomparer;
    exports owres.stockcomparer.application;
    opens owres.stockcomparer.application to javafx.fxml;
    exports owres.stockcomparer.application.controller;
    opens owres.stockcomparer.application.controller to javafx.fxml;
}