package com.syy.tessystem.service;

import com.syy.tessystem.entities.*;
import com.syy.tessystem.mapper.QuestionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Ke
 * @Date 2022/3/6 21:44
 * @Description
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionService {

    @Resource
    QuestionMapper questionMapper;

    public Integer addQuestion(Integer id,
                               String text,
                               String option1,
                               String option2,
                               String option3,
                               String option4,
                               String answer,
                               Integer createrId,
                               Timestamp createTime,
                               Integer chapterId,
                               Integer modularId,
                               Integer diffculyt) {

        Integer insert = questionMapper.insert(id, text, option1, option2, option3, option4, answer, createrId, createTime, chapterId, modularId, diffculyt);

        return insert;

    }

    public List<QuestionPublicSc> getQuestionByPaperId(Integer id){
        return questionMapper.getQuestionByPaperId(id);
    }

    public QuestionPublicSc getQuestionById(Integer id) {

        QuestionPublicSc questionPublicSc = questionMapper.get(id);

        return questionPublicSc;

    }

    public List<QuestionPublicSc> getAllSC() {

        List<QuestionPublicSc> publicScs = questionMapper.getAll();

        return publicScs;

    }

    public boolean checkUser(Integer userId) {

        Integer role = questionMapper.getUserRoleById(userId);

        if (role != null && role == 1) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkUser2(Integer userId) {
        Integer role = questionMapper.getUserRoleById(userId);

        if (role != null && role == 2) {
            return true;
        } else {
            return false;
        }
    }

    public List<QuestionPublicSc> getQuestionWithoutExamine(Integer courseId) {

        List<QuestionPublicSc> quesWithoutExamByCId = questionMapper.getQuesWithoutExamByCId(courseId);

        return quesWithoutExamByCId;

    }

    public boolean examineQuestion(Integer userId, Integer questionId, Integer result) {

        Timestamp examineTime = new Timestamp(System.currentTimeMillis());

        Integer examine = questionMapper.updateExamine(userId, questionId, result, examineTime);

        if (examine == 1) {
            return true;
        } else {
            return false;
        }

    }

    public Integer deleteQuestion(Integer id) {

        int result = questionMapper.delete(id);

        return result;

    }

    public Integer updateQuestion(Integer id,
                               String text,
                               String option1,
                               String option2,
                               String option3,
                               String option4,
                               String answer,
                               Integer chapterId,
                               Integer modularId,
                               Integer diffculyt) {

        Integer update = questionMapper.update(id,text, option1, option2, option3, option4, answer, chapterId, modularId, diffculyt);

        return update;

    }

    public boolean addQuestionComp(String content, String answer1, String answer2, String answer3, Integer createrId, Timestamp createTime, Integer chapterId, Integer modularId, Integer difficulty) {

        Integer integer = questionMapper.insertComp(content, answer1, answer2, answer3, createrId, createTime, chapterId, modularId, difficulty);

        if (integer!=0){
            return true;
        }else {
            return false;
        }

    }


    public Integer deleteComp(Integer id){
        return questionMapper.deleteComp(id);
    }

    public Integer updateComp(Integer id,String content, String answer1, String answer2, String answer3, Integer chapterId, Integer modularId, Integer difficulty){
        return questionMapper.updateComp(id, content, answer1, answer2, answer3, chapterId, modularId, difficulty);
    }

    public QuestionPublicComp getCompById(Integer id){
        return questionMapper.getComp(id);

    }

    public List<QuestionPublicComp> getAllComp(){
        return questionMapper.getAllComp();

    }

    public List<QuestionPublicSc> getQuestionScInCondition(Integer courseId, String type, Integer chapterId, Integer modularId, String content) {

        questionMapper.getQuestionScInCondition(chapterId,modularId,content);
        return null;
    }

    public List getAllQuestionInCondition(String type, Integer courseId, Integer chapterId, Integer modularId, String content) {

        List<QuestionPublicScWithName> questionScInCondition = null;
        List<QuestionPublicCompWithName> questionCompInCondition = null;

        if ("选择题".equals(type)){
            questionScInCondition = questionMapper.getQuestionScInCondition(chapterId, modularId, content);
            return questionScInCondition;
        }else {
            questionCompInCondition = questionMapper.getQuestionCompInCondition(chapterId, modularId, content);
            return questionCompInCondition;
        }

        /*if (map.size()!=0){
            return map;
        }else {
            return null;
        }*/

    }
    public List getAllQuestionInConditionWithName(String type,  Integer chapterId, Integer modularId, String content) {

        List<QuestionPublicScWithName> questionScInCondition = null;
        List<QuestionPublicCompWithName> questionCompInCondition = null;

        if ("选择题".equals(type)){
            questionScInCondition = questionMapper.getQuestionScInCondition(chapterId, modularId, content);
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (QuestionPublicScWithName questionPublicScWithName:questionScInCondition){
                Timestamp createTime = questionPublicScWithName.getCreateTime();

            }*/
            return questionScInCondition;
        }else {
            questionCompInCondition = questionMapper.getQuestionCompInCondition(chapterId, modularId, content);
            return questionCompInCondition;
        }

    }

    public List<QuestionEntity> getQuestionEntity(Integer type, List<Integer> answerIdList) {

        List<QuestionEntity> questionEntities = new ArrayList<>();

        //选择题
        if(type==1){
            for (int i = 0; i < answerIdList.size(); i++) {
                QuestionPublicSc questionPublicSc = questionMapper.get(answerIdList.get(i));
                ArrayList<Answer> answers = new ArrayList<>();
                answers.add(new Answer("A",questionPublicSc.getOption1()));
                answers.add(new Answer("B",questionPublicSc.getOption2()));
                answers.add(new Answer("C",questionPublicSc.getOption3()));
                answers.add(new Answer("D",questionPublicSc.getOption4()));
                ArrayList<String> correct = new ArrayList<>();
                correct.add(questionPublicSc.getAnswer());
                QuestionEntity questionEntity = new QuestionEntity(1,questionPublicSc.getText(),answers,correct,1);
                questionEntities.add(questionEntity);
            }
        }else if (type==4){
            for (int i = 0; i < answerIdList.size(); i++) {
                QuestionPublicComp questionPublicComp = questionMapper.getComp(answerIdList.get(i));
                ArrayList<Answer> answers = new ArrayList<>();
                ArrayList<String> correct = new ArrayList<>();
                correct.add(questionPublicComp.getAnswer1());
                correct.add(questionPublicComp.getAnswer2());
                correct.add(questionPublicComp.getAnswer3());
                QuestionEntity questionEntity = new QuestionEntity(4,questionPublicComp.getContent(),answers,correct,4);
                questionEntities.add(questionEntity);
            }
        }

        return questionEntities;
    }
}