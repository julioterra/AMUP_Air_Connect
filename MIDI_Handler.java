
 import processing.core.PApplet;
 import themidibus.*; //Import the library


public class MIDI_Handler extends Abstract_Handler{
  
 MidiBus port; // The MidiBus

 String midi_device_name = "Traktor";
 int midi_channels[] = {0, 1, 15};
 int msg_count_output = 0;
 byte new_msg_output [] = new byte [3];
 boolean new_msg_output_flag = false;
 final int midi_channel_offset = 176;
 

//// shared variables
//  String[] device_list = {};


  public MIDI_Handler(String _name) {
    super(_name);
    device_connected = false;
    controller_connected = false;

    MidiBus.list();
    device_list = MidiBus.availableInputs();
  }

  public String[] device_list() {
    return device_list;
  }

  public boolean connect(int port_number) {
    if (port_number < device_list.length) {
        device_number = port_number;
        try {
            port = new MidiBus(processing_app,  port_number, port_number);
            device_connected = true;
        } catch (Exception e) {
            device_connected = false;
        }
    }         
    if (!device_connected) processing_app.println (name + " NOT found");
    else processing_app.println(name + "found: " + device_list[port_number]);

    if (!device_connected) return false;
    else return true;
  }
  
  public boolean connected() {
    return device_connected;  
  }  

  public void read(byte[] data) {
        int channel = (int)(data[0] & 0xFF);
        int num = (int)(data[1] & 0xFF);
        int val = (int)(data[2] & 0xFF);
        
	processing_app.print("Raw Midi Data:");
	processing_app.print("channel:"+ channel + " num " + num + " val " + val);
        if (controller_connected) {
            controller.midi_to_serial(channel, num, val);  
        }
  }
  
  public void send(byte[] new_msg) {
      if (new_msg.length != 3 || !connected()) return;
      port.sendControllerChange((int)new_msg[0], (int)(new_msg[1]), (int)(new_msg[2])); // Send a controllerChange
  }
  
  int channel_count() {
    return midi_channels.length; 
  }
  
  int get_channel(int i) {
      if (i < midi_channels.length) return midi_channels[i];  
      else return -1;
  }
  
  public int is_active_channel(int new_msg_channel) {
    // convert midi channel number to number between 1 and 16
    if (new_msg_channel > midi_channel_offset) new_msg_channel = new_msg_channel - midi_channel_offset;

    // check if midi channel is current active
    for (int i = 0; i < midi_channels.length; i++) {
        if (midi_channels[i] == new_msg_channel) return new_msg_channel;
    } 
    return -1;
  }  
  
  
}
