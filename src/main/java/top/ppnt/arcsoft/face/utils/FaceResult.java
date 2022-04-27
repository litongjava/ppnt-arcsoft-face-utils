package top.ppnt.arcsoft.face.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author create by Ping E Lee on 2022年4月4日 下午9:34:35 
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceResult<T> {

  private int code = 0; // 状态码

  private String msg = "执行成功"; // 状态说明

  private T data; // 数据内容

  public FaceResult(T data) {
    this.data = data;
  }

  public FaceResult(int code) {
    this.code = code;
  }

  public FaceResult(String msg) {
    this.msg=msg;
  }

  public FaceResult(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

}
