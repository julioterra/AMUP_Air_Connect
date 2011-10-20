import processing.core.PApplet;
import processing.serial.*;

public class Serial_Handler extends Abstract_Handler{
        
    static Serial port;

    // variables associated to reading bytes and grouping together into a midi message
    int msg_count_input = 0;
    byte new_msg_input [] = new byte [3];
    boolean new_msg_input_flag = false;

    public Serial_Handler(String _name) {
        super(_name);
        device_list = Serial.list();
        // create array with list of serial ports available
        PApplet.println("Updaging list of serial devices: ");
        if (device_list.length > 0) {
            for (int i = 0; i < device_list.length; i++) { 
                processing_app.println(device_list[i]);
                
            }
        }
    }

     public boolean connect(int port_number) {
        if (port_number < device_list.length) {
            device_number = port_number;
            try {
                port = new Serial(processing_app, Serial.list()[device_number], 57600);
                processing_app.delay(4000);
                port.buffer(1); 
                device_connected = true;   
                device_name = device_list[device_number];
            } catch (Exception e) {
                device_connected = false;
                device_name = "";
                processing_app.println ("issue with serial connection");
            }
        }         
        if (!device_connected) processing_app.println (name + " NOT found");
        else processing_app.println(name + "found: " + device_list[port_number]);
    
        if (!device_connected) return false;
        else return true;
    }

    public void read(byte [] new_byte) {
      for (int i = 0; i < new_byte.length; i++) {
        if ((int)(new_byte[i]) < 0) {
            msg_count_input = 0;
            new_msg_input[msg_count_input] = new_byte[i];
            msg_count_input++;
            new_msg_input_flag = true;
        } else if (new_msg_input_flag && msg_count_input < 3 && (int)new_byte[i] < 128) {
            new_msg_input[msg_count_input] = new_byte[i];  
            msg_count_input++;
            if (msg_count_input == 3) {
                if (controller_connected) {
                    controller.serial_to_midi(new_msg_input);  
                }
                msg_count_input = 0;
                new_msg_input_flag = false;
            }
        }
     }
  }
  
  public void send_msg(byte[] new_msg) {
      if (new_msg.length != 3 || !connected()) return;
      port.write(new_msg[0]);
      port.write(new_msg[1]);
      port.write(new_msg[2]);
      processing_app.println(" serial sent");
  }
 

    public void disconnect() {
        device_connected = false;
        port.stop();
    }
    
}







