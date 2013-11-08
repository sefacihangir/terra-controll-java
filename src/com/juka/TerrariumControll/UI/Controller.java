package com.juka.TerrariumControll.UI;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Controller implements Initializable {

	private Task<Integer> task;
	
	static SerialPort serialPort;

	@FXML
	private Text currentLeftTemp;

	@FXML
	private ChoiceBox<String> comPorts;

	@FXML
	private ToggleButton startBtn;
	
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		currentLeftTemp.setText("asd");
		List<String> portNames = Arrays.asList(SerialPortList.getPortNames());
		ObservableList<String> observableList = FXCollections.observableArrayList(portNames);
		comPorts.setItems(observableList);

	}

	private void createConnectionTask(final String port) {
		task = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
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
	
	
    private class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == 10){//Check bytes count in the input buffer
                    //Read data, if 10 bytes available 
                    try {
                        byte buffer[] = serialPort.readBytes(10);
                        try {
							String decoded = new String(buffer, "UTF-8");
							currentLeftTemp.setText(decoded);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
                    }
                    catch (SerialPortException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
    }


}
