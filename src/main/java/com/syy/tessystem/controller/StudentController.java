package com.syy.tessystem.controller;

import com.syy.tessystem.entities.CommonResult;
import com.syy.tessystem.entities.Student;
import com.syy.tessystem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Ke
 * @Date 2022/2/9 21:56
 * @Description
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Resource
    StudentService studentService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private String checkup(String token) {

        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        String s = forValue.get("userToken:" + token);
        if (s == null || s.length() == 0) {
            return null;
        }
        return s;

    }

    @PostMapping(value = "/register")
    public CommonResult register(@RequestBody Map<String,String> map){

        String token = (String) map.get("token");
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }
//sno, name, grade, password, gender, role
        Integer sno = Integer.parseInt(map.get("sno"));
        String name = map.get("name");
        Integer grade = Integer.parseInt(map.get("grade"));
        String password = map.get("password");
        Integer gender = Integer.parseInt(map.get("gender"));

        Integer result = studentService.add(sno,name,password,grade,gender);

        return new CommonResult(200,"添加学生成功",null);
    }

    @PostMapping(value = "/login")
    public CommonResult login(@RequestBody Map<String,String> map) {


        Integer sNo = Integer.parseInt(map.get("sNo"));
        String password = map.get("password");

        Map<String, Object> studentMap = studentService.login(sNo, password);
        if (studentMap == null) {
            return new CommonResult(200, "用户名不存在或密码错误");
        }
        Student student = (Student) studentMap.get("student");
        String token = (String) studentMap.get("token");

        if (student == null) {
            return new CommonResult(200, "用户名不存在或密码错误");
        } else {
            if (student.getPhone().length() == 0) {
                return new CommonResult(100, "未绑定手机号", token);
            } else {
                return new CommonResult(100, "登录成功", token);
            }
        }

    }


    @PostMapping(value = "/test")
    public String test(@RequestBody Map map){

        String str = (String) map.get("str");

        return str;

    }

    @PostMapping(value = "getCode")
    public CommonResult<String> getCode(@RequestBody Map map){

        String token = (String)map.get("token");
        String checkResult = studentService.checkup(token);

        if (checkResult==null){
            return new CommonResult<>(200,"用户未登录或登录状态失效");
        }

        String phone = (String) map.get("phone");

        String code = studentService.getCode(phone, checkResult);

        return new CommonResult<>(100,"?",code);
    }

    @PostMapping(value = "changePwd")
    public CommonResult changePwd(@RequestBody Map map) {
        String oldPassword = (String) map.get("oldPassword");
        String token = (String) map.get("token");

        return new CommonResult(200,"修改失败");

    }

    @PostMapping(value = "updateStudentRole")
    public CommonResult<String> updateStudentRole(@RequestBody HashMap<String,String> map){

        String token = map.get("token");
        String checkup = studentService.checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        Integer studentId = Integer.parseInt(map.get("studentId"));
        Integer role = Integer.parseInt(map.get("role"));

        Integer result=0;
        if(role>-1&&role<=1){
            result = studentService.updateRole(studentId,role);
        } else {
            return new CommonResult<>(200,"role的取值应为0,1");
        }

        return result>0 ? new CommonResult<>(100,"更新成功"):new CommonResult<>(200,"更新失败");

    }


}