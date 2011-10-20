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
     HashMap<String, controlP5.Textlabel> handler_toggles_labels;
     HashMap<String, controlP5.Textarea> handler_text;
     HashMap<String, controlP5.Textlabel> handler_text_labels;
     HashMap<String, String> text_messages;
     ArrayList<String> handler_names;
    
     public User_Interface() {
        controlP5 = new ControlP5(processing_app);
        handler_menus = new HashMap<String, controlP5.DropdownList>();
        handler_toggles = new HashMap<String, controlP5.Toggle>();
        handler_toggles_labels = new HashMap<String, controlP5.Textlabel>();
        handler_text = new HashMap<String, controlP5.Textarea>();
        handler_text_labels = new HashMap<String, controlP5.Textlabel>();
        text_messages = new HashMap<String, String>();
        handler_names = new ArrayList<String>();
    }

    public void create_handler_menus(String[] _handler_names, int pos_x, int pos_y) {
        for (int i = 0; i < _handler_names.length; i ++) {
            pos_x = pos_x + i*250;
            handler_text.put(_handler_names[i], create_textarea(_handler_names[i],pos_x,pos_y+80,200,120));
            handler_toggles.put(_handler_names[i], create_toggle(_handler_names[i],pos_x,pos_y+25,15,15));
            handler_menus.put(_handler_names[i], create_dropdown(_handler_names[i],pos_x,pos_y+20,200,120));
            text_messages.put(_handler_names[i], "");
            handler_names.add(_handler_names[i]);
        }  
    }
    

    Textarea create_textarea(String name, int pos_x, int pos_y, int size_x, int size_y) {
        Textarea textarea = controlP5.addTextarea(name + "_textarea", "Please activate midi and serial.", pos_x, pos_y +12, size_x, size_y);
        controlP5.getGroup(name + "_textarea").setColorBackground(0xff555555);
        controlP5.getGroup(name + "_textarea").setColorLabel(0xff00ff00);
        handler_text_labels.put(name, controlP5.addTextlabel(name + "_textarea_label", "messages from: " + name.toUpperCase(), pos_x, pos_y));
        return textarea;
    }

    Toggle create_toggle(String name, int pos_x, int pos_y, int size_x, int size_y) {
        Toggle toggle = controlP5.addToggle(name + "_toggle", pos_x, pos_y , size_x, size_y);
        controlP5.controller(name + "_toggle").setColorBackground(0xffff0000);
        controlP5.controller(name + "_toggle").setColorActive(0xff00ff00);
        controlP5.controller(name + "_toggle").captionLabel().setVisible(false);
        handler_text_labels.put(name, controlP5.addTextlabel(name + "_toggle_label", "click to connect", pos_x + 20, pos_y + 3));
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
      ddl.setLabel("Select " + name + " from list");
      return ddl;
    }

    void add_menu_items(String _name, String[] items) {
      DropdownList ddl = handler_menus.get(_name);
      for(int i=0 ; i < items.length ; i++) {
        ddl.addItem(i + " " +items[i],i);
      }
    }

    void update_toggles(String _name, boolean connect_status) {
        handler_toggles.get(_name).setState(connect_status);
    }

    public void updateMessageStatus(String name, String input) {
        text_messages.put(name, (input + "\n" + text_messages.get(name)));
        if (text_messages.get(name).length() > 1000) text_messages.put(name, text_messages.get(name).substring(0, 499));
        handler_text.get(name).setText(text_messages.get(name));
    }

    // responsible for handling events associated to the user interface
    void controlEvent(ControlEvent theEvent) {
        if (theEvent.isController()){
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
