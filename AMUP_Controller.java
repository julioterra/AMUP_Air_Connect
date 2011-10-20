import processing.core.PApplet;
import java.util.HashMap;
import java.util.ArrayList;

public class AMUP_Controller extends AMUP_element{

    User_Interface user_interface; 
    HashMap<String, Abstract_Handler> handlers;
    String[] handler_name_keys;
    
    public AMUP_Controller (Abstract_Handler[] _handlers, User_Interface _user_interface) {
        handlers = new HashMap<String, Abstract_Handler>();
        String[] handler_name_keys = new String[_handlers.length];
        
        for (int i = 0; i < _handlers.length ; i ++) {
            handlers.put(_handlers[i].name, _handlers[i]);
            handler_name_keys[i] = _handlers[i].name;
        }
        
        user_interface = _user_interface;
        user_interface.create_handler_menus(handler_name_keys, 20, 20);
        for (int i = 0; i < handler_name_keys.length ; i ++) {
            user_interface.add_menu_items(handler_name_keys[i], handlers.get(handler_name_keys[i]).device_list());  
        }
    }
    
    public void connect_handler(String _name, int _port_number) {
        if (handlers.get(_name).connected()) return;
        boolean connection_status = handlers.get(_name).connect(_port_number);
        user_interface.update_toggles(_name, connection_status);  
        if (connection_status) user_interface.updateMessageStatus(_name, "established connection with " + handlers.get(_name).device_name() + "\n\n\n\n\n\n\n\n\n\n\n\n\n");
        else user_interface.updateMessageStatus(_name, "unable to establish connection. Choose another port\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
    
    public void disconnect_handler(String _name, int _port_number) {
        if (!handlers.get(_name).connected()) return;
        handlers.get(_name).disconnect();
        user_interface.update_toggles(_name, false);  
        user_interface.updateMessageStatus(_name, "disconnected from " + handlers.get(_name).device_name() + "\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
  
  
    public void serial_to_midi(byte new_msg []) {
        // check that byte array contains appropriate number of elements and that midi is connected
        if (new_msg.length != 3 || !handlers.get("midi").connected()) return;
            
        // identify the channel of the current midi message
        MIDI_Handler midi_handler = (MIDI_Handler) handlers.get("midi");    
        int midi_channel = midi_handler.is_active_channel((int)(new_msg[0]));
        if (midi_channel != -1) {
            midi_handler.send_msg(new_msg);
            user_interface.updateMessageStatus("serial", ("" + processing_app.abs((int)new_msg[0]+256) + ", " + new_msg[1] + ", " + new_msg[2]));
        }
    }

    void midi_to_serial(byte new_msg[]) {
        // check that byte array contains appropriate number of elements and that midi is connected
        if (new_msg.length != 3 || !handlers.get("serial").connected()) return;
    
        MIDI_Handler midi_handler = (MIDI_Handler) handlers.get("midi");    
        int midi_channel = midi_handler.is_active_channel(new_msg[0]);    
        // if correct channel was found, then send midi message
        if (midi_channel > -1) {
            Serial_Handler serial_handler = (Serial_Handler) handlers.get("serial");    
            serial_handler.send_msg(new_msg);
            user_interface.updateMessageStatus("midi", processing_app.abs(new_msg[0]+256) + ", " + new_msg[1] + ", " + new_msg[2]);
        }
    }
}
