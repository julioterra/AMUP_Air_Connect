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

 MidiBus myBus; // The MidiBus
 AMUP_Controller controller;
 User_Interface user_interface;
 Serial_Handler serial_handler;
 MIDI_Handler midi_handler;
 
 void setup () {
     // set the window size:
    size(300,  600);
    AMUP_element.register_app(this);
    user_interface = new User_Interface();
    serial_handler = new Serial_Handler("serial");
    midi_handler = new MIDI_Handler("midi");
    Abstract_Handler [] handlers = {serial_handler, midi_handler}; 
    controller = new AMUP_Controller(handlers, user_interface);
    AMUP_element.register_controller(controller);
    

    MidiBus.list();
    String midi_list [] = MidiBus.availableInputs();
    int midi_num = -1;
    for(int i = 0; i <  midi_list.length;i++){
      if (midi_list[i].contains(midi_device_name))
        midi_num = i;
        midi_found = true;
        myBus = new MidiBus(this,  midi_num, midi_num);
    } 
        
   
//   setupSerial(this);

    println("\n** connection status **");
    if (!midi_found) println ("ERROR: midi NOT found");
    else println("found midi: " + midi_list[midi_num]);

     background(0);
 }

 void draw () {
          
     
     // everything happens in the serialEvent()
 }

void serialEvent(Serial myPort) {
     while (myPort.available() > 0) {
          byte new_byte []  = {byte(myPort.read())};
          serial_handler.read(new_byte);
//          if (int(new_byte) > 127) {
//              msg_count_input = 0;
//              new_msg_input[msg_count_input] = new_byte;
//              msg_count_input++;
//              new_msg_input_flag = true;
//          } else if (new_msg_input_flag && msg_count_input < 3 && int(new_byte) < 128) {
//              new_msg_input[msg_count_input] = new_byte;  
//              msg_count_input++;
//              if (msg_count_input == 3) {
//                  serial_to_midi(new_msg_input);
//                  msg_count_input = 0;
//                  new_msg_input_flag = false;
//                  println("serial message" + int(new_msg_input[0]) + ", "  + int(new_msg_input[1]) + ", "  + int(new_msg_input[2]));
//              }
//          }
     }
 
}

void rawMidi(byte[] data) { // You can also use rawMidi(byte[] data, String bus_name)
  midi_handler.read(data);
}


public void serial_to_midi(byte new_msg_input []) {
    // check that byte array contains appropriate number of elements and that midi is connected
    if (new_msg_input.length != 3 || !midi_found) return;
        
    // identify the channel of the current midi message
    int midi_channel_select = -1;
    for (int i = 0; i < midi_channels.length; i++) {
      if (midi_channels[i] + midi_channel_offset == int(new_msg_input[0])) {
        midi_channel_select = i;
      }
    }
    
    // if correct channel was found, then send midi message
    if (midi_channel_select > -1) {
        int midi_channel = int(new_msg_input[0]) - midi_channel_offset;
	myBus.sendControllerChange(midi_channel, int(new_msg_input[1]), int(new_msg_input[2])); // Send a controllerChange
    }
}


void midi_to_serial(int channel, int num, int val) {

    int midi_channel_select = -1;
    for (int i = 0; i < midi_channels.length; i++) {
      if (midi_channels[i] + midi_channel_offset == channel) {
        midi_channel_select = i;
      }
    }
    
    // if correct channel was found, then send midi message
    if (midi_channel_select > -1) {
      myPort.write(byte(channel));
      myPort.write(byte(num));
      myPort.write(byte(val));
      println(" serial sent");
    }
}

    public void serial_connection_toggle(int theValue) {
      
       if(theValue == 0) {
           Serial_Handler.closeSerial();
           println("************ connection closed ************");
       } else {
//           setupSerial();
           println("************ connection openned ************");
       }
    }

//void console_midi(promidi.Controller controller) {
//  int num = controller.getNumber();
//  int val = controller.getValue();
//  myPort.write(byte(15+midi_channel_offset));
//  myPort.write(byte(num));
//  myPort.write(byte(val));
//  println("serial sent");
//  
//  
//}
//

}
