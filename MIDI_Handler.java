import processing.core.PApplet;
import themidibus.*; 

public class MIDI_Handler extends Abstract_Handler{
  
    MidiBus port; // The MidiBus
  
    int midi_channels_raw[] = {-80, -79, -65};
    int midi_channels[] = {0, 1, 15};
    int msg_count_output = 0;
    final int midi_channel_offset = 176;
   
    public MIDI_Handler(String _name) {
        super(_name);
        device_connected = false;
        controller_connected = false;
    
        MidiBus.list();
        device_list = MidiBus.availableInputs();
    }
  
  public boolean connect(int port_number) {
    if (port_number < device_list.length) {
        device_number = port_number;
        try {
            port = new MidiBus(processing_app,  port_number, port_number);
            device_connected = true;
            device_name = device_list[device_number];
        } catch (Exception e) {
            device_connected = false;
            device_name = "";
        }
    }         

    if (!device_connected) return false;
    else return true;
  }

  public void disconnect() {
      device_connected = false;
      port.stop();
  }
  
  public void read(byte[] data) {
        int channel = (int)(data[0] & 0xFF);
        int num = (int)(data[1] & 0xFF);
        int val = (int)(data[2] & 0xFF);
        
//	processing_app.print("Raw Midi Data:");
//	processing_app.print("channel:"+ channel + " num " + num + " val " + val);
        if (controller_connected) {
            controller.midi_to_serial(data);  
        }
  }
  
  public void send(byte[] data) {
      if (data.length != 3 || !connected()) return;
      port.sendControllerChange((int)data[0], (int)(data[1]), (int)(data[2])); // Send a controllerChange
  }
  
  int channel_count() {
    return midi_channels_raw.length; 
  }
  
  int get_channel(int i) {
      if (i < midi_channels_raw.length) return midi_channels_raw[i];  
      else return -1;
  }
  
  public int is_active_channel(int new_msg_channel) {
    // convert midi channel number to number between 1 and 16
    if (new_msg_channel > midi_channel_offset) new_msg_channel = new_msg_channel - midi_channel_offset;

    // check if midi channel is current active
    for (int i = 0; i < midi_channels_raw.length; i++) {
        if (midi_channels_raw[i] == new_msg_channel) return new_msg_channel;
    } 
    return -1;
  }  
  
  
}
