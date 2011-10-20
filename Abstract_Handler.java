
// abstract class that provides an basis for MIDI and Serial handler classes
class Abstract_Handler extends AMUP_element {
  
  String name;
  String device_name;
  String[] device_list = {};
  int device_number = -1;
  boolean device_connected = false;
  
  public Abstract_Handler(String _name){
    name = _name;  
    device_number = -1;
    device_connected = false;
    device_name = "";
  }
  
  public String name() {
    return name;  
  }

  public String device_name() {
    return device_name;  
  }
  
  public String[] device_list() {
    return device_list;
  }

  public boolean connect(int port_number) {
    return false;
  }

  public void disconnect() {
  }  

  public boolean connected() {
    return device_connected;
  }  

  public int connected_device_number() {
    if (connected()) return device_number;
    else return -1;
  }  
      
  public void read(byte[] data) {
  }
  
  public void send_msg(byte[] data) {
  } 
  
}
