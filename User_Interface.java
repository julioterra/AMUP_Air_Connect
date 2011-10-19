import processing.core.PApplet;
import controlP5.*;
import java.util.HashMap;
import java.util.ArrayList;


public class User_Interface extends AMUP_element{
  
 static ControlP5 controlP5;
 controlP5.Textarea current_serial_messages;
 controlP5.Textarea current_midi_messages;
 controlP5.Textarea current_serial_list;    
 controlP5.Toggle serial_connection_toggle;
 controlP5.DropdownList serial_menu, midi_menu;
 
 HashMap<String, controlP5.DropdownList> handler_menus;
 HashMap<String, controlP5.Toggle> handler_toggles;
 ArrayList<String> handler_names;


 final int left_margin = 20;
 final int top_margin = 20;

  
     public User_Interface() {
        controlP5 = new ControlP5(processing_app);
        handler_menus = new HashMap<String, controlP5.DropdownList>();
        handler_toggles = new HashMap<String, controlP5.Toggle>();
        handler_names = new ArrayList<String>();

        current_serial_messages = controlP5.addTextarea("current_serial_messages", "waiting to start connection", left_margin, top_margin + 50, 150, 120);
        current_serial_list = controlP5.addTextarea("current_serial_list", "waiting to start connection", left_margin, top_margin + 330, 150, 120);
        current_midi_messages = controlP5.addTextarea("current_midi_messages", "waiting to start connection", left_margin, top_margin + 190, 150, 120);

        current_serial_messages.setColorBackground(0xff555555);
        current_serial_list.setColorBackground(0xff555555);
        current_midi_messages.setColorBackground(0xff555555);

    }

    public void create_handler_menus(String[] _handler_names, int pos_x, int pos_y) {
        for (int i = 0; i < _handler_names.length; i ++) {
            pos_x = pos_x + i*250;
            handler_menus.put(_handler_names[i], create_dropdown(_handler_names[i],pos_x+25,pos_y+16,200,120));
            handler_toggles.put(_handler_names[i], create_toggle(_handler_names[i],pos_x,pos_y,15,15));
            handler_names.add(_handler_names[i]);
        }  
    }
    

    Toggle create_toggle(String name, int pos_x, int pos_y, int size_x, int size_y) {
        Toggle toggle = controlP5.addToggle(name + "_toggle", pos_x, pos_y , size_x, size_y);
        controlP5.controller(name + "_toggle").setColorBackground(0xffff0000);
        controlP5.controller(name + "_toggle").setColorActive(0xff00ff00);
        controlP5.controller(name + "_toggle").setCaptionLabel("Connect to " + name);
        return toggle;
    }

    DropdownList create_dropdown(String name, int pos_x, int pos_y, int size_x, int size_y) {
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
        ddl.addItem(i + " " +items[i],i);
      }
    }

    void update_toggles(String _name, boolean connect_status) {
        handler_toggles.get(_name).setState(connect_status);
    }

    void controlEvent(ControlEvent theEvent) {
    
        if(theEvent.isGroup()) {
            // HANDLE PULL DOWN MENUS
            for (int i = 0; i < handler_names.size(); i++ ) {
              if (theEvent.group().name() == handler_names.get(i)) {
//                  controller.connect_handler(handler_names.get(i),(int)theEvent.group().value()); 
              }
            }
        }
    
        else if (theEvent.isController()){
            for (int i = 0; i < handler_names.size(); i++ ) {
                if (theEvent.controller().name().contains(handler_names.get(i))) {
                    if (theEvent.controller().value() > 0) {
                        processing_app.println(handler_names.get(i) + ": open port " + handler_menus.get(handler_names.get(i)).value() );
                        controller.connect_handler(handler_names.get(i),(int)handler_menus.get(handler_names.get(i)).value()); 
                    } 
                    else {
                        processing_app.println(handler_names.get(i) + ": close port");
                        controller.disconnect_handler(handler_names.get(i),(int)handler_menus.get(handler_names.get(i)).value());                       
                    }
                }
            }
        }
    }  

}
