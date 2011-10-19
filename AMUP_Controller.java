import processing.core.PApplet;
import java.util.HashMap;
import java.util.ArrayList;

public class AMUP_Controller extends AMUP_element{

  Serial_Handler serial_handler;
  MIDI_Handler midi_handler;
  User_Interface user_interface;
  
 HashMap<String, Abstract_Handler> handlers;
 String[] handler_name_keys;
  
  public AMUP_Controller (Abstract_Handler[] _handlers, User_Interface _user_interface) {
    handlers = new HashMap<String, Abstract_Handler>();
    String[] handler_name_keys = new String[_handlers.length];
    
    for (int i = 0; i < _handlers.length ; i ++) {
        if (Serial_Handler.class.isInstance(_handlers[i])) {
          serial_handler = (Serial_Handler) _handlers[i]; 
        }
        else if (MIDI_Handler.class.isInstance(_handlers[i])) {
          midi_handler = (MIDI_Handler) _handlers[i];
        }
        handlers.put(_handlers[i].name, _handlers[i]);
        handler_name_keys[i] = _handlers[i].name;
    }
    
    user_interface = _user_interface;
    user_interface.create_handler_menus(handler_name_keys, 20, 100);

    for (int i = 0; i < handler_name_keys.length ; i ++) {
      user_interface.add_dropdown_items(handlers.get(handler_name_keys[i]).name, handlers.get(handler_name_keys[i]).device_list());  
    }
  }
  
  public void connect_handler(String _name, int _port_number) {
      if (handlers.get(_name).connected_device_number() == _port_number) return;
      user_interface.update_toggles(_name, handlers.get(_name).connect(_port_number));  
  }
  
  
  public void serial_to_midi(byte new_msg []) {
    // check that byte array contains appropriate number of elements and that midi is connected
    if (new_msg.length != 3 || !midi_handler.connected()) return;
        
    // identify the channel of the current midi message
    MIDI_Handler midi_handler = (MIDI_Handler) handlers.get("midi");    
    int midi_channel = midi_handler.is_active_channel((int)(new_msg[0]));
    if (midi_channel > -1) {
        midi_handler.send_msg(new_msg);
        processing_app.println(" midi sent");
    }
}

 void midi_to_serial(byte new_msg[]) {
    // check that byte array contains appropriate number of elements and that midi is connected
    if (new_msg.length != 3 || !serial_handler.connected()) return;

    MIDI_Handler midi_handler = (MIDI_Handler) handlers.get("midi");    
    int midi_channel = midi_handler.is_active_channel(new_msg[0]);    
    // if correct channel was found, then send midi message
    if (midi_channel > -1) {
        Serial_Handler serial_handler = (Serial_Handler) handlers.get("serial");    
        serial_handler.send_msg(new_msg);
    }
}

void midi_to_serial(int channel, int num, int val) {
    int midi_channel = midi_handler.is_active_channel(channel);
    
    // if correct channel was found, then send midi message
    if (midi_channel > -1) {
        byte[] new_msg = {(byte)channel, (byte)num, (byte)val};
        serial_handler.send_msg(new_msg);
    }
}

  
}
