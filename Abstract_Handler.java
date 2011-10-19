

class Abstract_Handler extends AMUP_element {
  
  String name;
  
  public Abstract_Handler(String _name){
    name = _name;  
  }
  
  public String device_name() {
    return name;  
  }
  
  public String[] device_list() {
    String[] temp_string = {};
    return temp_string;
  }

  public void connect(int port_number) {
  }

  public boolean connected() {
    return false;
  }  
      
  public void read(byte [] new_byte) {
  }
  
  public void send_msg(byte[] data) {
  } 
  
}
