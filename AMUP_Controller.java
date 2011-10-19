import processing.core.PApplet;
import java.util.ArrayList;

public class AMUP_Controller extends AMUP_element{

  Serial_Handler serial_handler;
  MIDI_Handler midi_handler;
  User_Interface user_interface;
  
 ArrayList<Abstract_Handler> handlers;
  
  
  public AMUP_Controller (Abstract_Handler[] _handlers, User_Interface _user_interface) {
    handlers = new ArrayList<Abstract_Handler>();
    String[] handler_names = new String[_handlers.length];
    
    for (int i = 0; i < _handlers.length ; i ++) {
        if (Serial_Handler.class.isInstance(_handlers[i])) {
          serial_handler = (Serial_Handler) _handlers[i]; 
        }
        else if (MIDI_Handler.class.isInstance(_handlers[i])) {
          midi_handler = (MIDI_Handler) _handlers[i];
        }
        handlers.add(_handlers[i]);
        handler_names[i] = handlers.get(i).name;
    }
    
    user_interface = _user_interface;
    user_interface.create_handler_menus(handler_names, 20, 100);

    for (int i = 0; i < handlers.size() ; i ++) {
      user_interface.add_dropdown_items(handlers.get(i).name, handlers.get(i).device_list());  
    }
    
  }
  
  
  public void serial_to_midi(byte new_msg_input []) {
    // check that byte array contains appropriate number of elements and that midi is connected
    if (new_msg_input.length != 3 || !midi_handler.connected()) return;
        
    // identify the channel of the current midi message
    int midi_channel = midi_handler.is_active_channel((int)(new_msg_input[0]));
    if (midi_channel > -1) {
//         myBus.sendControllerChange(midi_handler.get_channel(i), (int)(new_msg_input[1]), (int)(new_msg_input[2])); // Send a controllerChange
        processing_app.println(" midi sent");
    }
    
    // if correct channel was found, then send midi message
//    if (midi_channel_select > -1) {
//        int midi_channel = (int)(new_msg_input[0]) - midi_channel_offset;
//	myBus.sendControllerChange(midi_channel, (int)(new_msg_input[1]), (int)(new_msg_input[2])); // Send a controllerChange
//    }
}


void midi_to_serial(int channel, int num, int val) {

//    int midi_channel_select = -1;
//    for (int i = 0; i < midi_channels.length; i++) {
//      if (midi_channels[i] + midi_channel_offset == channel) {
//        midi_channel_select = i;
//      }
//    }
    int midi_channel = midi_handler.is_active_channel(channel);
    
    // if correct channel was found, then send midi message
    if (midi_channel > -1) {
        byte[] new_msg = {(byte)channel, (byte)num, (byte)val};
        serial_handler.send_msg(new_msg);
    }
}

 void midi_to_serial(byte new_msg[]) {
    if (new_msg.length != 3 || !serial_handler.connected()) return;

    int midi_channel = midi_handler.is_active_channel(new_msg[0]);
    
    // if correct channel was found, then send midi message
    if (midi_channel > -1) {
        serial_handler.send_msg(new_msg);
    }
}

  
}
