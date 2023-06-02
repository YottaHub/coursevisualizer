module com.example.coursevisualizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires poi;
    requires poi.ooxml;


    opens cn.ac.ucas.coursevisualizer to javafx.fxml;
    exports cn.ac.ucas.coursevisualizer;
}