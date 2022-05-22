package com.syy.tessystem.controller;

import com.github.pagehelper.PageInfo;
import com.syy.tessystem.entities.CommonResult;
import com.syy.tessystem.entities.Student;
import com.syy.tessystem.entities.Teacher;
import com.syy.tessystem.service.TeacherService;
import com.syy.tessystem.util.JavaWebToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Ke
 * @Date 2022/3/13 19:31
 * @Description
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Resource
    TeacherService teacherService;

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
        Integer tno = Integer.parseInt(map.get("tno"));
        String name = map.get("name");
        String phone = map.get("phone");
        String password = map.get("password");
        Integer gender = Integer.parseInt(map.get("gender"));

        Integer result = teacherService.add(tno,name,password,phone,gender);

        return result>0 ? new CommonResult<>(100,"添加成功"):new CommonResult<>(200,"添加失败");
    }
    @PostMapping(value = "/login")
    public CommonResult<String> login(@RequestBody HashMap<String, String> map) {


        Integer tNo = Integer.parseInt(map.get("tNo"));

        String password = map.get("password");

        String token = teacherService.login(tNo, password);

        if (token != null) {
            return new CommonResult<>(100, "登录成功", token);
        }

        return new CommonResult<>(200, "登录失败");
    }

    @PostMapping(value = "/getTeacher")
    public CommonResult<Teacher> getTeacher(@RequestBody HashMap<String, String> map) {

        String token = map.get("token");
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        Teacher teacher = teacherService.getTeacher(Integer.parseInt(map.get("id")));

        return teacher != null ? new CommonResult<>(100,"获取教师信息成功",teacher):new CommonResult<>(200,"获取教师信息失败");

    }

    @PostMapping(value = "/getAllTeacher")
    public CommonResult<PageInfo<Teacher>> getAllTeacher(@RequestBody HashMap<String, String> map) {

        String token = map.get("token");
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        PageInfo<Teacher> teacherList = teacherService.getAll(map.get("numStr"),map.get("nameStr"),map.get("limit"),map.get("page"));

        return teacherList != null ? new CommonResult<>(100,"获取教师列表成功",teacherList):new CommonResult<>(200,"获取教师列表失败");

    }

    @PostMapping(value = "/update")
    public CommonResult<String> update(@RequestBody HashMap<String, String> map) {

        String token = map.get("token");
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        Integer id = Integer.parseInt(map.get("id"));
        Integer tno = Integer.parseInt(map.get("tno"));
        String name = map.get("name");
        String password = map.get("password");
        String phone = map.get("phone");
        Integer gender = Integer.parseInt(map.get("gender"));

        Integer result = teacherService.update(id,tno,name,phone,password,gender);

        return result > 0 ? new CommonResult<>(100, "更新成功") : new CommonResult<>(200, "更新失败");

    }

    @PostMapping(value = "updateTeacherRole")
    public CommonResult<String> updateTeacherRole(@RequestBody HashMap<String,String> map){

        String token = map.get("token");
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        Integer teacherId = Integer.parseInt(map.get("teacherId"));
        Integer role = Integer.parseInt(map.get("role"));

        Integer result=0;
        if(role>-1&&role<=2){
            result = teacherService.updateRole(teacherId,role);
        } else {
            return new CommonResult<>(200,"role的取值应为0,1,2");
        }

        return result>0 ? new CommonResult<>(100,"更新成功"):new CommonResult<>(200,"更新失败");

    }
    @PostMapping(value = "/delete")
    public CommonResult<String> delete(@RequestBody HashMap<String,String> map){

        String token = map.get("token");
        String checkup = checkup(token);

        if (checkup == null) {
            return new CommonResult<>(200, "用户未登录或登录状态失效", null);
        }

        Integer result = teacherService.delete(Integer.parseInt(map.get("id")));

        return result > 0 ? new CommonResult<>(100,"删除成功"):new CommonResult<>(200,"删除失败");

    }

}