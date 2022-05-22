package com.syy.tessystem.mapper;

import com.syy.tessystem.entities.Student;
import com.syy.tessystem.entities.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Ke
 * @Date 2022/3/13 19:32
 * @Description
 * @Version 1.0
 */
@Mapper
public interface TeacherMapper {
    int add(@Param("tno") Integer tno,
            @Param("name") String name,
            @Param("password") String password,
            @Param("phone") String phone,
            @Param("gender") Integer gender);
    /**
     * create by: Chalksyy
     * description: TODO
     * create time: 2022/5/22 18:37
     * @return
     */
    Integer delete(@Param("id") Integer id);
    /**
     * 通过tNo返回信息
     * @param tNo
     * @return
     */
    Teacher login(@Param("tNo") Integer tNo);

    /**
     * 通过id获得老师信息
     * @param id
     * @return
     */
    Teacher getTeacherById(@Param("id") Integer id);

    /**
     * create by: syy
     * description: 修改教师资料
     * create time: 2022/3/14 20:07
     *
      * @Param: null
     * @return
     */
    Integer updateTeacher(@Param("id") Integer id,
                          @Param("tno") Integer tno,
                          @Param("name") String name,
                          @Param("phone") String phone,
                          @Param("password") String password,
                          @Param("gender") Integer gender);

    /**
     * 获得所有老师信息
     * @return
     */
    List<Teacher> getAll(@Param("numStr") String numStr,
                         @Param("nameStr") String nameStr);


    Integer updateRole(@Param("teacherId") Integer teacherId,
                       @Param("role") Integer role);
}