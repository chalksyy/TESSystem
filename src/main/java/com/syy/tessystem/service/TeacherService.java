package com.syy.tessystem.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syy.tessystem.entities.Student;
import com.syy.tessystem.entities.Teacher;
import com.syy.tessystem.mapper.TeacherMapper;
import com.syy.tessystem.util.JavaWebToken;
import com.syy.tessystem.util.RSAEncrypt;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Ke
 * @Date 2022/3/13 19:32
 * @Description
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TeacherService {

    @Resource
    TeacherMapper teacherMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public int add(Integer tno,String name, String password,String phone,Integer gender){
        String decrypt = "";
        try {
            decrypt = RSAEncrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherMapper.add(tno, name, decrypt, phone, gender);
    }

    public String login(Integer tNo, String password) {

        Teacher teacher = teacherMapper.login(tNo);

        if (teacher != null) {
            String decrypt = "";

            try {
                decrypt = RSAEncrypt.decrypt(teacher.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (decrypt.equals(password)) {
                HashMap<String, Object> map = new HashMap<>(2);
                map.put("id", teacher.getId());
                String token = JavaWebToken.createJavaWebToken(map);

                ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
                forValue.set("userToken:" + token, String.valueOf(teacher.getId()));
                //stringRedisTemplate.expire("userToken:" + token, 7, TimeUnit.DAYS);
                return token;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public Teacher getTeacher(Integer id) {

        Teacher teacher = teacherMapper.getTeacherById(id);
        try {
            teacher.setPassword(RSAEncrypt.decrypt(teacher.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teacher;

    }

    public PageInfo<Teacher> getAll(String numStr,String nameStr,String limit,String page){

        Integer pageNum = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(limit);
        PageHelper.startPage(pageNum, pageSize);
        List<Teacher> teacherList = teacherMapper.getAll(numStr,nameStr);
        PageInfo<Teacher> pageInfo = new PageInfo<>(teacherList);
        return pageInfo;

    }
    public Integer delete(Integer id){
        return teacherMapper.delete(id);
    }

    public Integer update(Integer id,Integer tno,String name,String phone,String password,Integer gender){
        String decrypt = "";
        try {
            decrypt = RSAEncrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherMapper.updateTeacher(id, tno, name, phone, decrypt, gender);
    }

    public Integer updateRole(Integer teacherId,Integer role){
        return teacherMapper.updateRole(teacherId,role);
    }
}