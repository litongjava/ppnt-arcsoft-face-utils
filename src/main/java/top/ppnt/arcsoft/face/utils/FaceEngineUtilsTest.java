package top.ppnt.arcsoft.face.utils;

import com.arcsoft.face.FaceFeature;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author create by Ping E Lee on 2022年4月4日 下午9:39:13 
 *
 */
@Slf4j
public class FaceEngineUtilsTest {
  public static void main(String[] args) {
    // FaceEngineUtils.check();
    getFaceFeatureForHuge();

  }

  /**
   * 获取huge的人脸特征
   */
  private static void getFaceFeatureForHuge() {
    File file = new File("D:\\opencv-images\\face\\huge.png");
    FaceResult<FaceFeature> faceResult = FaceEngineUtils.getFaceFeature(file);
    FaceFeature faceFeature = faceResult.getData();
    log.info("faceFeature:{}", faceFeature);
    // 保存的JSON
//    byte[] featureData = data.getFeatureData();
//    System.out.println(featureData.length);
  }

}
