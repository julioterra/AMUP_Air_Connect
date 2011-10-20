 import processing.serial.*;
 import themidibus.*; //Import the library

import processing.serial.*;
import promidi.*;
import controlP5.*;

public class AMUP_air_Control  extends PApplet {
  
   AMUP_Controller controller;
   User_Interface user_interface;
   Serial_Handler serial_handler;
   MIDI_Handler midi_handler;
 
     void setup () {
         // set the window size:
        size(600,  300);
        background(0);
        
        AMUP_element.register_app(this);
        
        user_interface = new User_Interface();
        serial_handler = new Serial_Handler("serial");
        midi_handler = new MIDI_Handler("midi");
        
        Abstract_Handler [] handlers = {serial_handler, midi_handler};         
        controller = new AMUP_Controller(handlers, user_interface);
        AMUP_element.register_controller(controller);   
     }
    
     void draw () {
         background(0);
         // action takes place in the callback methods for Serial, MIDI, and Interface events
     }
    
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
    
    // ** CALLBACK: handle application UI events
    void controlEvent(ControlEvent theEvent) {
        user_interface.controlEvent(theEvent);
    }


}

