package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jssc.SerialPortList;
import arduino.Arduino;


public class Controller {

    Arduino arduino; //ардуино
    ObservableList<String> portsList;//список доступных ком портов
    String[] serialPortArray;//массив доступных ком портов
    String choisedPort;//выбранный порт
    String choisedMode;//выбранный режим
    int brightnessValue = 70;
    int mixingValue = 7;
    String smoothing = "L";
    boolean Custom = false;
    Color currentColor = Color.WHITE;
    Color current1PaletteColor = Color.WHITE;
    Color current2PaletteColor = Color.WHITE;
    Color current3PaletteColor = Color.WHITE;
    Color current4PaletteColor = Color.WHITE;
    private double xOffSet = 0;
    private double yOffSet = 0;
    String[] modes =
            {
                    "Off",
                    "Lines",
                    "Solid",
                    "Rainbow",
                    "RainbowStripes",
                    "Ocean",
                    "Cloud",
                    "Lava",
                    "Forest",
                    "Party",
                    "CustomPalette"
            };
    String[] modes_ru = {
            "Выкл",
            "Линии",
            "Заполнение",
            "Радуга",
            "Радужные линии",
            "Океан",
            "Облака",
            "Лава",
            "Лес",
            "Вечеринка",
            "Кастомная палитра"
    };

    ChangeListener<String> portsBoxChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            choisedPort = newValue;
            applyButton.setDisable(true);
        }
    };
    ChangeListener<String> modesBoxChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            choisedMode = t1;
            if(modes[findId(t1,modes_ru)].equals("CustomPalette")){
                picker1.setVisible(true);
                picker2.setVisible(true);
                picker3.setVisible(true);
                picker4.setVisible(true);
                paletteLabel.setVisible(true);
                Custom = true;
            }
            else{
                picker1.setVisible(false);
                picker2.setVisible(false);
                picker3.setVisible(false);
                picker4.setVisible(false);
                paletteLabel.setVisible(false);
                Custom = false;
            }
        }
    };
    ChangeListener<Number> brightnessSliderChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            brightnessValueLabel.setText(t1.intValue()+"%");
            brightnessValue = (t1.intValue());
        }
    };
    EventHandler<ActionEvent> colorPickerListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            currentColor = colorPicker.getValue();
        }
    };
    EventHandler<ActionEvent> colorPicker1Listener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            current1PaletteColor = picker1.getValue();
        }
    };
    EventHandler<ActionEvent> colorPicker2Listener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            current2PaletteColor = picker2.getValue();
        }
    };
    EventHandler<ActionEvent> colorPicker3Listener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            current3PaletteColor = picker3.getValue();
        }
    };
    EventHandler<ActionEvent> colorPicker4Listener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            current4PaletteColor = picker4.getValue();
        }
    };
    ChangeListener<Number> mixingSliderChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            mixingLabel.setText(t1.intValue()+"");
            mixingValue = t1.intValue();

        }
    };
    EventHandler<ActionEvent> smoothingCheckBoxListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if(smoothingCheckBox.isSelected()){
                smoothing = "L";
            }
            else
            {smoothing = "N";}
        }
    };

    @FXML
    private ChoiceBox portsBox;

    @FXML
    private Label statusText;

    @FXML
    private ComboBox operatingModeBox;

    @FXML
    private Button applyButton;

    @FXML
    private Slider brightnessSlider;

    @FXML
    private Label brightnessValueLabel;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button disconnectButton;

    @FXML
    private Slider mixingSlider;

    @FXML
    private Label mixingLabel;

    @FXML
    private CheckBox smoothingCheckBox;

    @FXML
    private ColorPicker picker1;

    @FXML
    private ColorPicker picker2;

    @FXML
    private ColorPicker picker3;

    @FXML
    private ColorPicker picker4;

    @FXML
    private Label paletteLabel;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void initialize(){
        serialPortArray = SerialPortList.getPortNames();
        portsBox.getSelectionModel().selectedItemProperty().addListener(portsBoxChangeListener);
        operatingModeBox.getItems().setAll(modes_ru);
        operatingModeBox.getSelectionModel().selectedItemProperty().addListener(modesBoxChangeListener);
        portsBox.setItems(FXCollections.observableArrayList(SerialPortList.getPortNames()));
        brightnessSlider.valueProperty().addListener(brightnessSliderChangeListener);
        mixingSlider.valueProperty().addListener(mixingSliderChangeListener);
        colorPicker.setOnAction(colorPickerListener);
        smoothingCheckBox.setOnAction(smoothingCheckBoxListener);
        picker1.setVisible(false);
        picker1.setOnAction(colorPicker1Listener);
        picker2.setVisible(false);
        picker2.setOnAction(colorPicker2Listener);
        picker3.setVisible(false);
        picker3.setOnAction(colorPicker3Listener);
        picker4.setVisible(false);
        picker4.setOnAction(colorPicker4Listener);
        paletteLabel.setVisible(false);
    }
    @FXML
    protected void handleClickAction(MouseEvent event){
        Stage stage = (Stage) mainPane.getScene().getWindow();
        xOffSet = stage.getX() - event.getScreenX();
        yOffSet = stage.getY() - event.getScreenY();
    }
    @FXML
    protected void handleMovementAction(MouseEvent event){
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffSet);
        stage.setY(event.getScreenY() + yOffSet);

    }

    public void onConnect(ActionEvent actionEvent) {

        serialPortArray = SerialPortList.getPortNames();
        arduino = new Arduino(choisedPort, 9600);
        if (arduino.openConnection()){

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            statusText.setText("Подключен");
            applyButton.setDisable(false);
            disconnectButton.setDisable(false);

        } else{

            statusText.setText("Ошибка подключения");
            applyButton.setDisable(true);
        } //попытка открытия порта

    }


    public void onClick(MouseEvent mouseEvent) {

        serialPortArray = SerialPortList.getPortNames();
        portsList = FXCollections.observableArrayList(serialPortArray);
        portsBox.setItems(portsList);

    }

    public void onApply(ActionEvent actionEvent) {
        if(Custom){
            arduino.serialWrite(
                    modes[findId(choisedMode,modes_ru)]+","
                            +brightnessValue+","
                            +mixingValue+","
                            +smoothing+","
                            +toHEX(current1PaletteColor)+","
                            +toHEX(current2PaletteColor)+","
                            +toHEX(current3PaletteColor)+","
                            +toHEX(current4PaletteColor)

            );

        } else if(!Custom) {

            arduino.serialWrite(
                    modes[findId(choisedMode,modes_ru)] + ","
                            + brightnessValue + ","
                            + mixingValue + ","
                            + smoothing + ","
                            + toHEX(currentColor)
            );
        }
    }

    public void onDisconnect(ActionEvent actionEvent) {
        arduino.closeConnection();
        applyButton.setDisable(true);
        disconnectButton.setDisable(true);
        statusText.setText("Отключен");
    }
    public String toHEX(Color color){
        String hex = String.format("0x%02x%02x%02x", (int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255));
        return hex;
    }
    public int findId(String s, String[] m){
        int result = 0;
        for (int i = 0; i < m.length; i++) {
            if(m[i].equals(s)){
                result = i;
            }
        }
        return result;
    }

    public void onClose(ActionEvent actionEvent) {
        Stage stage = (Stage)disconnectButton.getScene().getWindow();
        stage.close();

    }
    public void onHide(ActionEvent actionEvent){
        Stage stage = (Stage)disconnectButton.getScene().getWindow();
        stage.setIconified(true);
    }
}