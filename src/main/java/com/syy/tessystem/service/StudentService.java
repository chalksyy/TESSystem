package com.syy.tessystem.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.syy.tessystem.entities.Student;
import com.syy.tessystem.entities.StudentExample;
import com.syy.tessystem.mapper.StudentMapper;
import com.syy.tessystem.util.CreateCode;
import com.syy.tessystem.util.JavaWebToken;
import com.syy.tessystem.util.RSAEncrypt;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Ke
 * @Date 2022/2/10 12:51
 * @Description
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StudentService {

    @Resource
    StudentMapper studentMapper;

    @Resource
    CodeService codeService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public int add(Integer sno,String name, String password,String phone,Integer gender){
        String decrypt = "";
        try {
            decrypt = RSAEncrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentMapper.add(sno, name, decrypt, phone, gender);
    }

    public Map<String, Object> login(Integer sNo, String password) {

        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andSnoEqualTo(sNo);

        /*criteria.andPasswordEqualTo(password);*/
        List<Student> students = studentMapper.selectByExample(studentExample);



        if (students.size() == 0) {
            return null;
        }

        Student student = students.get(0);
        String decrypt = "";
        try {
            decrypt = RSAEncrypt.decrypt(student.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (student != null && password.equals(decrypt)) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("id", student.getId());
            String token = JavaWebToken.createJavaWebToken(map);

            ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
            forValue.set("userToken:" + token, String.valueOf(student.getId()));
            //stringRedisTemplate.expire("userToken:" + token, 7, TimeUnit.DAYS);
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("student", student);
            studentMap.put("token", token);
            return studentMap;
        } else {
            return null;
        }
    }

    public Student getById(Integer id){
        Student student = studentMapper.getById(id);
        try {
            student.setPassword(RSAEncrypt.decrypt(student.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return student;
    }

    public PageInfo<Student> getAll(String numStr,String nameStr,String limit,String page){

        Integer pageNum = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(limit);
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.getAll(numStr,nameStr);
        PageInfo<Student> pageInfo = new PageInfo<>(studentList);
        return pageInfo;
    };

    public Integer update(Integer id,Integer sno,String name,String phone,String password,Integer gender){
        String decrypt = "";
        try {
            decrypt = RSAEncrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentMapper.update(id, sno, name, phone, decrypt, gender);
    }

    public Integer delete(Integer id){
        return studentMapper.delete(id);
    }


    public String checkup(String token) {

        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        String s = forValue.get("userToken:" + token);
        if (s==null||s.length()==0){
            return null;
        }
        return s;

    }

    public Boolean checkPwd(String token,String password) {

        Map<String, Object> stringObjectMap = JavaWebToken.parserJavaWebToken(token);
        Integer id = (Integer) stringObjectMap.get("id");

        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andIdEqualTo(id);

        /*criteria.andPasswordEqualTo(password);*/
        List<Student> students = studentMapper.selectByExample(studentExample);

        if (students.size() == 0) {
            return null;
        }

        Student student = students.get(0);
        String decrypt = "";
        try {
            decrypt = RSAEncrypt.decrypt(student.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (student != null && password.equals(decrypt)) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteToken(String token) {

        stringRedisTemplate.delete("userToken:" + token);

    }

    public String updatePassword(String token,String password) {

        Map<String, Object> stringObjectMap = JavaWebToken.parserJavaWebToken(token);
        Integer id = (Integer) stringObjectMap.get("id");

        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andIdEqualTo(id);

        /*criteria.andPasswordEqualTo(password);*/
        List<Student> students = studentMapper.selectByExample(studentExample);

        if (students.size() == 0) {
            return null;
        }

        Student student = students.get(0);
        String encrypt = "";
        try {
            encrypt = RSAEncrypt.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (student != null) {

            if(studentMapper.updatePassword(id,encrypt)>0){
                return "AC";
            }
            return "false";
        } else {
            return "false";
        }

    }


    public String getCode(String phone, String checkResult) {

        String code = CreateCode.getCode(6);
        String s = codeService.setCodeWithoutAddr(code, phone,checkResult);

        return s;
    }

    public Integer updateRole(Integer studentId,Integer role){
        return studentMapper.updateRole(studentId,role);
    }

}