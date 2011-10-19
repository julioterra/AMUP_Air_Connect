 import processing.serial.*;
 import themidibus.*; //Import the library

import processing.serial.*;
import promidi.*;
import controlP5.*;

public class AMUP_air_Control  extends PApplet {



 Serial myPort;
 boolean serial_found = false;
 String serial_device_name = "/dev/tty.usbmodem";
// int msg_count_input = 0;
// byte new_msg_input [] = new byte [3];
// boolean new_msg_input_flag = false;

 String midi_device_name = "Traktor";
 boolean midi_found = false;
 int midi_channels[] = {0, 1, 15};
 int msg_count_output = 0;
 byte new_msg_output [] = new byte [3];
 boolean new_msg_output_flag = false;
 int midi_channel_offset = 176;

// MidiBus myBus; // The MidiBus
 AMUP_Controller controller;
 User_Interface user_interface;
 Serial_Handler serial_handler;
 MIDI_Handler midi_handler;
 
     void setup () {
         // set the window size:
        size(600,  300);
        AMUP_element.register_app(this);
        
        user_interface = new User_Interface();
        serial_handler = new Serial_Handler("serial");
        midi_handler = new MIDI_Handler("midi");
        
        Abstract_Handler [] handlers = {serial_handler, midi_handler};         
        controller = new AMUP_Controller(handlers, user_interface);
        AMUP_element.register_controller(controller);
    
         background(0);
     }
    
     void draw () {
         // everything happens in the serialEvent()
         background(0);
     }
    
        int msg_count_input = 0;
        byte new_msg_input [] = new byte [3];
        boolean new_msg_input_flag = false;

    // ** CALLBACK: handle serial events
    void serialEvent(Serial myPort) {
         while (myPort.available() > 0) {
              byte [] new_byte = {byte(myPort.read())};
              serial_handler.read(new_byte);
         }
    }
    

    // ** CALLBACK: handle midi events
    void rawMidi(byte[] data) { // You can also use rawMidi(byte[] data, String bus_name)
        midi_handler.read(data);
    }
    
    void controlEvent(ControlEvent theEvent) {
        user_interface.controlEvent(theEvent);
    }


}

