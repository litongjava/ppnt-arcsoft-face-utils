package top.ppnt.arcsoft.face.utils;

/**
 * @author create by Ping E Lee on 2022年4月4日 下午9:21:49 
 *
 */
public class FaceAppId{
  // 从官网获取
  private static String appId = "8QjdYNbG1WWUppiyUzYRt1bndrC9bt6vPeUaJSrCmwWF";
  private static String sdkKeyWin = "3VZNTTvjujymVFA4r6yenbaio3yZiyy6J2q6AedJa3in";
  private static String libPathWin = "D:\\arcsoft_lib\\WIN64";
  private static String sdkKeyLinux = "3VZNTTvjujymVFA4r6yenbaiewQNe7gsT3VVJSBLZ29A";
  private static String libPathLinux = "/opt/arcsoft_lib/LINUX64";
  public static String getAppId() {
    return appId;
  }
  public static void setAppId(String appId) {
    FaceAppId.appId = appId;
  }
  public static String getSdkKeyWin() {
    return sdkKeyWin;
  }
  public static void setSdkKeyWin(String sdkKeyWin) {
    FaceAppId.sdkKeyWin = sdkKeyWin;
  }
  public static String getLibPathWin() {
    return libPathWin;
  }
  public static void setLibPathWin(String libPathWin) {
    FaceAppId.libPathWin = libPathWin;
  }
  public static String getSdkKeyLinux() {
    return sdkKeyLinux;
  }
  public static void setSdkKeyLinux(String sdkKeyLinux) {
    FaceAppId.sdkKeyLinux = sdkKeyLinux;
  }
  public static String getLibPathLinux() {
    return libPathLinux;
  }
  public static void setLibPathLinux(String libPathLinux) {
    FaceAppId.libPathLinux = libPathLinux;
  }

}
