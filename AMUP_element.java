import processing.core.PApplet;

public class AMUP_element {

  static PApplet processing_app;
  static AMUP_Controller controller;
  
  static boolean controller_connected;
  
  public static void register_app(PApplet _processing_app) {
    processing_app = _processing_app; 
  }
      
  public static void register_controller(AMUP_Controller _controller) {
    controller = _controller; 
    controller_connected = true;    
  }
  
  
}
