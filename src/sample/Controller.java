package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jssc.SerialPortList;
import arduino.Arduino;


public class Controller {

    Arduino arduino; //ардуино
    ObservableList<String> portsList;//список доступных ком портов
    String[] serialPortArray;//массив доступных ком портов
    String port; //выбранный порт
    String mode; //выбранный режим
    int brightness = 70; //яркость
    int mixing = 7;
    String smoothing = "L";
    boolean Custom = false;
    boolean debudMode = false;
    Color current0PaletteColor = Color.WHITE;
    Color current1PaletteColor = Color.WHITE;
    Color current2PaletteColor = Color.WHITE;
    Color current3PaletteColor = Color.WHITE;
    Color current4PaletteColor = Color.WHITE;

    String[] modes =
            {
                    "Off",
                    "Solid",
                    "Rainbow",
                    "RainbowStripes",
                    "Ocean",
                    "Cloud",
                    "Lava",
                    "Forest",
                    "Party",
                    "1_Color",
                    "2_Colors",
                    "3_Colors",
                    "4_Colors",
                    "5_Colors"
            };

    String[] modes_ru = {
            "Выкл",
            "Заполнение",
            "Радуга",
            "Радужные линии",
            "Океан",
            "Облака",
            "Лава",
            "Лес",
            "Вечеринка",
            "Один цвет",
            "Два цвета",
            "Три цвета",
            "Четыре цвета",
            "Пять цветов"
    };
    ChangeListener<String> portsBoxChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            port = newValue;
            //applyButton.setDisable(true);
        }
    };

    ChangeListener<String> modesBoxChangeListener = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            mode = t1;
            switch (modes[findId(t1, modes_ru)]) {
                case "1_Color":
                    picker0.setVisible(true);
                    picker1.setVisible(false);
                    picker2.setVisible(false);
                    picker3.setVisible(false);
                    picker4.setVisible(false);
                    mainColorLabel.setVisible(true);
                    Custom = true;
                    break;
                case "2_Colors":
                    picker0.setVisible(true);
                    picker1.setVisible(true);
                    picker2.setVisible(false);
                    picker3.setVisible(false);
                    picker4.setVisible(false);
                    mainColorLabel.setVisible(true);
                    Custom = true;
                    break;
                case "3_Colors":
                    picker0.setVisible(true);
                    picker1.setVisible(true);
                    picker2.setVisible(true);
                    picker3.setVisible(false);
                    picker4.setVisible(false);
                    mainColorLabel.setVisible(true);
                    Custom = true;
                    break;
                case "4_Colors":
                    picker0.setVisible(true);
                    picker1.setVisible(true);
                    picker2.setVisible(true);
                    picker3.setVisible(true);
                    picker4.setVisible(false);
                    mainColorLabel.setVisible(true);
                    Custom = true;
                    break;
                case "5_Colors":
                    picker0.setVisible(true);
                    picker1.setVisible(true);
                    picker2.setVisible(true);
                    picker3.setVisible(true);
                    picker4.setVisible(true);
                    mainColorLabel.setVisible(true);
                    Custom = true;
                    break;
                default:
                    picker0.setVisible(false);
                    picker1.setVisible(false);
                    picker2.setVisible(false);
                    picker3.setVisible(false);
                    picker4.setVisible(false);
                    mainColorLabel.setVisible(false);
                    Custom = false;

                    break;
            }
        }
    };
    ChangeListener<Number> brightnessSliderChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            brightnessValueLabel.setText(t1.intValue()+"%");
            brightness = (t1.intValue());
        }
    };
    EventHandler<ActionEvent> colorPicker0Listener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            current0PaletteColor = picker0.getValue();
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
            mixing = t1.intValue();

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
    private Label mainColorLabel;

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
    private Button disconnectButton;

    @FXML
    private Slider mixingSlider;

    @FXML
    private Label mixingLabel;

    @FXML
    private CheckBox smoothingCheckBox;

    @FXML
    private ColorPicker picker0;

    @FXML
    private ColorPicker picker1;

    @FXML
    private ColorPicker picker2;

    @FXML
    private ColorPicker picker3;

    @FXML
    private ColorPicker picker4;

    @FXML
    private void initialize(){
        serialPortArray = SerialPortList.getPortNames();
        portsBox.getSelectionModel().selectedItemProperty().addListener(portsBoxChangeListener);
        operatingModeBox.getItems().setAll(modes_ru);
        operatingModeBox.getSelectionModel().selectedItemProperty().addListener(modesBoxChangeListener);
        portsBox.setItems(FXCollections.observableArrayList(SerialPortList.getPortNames()));
        brightnessSlider.valueProperty().addListener(brightnessSliderChangeListener);
        mixingSlider.valueProperty().addListener(mixingSliderChangeListener);
        //colorPicker_main.setOnAction(colorPickerListener);
        smoothingCheckBox.setOnAction(smoothingCheckBoxListener);
        picker0.setVisible(false);
        picker0.setOnAction(colorPicker0Listener);
        picker1.setVisible(false);
        picker1.setOnAction(colorPicker1Listener);
        picker2.setVisible(false);
        picker2.setOnAction(colorPicker2Listener);
        picker3.setVisible(false);
        picker3.setOnAction(colorPicker3Listener);
        picker4.setVisible(false);
        picker4.setOnAction(colorPicker4Listener);
        mainColorLabel.setVisible(false);
    }

    public void onConnect() {

        serialPortArray = SerialPortList.getPortNames();
        arduino = new Arduino(port, 9600);
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


    public void onClick() {

        serialPortArray = SerialPortList.getPortNames();
        portsList = FXCollections.observableArrayList(serialPortArray);
        portsBox.setItems(portsList);

    }

    public void onApply() {
        if(Custom){
            if(!debudMode) arduino.serialWrite(
                    modes[findId(mode,modes_ru)]+","
                            + brightness +","
                            + mixing +","
                            +smoothing+","
                            +toHEX(current0PaletteColor)+","
                            +toHEX(current1PaletteColor)+","
                            +toHEX(current2PaletteColor)+","
                            +toHEX(current3PaletteColor)+","
                            +toHEX(current4PaletteColor)
            );

            System.out.println(
                    modes[findId(mode,modes_ru)]+","
                    + brightness +","
                    + mixing +","
                    +smoothing+","
                    +toHEX(current0PaletteColor)+","
                    +toHEX(current1PaletteColor)+","
                    +toHEX(current2PaletteColor)+","
                    +toHEX(current3PaletteColor)+","
                    +toHEX(current4PaletteColor)
            );

        } else if(!Custom) {

            if(!debudMode) arduino.serialWrite(
                    modes[findId(mode,modes_ru)] + ","
                            + brightness + ","
                            + mixing + ","
                            + smoothing
            );
            System.out.println(
                    modes[findId(mode,modes_ru)] + ","
                            + brightness + ","
                            + mixing + ","
                            + smoothing
            );
        }
    }

    public void onDisconnect() {
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

    public void onClose() {
        Stage stage = (Stage)disconnectButton.getScene().getWindow();
        stage.close();

    }
    public void onHide(){
        Stage stage = (Stage)disconnectButton.getScene().getWindow();
        stage.setIconified(true);
    }
}