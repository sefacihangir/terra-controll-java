package terra.app.controller;


public class SerialMessage {

	private double leftTemp;
	private double rightTemp;
	private double humid;
	private boolean autoModeOn;
	private boolean lightsOn;
	private boolean heatingOn;

	public SerialMessage(Builder builder) {
		this.leftTemp = builder.leftTemp;
		this.rightTemp = builder.rightTemp;
		this.humid = builder.humid;
		this.autoModeOn = builder.autoModeOn;
		this.lightsOn = builder.lightsOn;
		this.heatingOn = builder.heatingOn;
	}
	
	public double getLeftTemp() {
		return leftTemp;
	}
	
	public double getRightTemp() {
		return rightTemp;
	}
	
	public double getHumid() {
		return humid;
	}
	
	public boolean isAutoModeOn() {
		return autoModeOn;
	}
	
	public boolean isLightsOn() {
		return lightsOn;
	}
	
	public boolean isHeatingOn() {
		return heatingOn;
	}
	
	public static class Builder {
		
		private static final String LOGIC_ONE = "1";

		private double leftTemp;
		private double rightTemp;
    	private double humid;
    	private boolean autoModeOn;
    	private boolean lightsOn;
    	private boolean heatingOn;
		
		Builder setRightTemp(String temp){
			this.rightTemp = Double.valueOf(temp);
			return this;
		}
		
		Builder setLeftTemp(String temp){
			this.leftTemp = Double.valueOf(temp);
			return this;
		}
		
		Builder setHumidity(String humid){
			this.humid = Double.valueOf(humid);
			return this;
		}
		
		Builder setAutoMode(String value){
			this.autoModeOn = isTrue(value);
			return this;
		}
		
		Builder setLightsOn(String value){
			this.lightsOn = isTrue(value);
			return this;
		}
		
		Builder setHeatingOn(String value){
			this.heatingOn = isTrue(value);
			return this;
		}

		private boolean isTrue(String value) {
			return LOGIC_ONE.equals(value) ? true : false;
		}
		
		public SerialMessage build(){
			return new SerialMessage(this);
		}
	}
	
	
	
}