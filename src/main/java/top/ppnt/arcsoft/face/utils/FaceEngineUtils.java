package top.ppnt.arcsoft.face.utils;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FaceEngineUtils {

  // 从官网获取
  private static String appId;
  private static String sdkKey;
  private static String libPath;

  static {
    appId = FaceConstans.APP_ID;
    String osName = System.getProperty("os.name");
    if (osName.startsWith("Windows")) {
      sdkKey = FaceConstans.SDK_KEY_WIN;
      libPath = FaceConstans.LIB_PATH_WIN;
    } else {
      sdkKey = FaceConstans.SDK_KEY_LINUX;
      libPath = FaceConstans.LIB_PATH_LINUX;
    }
  }

  public static String getAppid() {
    return appId;
  }

  public static String getSdkKey() {
    return sdkKey;
  }

  public static String getLibPath() {
    return libPath;
  }

  /**
   * 激活引擎
   * @param faceEngine
   * @return
   */
  public static int activteEngine(FaceEngine faceEngine) {
    // 在方法内改变对象的值有用吗?测试失败
    // faceEngine = new FaceEngine(libPath);
    // 激活引擎
    int errorCode = faceEngine.activeOnline(appId, sdkKey);
    return errorCode;
  }

  /**
   * 获取人脸特征
   * @param file
   * @return
   */
  public static FaceResult<FaceFeature> getFaceFeature(File file) {
    return getFaceFeature(file, libPath);
  }

  public static FaceResult<FaceFeature> getFaceFeature(File file, String libPath) {
    // 引入动态库
    FaceEngine faceEngine = new FaceEngine(libPath);
    int errorCode = activteEngine(faceEngine);

    if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      System.out.println("引擎激活失败");
      return new FaceResult<FaceFeature>(errorCode, "引擎激活失败");
    }
    ActiveFileInfo activeFileInfo = new ActiveFileInfo();
    errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
    if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      System.out.println("获取激活文件信息失败");
      return new FaceResult<FaceFeature>(errorCode, "获取激活文件信息失败");
    }
    EngineConfiguration engineConfiguration = getEngineConfiguration();

    // 初始化引擎
    errorCode = faceEngine.init(engineConfiguration);

    if (errorCode != ErrorInfo.MOK.getValue()) {
      System.out.println("初始化引擎失败");
      return new FaceResult<FaceFeature>(errorCode, "初始化引擎失败");
    }
    // 人脸检测
    ImageInfo imageInfo = getRGBData(file);
    List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
    errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(),
        faceInfoList);
    // 特征提取
    // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
    if (faceInfoList.size() < 1) {
      return new FaceResult<FaceFeature>(1, "没有检测到人脸");
    }
    FaceFeature faceFeature = new FaceFeature();
    errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(),
        faceInfoList.get(0), faceFeature);
    System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
    System.out.println("特征值：" + faceFeature.getFeatureData().toString());

    faceEngine.unInit();
    // byte[] featureData = faceFeature.getFeatureData();

    return new FaceResult<FaceFeature>(faceFeature);
  }

  /**
   * 获取引擎配置
   * @return
   */
  public static EngineConfiguration getEngineConfiguration() {
    // 引擎配置
    EngineConfiguration engineConfiguration = new EngineConfiguration();
    engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
    engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
    engineConfiguration.setDetectFaceMaxNum(10);
    engineConfiguration.setDetectFaceScaleVal(16);
    // 功能配置
    FunctionConfiguration functionConfiguration = new FunctionConfiguration();
    functionConfiguration.setSupportAge(true);
    functionConfiguration.setSupportFace3dAngle(true);
    functionConfiguration.setSupportFaceDetect(true);
    functionConfiguration.setSupportFaceRecognition(true);
    functionConfiguration.setSupportGender(true);
    functionConfiguration.setSupportLiveness(true);
    functionConfiguration.setSupportIRLiveness(true);
    engineConfiguration.setFunctionConfiguration(functionConfiguration);
    return engineConfiguration;
  }

  /**
   * 检查服务是否有效
   * @return
   */
  public static FaceResult<Void> check() {
    // 引入动态库
    FaceEngine faceEngine = new FaceEngine(libPath);
    int errorCode = activteEngine(faceEngine);

    if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      log.info("引擎激活失败");
      return new FaceResult<Void>(errorCode, "引擎激活失败");
    }

    ActiveFileInfo activeFileInfo = new ActiveFileInfo();
    errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
    if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      log.info("获取激活文件信息失败");
      return new FaceResult<Void>(errorCode, "获取激活文件信息失败");
    }

    // 初始化引擎
    errorCode = faceEngine.init(getEngineConfiguration());

    if (errorCode != ErrorInfo.MOK.getValue()) {
      log.info("初始化引擎失败");
      return new FaceResult<Void>(errorCode, "初始化引擎失败");
    }
    faceEngine.unInit();
    return new FaceResult<Void>(errorCode, "正常");
  }

  /**
   * 比较人脸特征的相似度
   * @param src
   * @param dst
   * @return
   */
  public static FaceSimilar compare(FaceFeature src, FaceFeature dst) {
    return compare(src, dst, libPath);
  }

  public static FaceSimilar compare(FaceFeature src, FaceFeature dst, String libPath) {
    // 引入动态库
    FaceEngine faceEngine = new FaceEngine(libPath);
    int errorCode = activteEngine(faceEngine);

    if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      System.out.println("引擎激活失败");
    }
    ActiveFileInfo activeFileInfo = new ActiveFileInfo();
    errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
    if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      System.out.println("获取激活文件信息失败");
    }
    // 引擎配置
    EngineConfiguration engineConfiguration = new EngineConfiguration();
    engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
    engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
    engineConfiguration.setDetectFaceMaxNum(1);
    engineConfiguration.setDetectFaceScaleVal(16);

    // 功能配置
    FunctionConfiguration functionConfiguration = new FunctionConfiguration();
    functionConfiguration.setSupportFace3dAngle(true);
    functionConfiguration.setSupportFaceDetect(true);
    functionConfiguration.setSupportFaceRecognition(true);
    engineConfiguration.setFunctionConfiguration(functionConfiguration);

    // 初始化引擎
    errorCode = faceEngine.init(engineConfiguration);
    if (errorCode != ErrorInfo.MOK.getValue()) {
      System.out.println("初始化引擎失败");
    }
    // 人脸比对
    FaceSimilar faceSimilar = new FaceSimilar();
    errorCode = faceEngine.compareFaceFeature(src, dst, faceSimilar);

    System.out.println("相似度" + faceSimilar.getScore());
    if (faceEngine != null) {
      faceEngine.unInit();
    }
    return faceSimilar;
  }
}