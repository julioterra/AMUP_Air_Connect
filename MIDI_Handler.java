
 import processing.core.PApplet;
 import themidibus.*; //Import the library


public class MIDI_Handler extends Abstract_Handler{
  
  boolean midi_connected;
 String midi_device_name = "Traktor";
 boolean midi_found = false;
 int midi_channels[] = {0, 1, 15};
 int msg_count_output = 0;
 byte new_msg_output [] = new byte [3];
 boolean new_msg_output_flag = false;
 final int midi_channel_offset = 176;
 

// shared variables
  String[] device_list = {};


  public MIDI_Handler(String _name) {
    super(_name);
    midi_connected = false;
    controller_connected = false;
    // create list of midi devices and notify the controller
  }

  public String[] device_list() {
    return device_list;
  }

  public void connect(int port_number) {
  }
  
  public boolean connected() {
    return midi_connected;  
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
  
  public void send(byte[] data) {
    
    
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
