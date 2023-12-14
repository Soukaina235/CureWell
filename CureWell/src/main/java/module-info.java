module com.example.curewell {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;

    opens com.example.curewell to javafx.fxml;
    exports com.example.curewell;
}