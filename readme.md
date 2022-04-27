## ppnt-arcsoft-face-utils
基于虹软人脸识别SDK封装的人脸识别库,自动适应linux和windows环境
### ppnt-arcsoft-face-utils快速入门
#### 快速入门
引入依赖
```
<dependency>
  <groupId>top.ppnt</groupId>
  <artifactId>ppnt-arcsoft-face-utils</artifactId>
  <version>1.0</version>
</dependency>
```

复制SDK文件 
将ddl文件或者so文件复制到下面的文件夹  
windows:D:\\arcsoft_lib\\WIN64  
linux:/opt/arcsoft_lib/LINUX64  

##### 整合spring-boot
整合Controller示例如下
调用FaceEngineUtils,FaceEngineUtil会自动匹配windows环境和linux环境
```
import java.io.File;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;
import com.smu.recognize.utils.MultipartFileUtils;

import top.ppnt.arcsoft.face.utils.FaceEngineUtils;
import top.ppnt.arcsoft.face.utils.FaceResult;

/**
 * @author create by Ping E Lee on 2022年4月4日 下午7:54:15 
 *
 */
@RestController
@RequestMapping("face/engine")
public class FaceController {

  /**
   * 检查服务是否有效
   * @return
   */
  @RequestMapping("check")
  public FaceResult<Void> check() {
    return FaceEngineUtils.check();
  }

  /**
   * 获取人脸特征
   * @param file
   * @return
   * @throws Exception
   */
  @RequestMapping("getFaceFeature")
  public FaceResult<FaceFeature> getFaceFeature(MultipartFile file) throws Exception {
    File file1 = MultipartFileUtils.transferToFile(file);
    return FaceEngineUtils.getFaceFeature(file1);
  }

  /**
   * 人脸特征比对
   * @param srcByte
   * @param dstByte
   * @return
   */
  @RequestMapping("compare")
  public FaceResult<FaceSimilar> compare(byte[] srcByte, byte[] dstByte) {
    FaceFeature src = new FaceFeature(srcByte);
    FaceFeature dst = new FaceFeature(dstByte);
    FaceSimilar faceSimilar = FaceEngineUtils.compare(src, dst);
    return new FaceResult<>(faceSimilar);
  }

  /**
   * 人脸特征比对
   */
  @RequestMapping("compare")
  public FaceResult<FaceSimilar> compareString(String srcStr, String dstStr) {
    byte[] srcByte = org.apache.commons.codec.binary.Base64.decodeBase64(srcStr);
    byte[] dstByte = org.apache.commons.codec.binary.Base64.decodeBase64(dstStr);
    FaceFeature src = new FaceFeature(srcByte);
    FaceFeature dst = new FaceFeature(dstByte);
    FaceSimilar faceSimilar = FaceEngineUtils.compare(src, dst);
    return new FaceResult<>(faceSimilar);
  }
}
```
##### 整合jfinal
```
package top.ppnt.modules.asrsoft.face.controller;

import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.upload.UploadFile;

import top.ppnt.arcsoft.face.utils.FaceEngineUtils;
import top.ppnt.arcsoft.face.utils.FaceResult;

/**
 * @author Ping E Lee
 *
 */
@Path("api/arcsoft/face")
public class ArcSoftFaceController extends Controller {
  /**
   * 检查服务是否有效
   * @return
   */
  public void check() {
    renderJson(FaceEngineUtils.check());
  }

  /**
   * 获取人脸特征
   * @param file
   * @return
   * @throws Exception
   */
  public void getFaceFeature() throws Exception {
    UploadFile uploadFile = getFile();
    renderJson(FaceEngineUtils.getFaceFeature(uploadFile.getFile()));
  }

  /**
   * 人脸特征比对
   * @param srcByte
   * @param dstByte
   * @return
   */
  public void compare(byte[] srcByte, byte[] dstByte) {
    FaceFeature src = new FaceFeature(srcByte);
    FaceFeature dst = new FaceFeature(dstByte);
    FaceSimilar faceSimilar = FaceEngineUtils.compare(src, dst);
    renderJson(new FaceResult<>(faceSimilar));
  }

  /**
   * 人脸特征比对
   */
  public void compareString(String srcStr, String dstStr) {
    byte[] srcByte = org.apache.commons.codec.binary.Base64.decodeBase64(srcStr);
    byte[] dstByte = org.apache.commons.codec.binary.Base64.decodeBase64(dstStr);
    FaceFeature src = new FaceFeature(srcByte);
    FaceFeature dst = new FaceFeature(dstByte);
    FaceSimilar faceSimilar = FaceEngineUtils.compare(src, dst);
    renderJson(new FaceResult<>(faceSimilar));
  }
}
```


### 其他使用
#### 初始化引擎
使用FaceEngineUtils初始化引擎,测试代码如下
```
  private FaceEngine faceEngine = new FaceEngine(FaceEngineUtils.getLibPath());

  //构造方法
  {
    EngineConfiguration engineConfiguration = getFaceConfigraution();
    // 获取FaceEngine
    int activeCode = FaceEngineUtils.activteEngine(faceEngine);
    if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
      System.out.println("引擎激活失败");
    } else {
      System.out.println("引擎激活成功");
    }
    int initCode = faceEngine.init(engineConfiguration);

    System.out.println(initCode);
    if (initCode != ErrorInfo.MOK.getValue()) {
      System.out.println("初始化引擎失败");
    }
  }

  /**
   * 获取人脸配置信息
   * @return
   */
  private EngineConfiguration getFaceConfigraution() {
    // 引擎配置
    EngineConfiguration engineConfiguration = new EngineConfiguration();
    // 检测模式 图片/视频，这里选择的是图片模式
    engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
    // 人脸检测角度
    engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);
    // 功能配置
    FunctionConfiguration functionConfiguration = new FunctionConfiguration();
    // 是否支持年龄检测
    functionConfiguration.setSupportAge(true);
    // 是否支持3D人脸检测
    functionConfiguration.setSupportFace3dAngle(true);
    // 是否支持人脸检测
    functionConfiguration.setSupportFaceDetect(true);
    // 是否支持人脸识别
    functionConfiguration.setSupportFaceRecognition(true);
    // 是否支持性别检测
    functionConfiguration.setSupportGender(true);
    // 是否支持RGB活体检测
    functionConfiguration.setSupportLiveness(true);
    // 是否支持RGB活体检测
    functionConfiguration.setSupportIRLiveness(true);
    // 设置引擎功能配置
    engineConfiguration.setFunctionConfiguration(functionConfiguration);
    // 初始化引擎
    return engineConfiguration;
  }
```