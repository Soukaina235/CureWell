module com.example.curewell {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;
    requires java.sql;
    requires com.jfoenix;

    opens com.curewell to javafx.fxml;
    opens com.curewell.model to javafx.base;

    exports com.curewell;
<<<<<<< HEAD
=======
    exports com.curewell.controller;
    opens com.curewell.controller to javafx.fxml;
>>>>>>> b547669e9a8f206a73500ebd829b30f4e580c5c0
}