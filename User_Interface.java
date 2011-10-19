import processing.core.PApplet;
import controlP5.*;
import java.util.HashMap;


public class User_Interface extends AMUP_element{
  
 static ControlP5 controlP5;
 controlP5.Textarea current_serial_messages;
 controlP5.Textarea current_midi_messages;
 controlP5.Textarea current_serial_list;    
 controlP5.Toggle serial_connection_toggle;

 controlP5.DropdownList serial_menu, midi_menu;
 
 HashMap<String, controlP5.DropdownList> handler_menus;
 


 final int left_margin = 20;
 final int top_margin = 20;

  
     public User_Interface() {
        controlP5 = new ControlP5(processing_app);
        handler_menus = new HashMap<String, controlP5.DropdownList>();

        serial_connection_toggle = controlP5.addToggle("serial_connection_toggle", left_margin, top_margin , 20, 20);
        current_serial_messages = controlP5.addTextarea("current_serial_messages", "waiting to start connection", left_margin, top_margin + 50, 150, 120);
        current_serial_list = controlP5.addTextarea("current_serial_list", "waiting to start connection", left_margin, top_margin + 330, 150, 120);
        current_midi_messages = controlP5.addTextarea("current_midi_messages", "waiting to start connection", left_margin, top_margin + 190, 150, 120);

//        serial_menu = customize_dropdown("Serial Devices",20,100,100,120);
//        customize_dropdown(serial_menu);
//        midi_menu = customize_dropdown("MIDI Devices",20,120,100,120);
//        customize_dropdown(midi_menu);


        current_serial_messages.setColorBackground(0xff555555);
        current_serial_list.setColorBackground(0xff555555);
        current_midi_messages.setColorBackground(0xff555555);

        controlP5.controller("serial_connection_toggle").setColorBackground(0xffcc0000);
        controlP5.controller("serial_connection_toggle").setColorActive(0xffcccc00);
        controlP5.controller("serial_connection_toggle").setCaptionLabel("Connect to AMUP Box");

    }

public void create_handler_menus(String[] handler_names, int pos_x, int pos_y) {
    for (int i = 0; i < handler_names.length; i ++) {
        handler_menus.put(handler_names[i], customize_dropdown(handler_names[i],pos_x,pos_y+(i*20),200,120));
    }  
}

DropdownList customize_dropdown(String name, int pos_x, int pos_y, int size_x, int size_y) {
  DropdownList ddl = controlP5.addDropdownList(name, pos_x, pos_y, size_x, size_y);
  ddl.setBackgroundColor(processing_app.color(190));
  ddl.setItemHeight(20);
  ddl.setBarHeight(15);
  ddl.captionLabel().set(name);
  ddl.captionLabel().style().marginTop = 3;
  ddl.captionLabel().style().marginLeft = 3;
  ddl.valueLabel().style().marginTop = 3;
  ddl.setColorBackground(processing_app.color(60));
  ddl.setColorActive(processing_app.color(255,128));
  return ddl;
}

void add_dropdown_items(String _name, String[] items) {
  DropdownList ddl = handler_menus.get(_name);
  for(int i=0 ; i < items.length ; i++) {
    ddl.addItem(items[i],i);
  }
}

  
  
}
