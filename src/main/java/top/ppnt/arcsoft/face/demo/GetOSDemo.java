package top.ppnt.arcsoft.face.demo;

/**
 * @author Ping E Lee
 *
 */
public class GetOSDemo {
  public static void main(String[] args) {
    //Windows 10 or Linux
    String osName = System.getProperty("os.name");
    System.out.println(osName);
  }
}
