module mv.port.harbour_tapngo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.datatransfer;
    requires java.desktop;
    requires org.apache.logging.log4j;
    requires retrofit2;
    requires escpos.coffee;
    requires okhttp3;
    requires okhttp3.logging;
    requires java.sql;
    requires retrofit2.converter.gson;
    requires com.fasterxml.jackson.annotation;
    requires retrofit2.converter.jackson;
    requires com.google.gson;



    opens mv.port.harbour_tapngo to javafx.fxml, com.google.gson;
    exports mv.port.harbour_tapngo;
}