package com.yangbingdong.security.util;

import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@UtilityClass
public class RestUtil {

    public void writeJson(HttpServletResponse response, Object result, HttpStatus httpStatus) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(httpStatus.value());
            out = response.getWriter();
            out.println(JSONObject.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }
}
