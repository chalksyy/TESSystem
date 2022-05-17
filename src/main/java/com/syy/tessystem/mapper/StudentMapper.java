package com.syy.tessystem.mapper;

import com.syy.tessystem.entities.Student;
import com.syy.tessystem.entities.StudentExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    /**
     * create by: Chalksyy
     * description: 添加学生
     * create time: 2022/5/17 19:20
     * @return
     */
    int add(@Param("sno") Integer sno,
            @Param("name") String name,
            @Param("password") String password,
            @Param("grade") Integer grade,
            @Param("gender") Integer gender);

    Integer updateRole(@Param("studentId") Integer teacherId,
                       @Param("role") Integer role);

    long countByExample(StudentExample example);

    int deleteByExample(StudentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    List<Student> selectByExample(StudentExample example);

    Student selectByPrimaryKey(Integer id);

    int updatePassword(@Param("id") Integer id, @Param("password") String password);

    int updateByExampleSelective(@Param("record") Student record, @Param("example") StudentExample example);

    int updateByExample(@Param("record") Student record, @Param("example") StudentExample example);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);


}