
import processing.core.PApplet;
import processing.serial.*;

public class Serial_Handler extends Abstract_Handler{
        
        static Serial port;

//        static String init_command = "amup_connected";    
        
        
        String[] device_list = {};
        
        static boolean serial_connected = false;
        static boolean amup_connect_requested = false;
        static boolean amup_connected = false;
        static long last_serial_connection;
        static int connection_interval =  4000; 
        static String serial_msg =  "SERIAL MESSAGES\nwaiting to start connection \n";
        static String serial_status =  "SERIAL PORT\nwaiting to start connection \n";

        static String[] serial_list;
        static int status_color = 0xffcccc00;

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

    public String[] device_list() {
      return device_list;
    }

     public void connect(int port_number) {
        if (port_number < serial_list.length) {
            port = new Serial(processing_app, Serial.list()[port_number], 57600);
            port.bufferUntil(10); 
            serial_connected = true;
            
            amup_connected = false;
            amup_connect_requested = false;
            last_serial_connection = processing_app.millis();
            processing_app.println("connected to serial port: " + Serial_Handler.serial_list[port_number]);
        }
            else processing_app.println ("ERROR: serial NOT found");
//            setStatusColor();
    }

    public boolean connected() {
      return amup_connected;        
    }  

    public void read() {
        // read from serial port and return data        
    }
      
    public void read(byte [] new_byte) {
      for (int i = 0; i < new_byte.length; i++) {
        if ((int)(new_byte[i]) > 127) {
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
                processing_app.println("serial message" + (int)new_msg_input[0] + ", "  + (int)new_msg_input[1] + ", "  + (int)new_msg_input[2]);
            }
        }
     }
  }
  
  public void send_msg(byte[] data) {
      if (new_msg_input.length != 3 || !connected()) return;
      port.write(data[0]);
      port.write(data[1]);
      port.write(data[2]);
      processing_app.println(" serial sent");
  }
 

  public static void updateMessageStatus(String input) {
      if(serial_msg.length() > 17) serial_msg = serial_msg.substring(16);
      else serial_msg = "";
      serial_msg = "SERIAL MESSAGES\n" + input + serial_msg;
      if (serial_msg.length() > 500) serial_msg = serial_msg.substring(0, 499);
  }

  public static void updateMessagePorts(String input) {
      if(serial_status.length() > 13) serial_status = serial_status.substring(12);
      else serial_status = "";
      serial_status = "SERIAL PORT\n" + input + serial_status;
      if (serial_status.length() > 500) serial_status = serial_status.substring(0, 499);
  }


  public static boolean serialActive(PApplet processing_app) {
    if (processing_app.millis() - last_serial_connection > connection_interval) {
          amup_connected = false;
          serial_connected = false;
          port.stop();
          return false; 
      }
      return true;
  }

//        public static void setStatusColor() {
//            if (amup_connected) status_color = 0xff00cc00;  
//            else status_color = 0xffcccc00;  
//        }

    public static void closeSerial() {
        updateMessagePorts("waiting to start connection\n"); 
        port.stop();
        amup_connected = false;
        serial_connected = false;
        amup_connect_requested = false;
//            setStatusColor();
    }


//        public static void connectAMUP() {
//          if (serial_connected && !amup_connect_requested) {
//            port.write('S');
//            updateMessageStatus("starting amup connection\n");
//            amup_connect_requested = true;
//            if (AMUP_MIDI.debug_code) PApplet.println("starting amup connection");
//          }
//        }

}







