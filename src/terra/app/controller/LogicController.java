package terra.app.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class LogicController implements Initializable {
	
	private final String DEGREE  = "\u00b0";

	private Task<Void> task;
	
	static SerialPort serialPort;

	@FXML
	private Text leftTemp;

	@FXML
	private Text rightTemp;
	
	@FXML
	private Text humid;
	
	@FXML
	private LineChart<Number, Number> humidChart;
	
	@FXML
	private LineChart<Number, Number> leftTempChart;
	
	@FXML
	private LineChart<Number, Number> rightTempChart;
	
	@FXML
	private ChoiceBox<String> comPorts;

	@FXML
	private ToggleButton startBtn;
	
	private int timeCount = -1;
	
	public void startConnection(){
		if(startBtn.getText().equals("Start")){
			String port = comPorts.getValue();
			createConnectionTask(port);
			new Thread(task).start();
			startBtn.setText("Stop");
		} else {
			startBtn.setText("Start");
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				System.out.println(e);
			}
			task.cancel();
		}
	}
	
	private Series<Number, Number> humidSeries;
	private Series<Number, Number> rightTempSeries;
	private Series<Number, Number> leftTempSeries;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		humidSeries = new Series<Number,Number>();
		humidChart.getData().add(humidSeries);

		rightTempSeries = new Series<Number,Number>();
		rightTempChart.getData().add(rightTempSeries);
		
		leftTempSeries = new Series<Number,Number>();
		leftTempChart.getData().add(leftTempSeries);
		
		List<String> portNames = Arrays.asList(SerialPortList.getPortNames());
		ObservableList<String> observableList = FXCollections.observableArrayList(portNames);
		comPorts.setItems(observableList);

	}

	private void createConnectionTask(final String port) {
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				 serialPort = new SerialPort(port); 
			        try {
			            serialPort.openPort();//Open port
			            serialPort.setParams(115200, 8, 1, 0);//Set params
			            serialPort.setEventsMask(SerialPort.MASK_RXCHAR);//Set mask
			            serialPort.addEventListener(new SerialPortReader());//Add SerialPortEventListener
			        }
			        catch (SerialPortException ex) {
			            System.out.println(ex);
			        }

				return null;
			}
		};
	}
	
	
	public void changeValues(final SerialMessage msg){
		timeCount++;
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				leftTemp.setText(toTemp(msg.getLeftTemp()));
				rightTemp.setText(toTemp(msg.getRightTemp()));
				humid.setText(toPercent(msg.getHumid()));
				humidSeries.getData().add(new Data<Number,Number>(timeCount,msg.getHumid()));
				leftTempSeries.getData().add(new Data<Number,Number>(timeCount,msg.getLeftTemp()));
				rightTempSeries.getData().add(new Data<Number,Number>(timeCount,msg.getRightTemp()));
			}
		});
		
	}
	
    private String toPercent(double value) {
    	return String.format("%s%%", value);
    }

	private String toTemp(double temp) {
		return String.format("%s%sC", temp,DEGREE);
	}

	private class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){
                if(event.getEventValue() == 23){
                	try {
                		String incomeString = serialPort.readString();
                		SerialMessage msg = extractMessage(incomeString);
                		changeValues(msg);
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
                }
            }
        }

		private SerialMessage extractMessage(String readString) {
			String[] parts = readString.split(",");
			
			return new SerialMessage.Builder()
				.setHumidity(parts[0])
				.setLeftTemp(parts[1])
				.setRightTemp(parts[2])
				.setAutoMode(parts[3])
				.setLightsOn(parts[4])
				.setHeatingOn(parts[5])
				.build();
		}
    }
    

}
