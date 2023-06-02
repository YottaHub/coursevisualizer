package cn.ac.ucas.coursevisualizer;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.chart.*;


import java.io.File;
import java.util.Map;
import java.util.Set;

public class CourseVisualizerApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a VBox layout
        VBox vBox = new VBox();

        // Create a label for the welcome message
        Label welcomeLabel = new Label("Welcome! 这是一个UCAS课程表可视化工具。");
        welcomeLabel.setStyle("-fx-font-size: 30;");
        Label descriptionLabel = new Label("(1)点击下方按钮选择课程表文件" +
                " \n（可以从“https://jwxk.ucas.ac.cn/course/termSchedule”中导出）； \n" +
                "(2)成功后，在新按钮中选择分类；\n(3)最后选择统计图类型进行可视化。");
        descriptionLabel.setStyle("-fx-font-size: 20;");

        // Create a button for opening the file chooser
        Button openButton = new Button("选择课程表");
        openButton.setOnAction(event -> {
            // Show the file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Excel File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                ExcelHandler excelHandler = new ExcelHandler(selectedFile.getAbsolutePath());
                if (excelHandler.isInitialized) {
                    Label promptLabel = new Label("课程表已选择，请继续操作。");
                    promptLabel.setStyle("-fx-font-size: 15;");
                    vBox.getChildren().add(promptLabel);

                    Button barChartButton = new Button("柱状图");
                    Button pieChartButton = new Button("饼状图");
                    ComboBox<String> categoryComboBox = new ComboBox<>();
                    categoryComboBox.getItems().addAll("课程属性", "开课院系");

                    vBox.getChildren().addAll(categoryComboBox, barChartButton, pieChartButton);

                    barChartButton.setOnAction(event2 -> {
                        if (categoryComboBox.getValue() != null && categoryComboBox.getValue().equals("课程属性")) {
                            // Create a bar chart
                            BarChart<String, Number> barChart = createBarChart(excelHandler.categoryMap);

                            // Show the bar chart in a new window
                            showNewWindow("Category Bar Chart", barChart);
                        } else if (categoryComboBox.getValue() != null && categoryComboBox.getValue().equals("开课院系")) {
                            // Create a bar chart for departments
                            BarChart<String, Number> departmentBarChart = createBarChart(excelHandler.departmentMap);

                            // Show the department bar chart in a new window
                            showNewWindow("Department Bar Chart", departmentBarChart);
                        }
                    });

                    pieChartButton.setOnAction(event3 -> {
                        if (categoryComboBox.getValue() != null && categoryComboBox.getValue().equals("课程属性")) {
                            // Create a pie chart for categories
                            PieChart pieChart = createPieChart(excelHandler.categoryMap);

                            // Show the pie chart in a new window
                            showNewWindow("Category Pie Chart", pieChart);
                        } else if (categoryComboBox.getValue() != null && categoryComboBox.getValue().equals("开课院系")) {
                            // Create a pie chart for departments
                            PieChart departmentPieChart = createPieChart(excelHandler.departmentMap);

                            // Show the department pie chart in a new window
                            showNewWindow("Department Pie Chart", departmentPieChart);
                        }
                    });

                } else {
                    Label errorLabel = new Label("这不是一个课程表文件！");
                    errorLabel.setStyle("-fx-font-size: 15; -fx-text-fill: red;");
                    vBox.getChildren().add(errorLabel);
                }
            }

        });

        // Load the logo image
        Image logo = new Image("https://storage.sekai.best/sekai-assets/character/member/res021_no032_rip/card_after_training.png");

        // Create an ImageView for the logo
        ImageView logoView = new ImageView(logo);

        // Set the logo image view properties
        logoView.setFitWidth(320);
        logoView.setFitHeight(180);

        // Add the logo image view to the VBox layout
        vBox.getChildren().addAll(welcomeLabel, descriptionLabel, logoView, openButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(20));

        // Set the scene
        Scene scene = new Scene(vBox, 650, 750);
        primaryStage.setTitle("UCAS School Timetable Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BarChart<String, Number> createBarChart(Map<String, Set<String>> dataMap) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("课程分布");

        for (String category : dataMap.keySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(category);

            series.getData().add(new XYChart.Data<>(category, dataMap.get(category).size()));
            barChart.getData().add(series);
        }

        return barChart;
    }

    private PieChart createPieChart(Map<String, Set<String>> dataMap) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (String category : dataMap.keySet()) {
            PieChart.Data slice = new PieChart.Data(category, dataMap.get(category).size());
            pieChartData.add(slice);
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("课程分布");

        pieChartData.forEach(slice ->
                slice.nameProperty().bind(
                        Bindings.concat(slice.getName(), " ", slice.pieValueProperty())
                )
        );

        return pieChart;
    }

    private void showNewWindow(String title, Node content) {
        Stage stage = new Stage();
        VBox vBox = new VBox(content);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        Scene scene = new Scene(vBox);

        stage.setScene(scene);
        stage.setTitle(title);
        stage.setWidth(800);
        stage.setHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
