package com.syy.tessystem.controller;

import com.syy.tessystem.entities.AiFaceObject;
import com.syy.tessystem.entities.CommonResult;
import com.syy.tessystem.service.FaceService;
import com.syy.tessystem.util.FaceDetection;
import com.syy.tessystem.util.FaceRegistration;
import com.syy.tessystem.util.FaceSearch;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @Author Ke
 * @Date 2022/3/23 19:06
 * @Description
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping(value = "/face")
public class FaceController {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FaceService faceService;

    private String checkup(String token) {

        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        String s = forValue.get("userToken:" + token);
        if (s == null || s.length() == 0) {
            return null;
        }
        return s;

    }

    @PostMapping(value = "/addFace")
    public CommonResult<String> addFace(@RequestBody HashMap<String,Object> map){

        String token = String.valueOf(map.get("token"));
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        //Integer userId = Integer.valueOf(checkup);
        String imageBase64 = String.valueOf(map.get("imageBase64"));

        if (imageBase64==null){
            return new CommonResult<>(200,"图片未上传");
        }

        JSONObject faceDetection = FaceDetection.Facedetection(AiFaceObject.getClient(), imageBase64, "BASE64");

        if ("SUCCESS".equals(faceDetection.get("error_msg"))){
            JSONObject faceregistrtion = FaceRegistration.Faceregistrtion(AiFaceObject.getClient(), "student", "3", imageBase64, "BASE64");

            if ("SUCCESS".equals(faceregistrtion.get("error_msg"))){
                return new CommonResult<>(100,"添加成功");
            }else {
                return new CommonResult<>(200,"添加失败");
            }

        }else {
            return new CommonResult<>(200,"人脸校验失败");
        }

    }

    @PostMapping(value = "/search")
    public CommonResult<String> search(@RequestBody HashMap<String,Object> map){

        String token = String.valueOf(map.get("token"));
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        //Integer userId = Integer.valueOf(checkup);
        String imageBase64 = String.valueOf(map.get("imageBase64"));

        if (imageBase64==null){
            return new CommonResult<>(200,"图片未上传");
        }

        JSONObject faceDetection = FaceDetection.Facedetection(AiFaceObject.getClient(), imageBase64, "BASE64");

        if ("SUCCESS".equals(faceDetection.get("error_msg"))){

            JSONObject search = FaceSearch.search(AiFaceObject.getClient(), imageBase64, "BASE64", "student", checkup);


            if ("SUCCESS".equals(search.get("error_msg"))){
                return new CommonResult<>(100,"验证通过");
            }else {
                return new CommonResult<>(200,"验证失败");
            }

        }else {
            return new CommonResult<>(200,"人脸校验失败");
        }

    }

}