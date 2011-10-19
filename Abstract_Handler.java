

class Abstract_Handler extends AMUP_element {
  
  String name;
  String[] device_list = {};
  int device_number = -1;
  boolean device_connected = false;
  
  public Abstract_Handler(String _name){
    name = _name;  
    device_number = -1;
    device_connected = false;
  }
  
  public String device_name() {
    return name;  
  }
  
  public String[] device_list() {
    String[] temp_string = {};
    return temp_string;
  }

  public boolean connect(int port_number) {
    return false;
  }

  public void disconnect() {
  }  

  public boolean connected() {
    return false;
  }  

  public int connected_device_number() {
    if (connected()) return device_number;
    else return -1;
  }  
      
  public void read(byte [] new_byte) {
  }
  
  public void send_msg(byte[] data) {
  } 
  
}
