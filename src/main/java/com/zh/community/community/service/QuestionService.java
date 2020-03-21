package com.zh.community.community.service;

import com.zh.community.community.dto.PaginationDto;
import com.zh.community.community.dto.QuestionDto;
import com.zh.community.community.exception.CustomizeErrorCodeEnum;
import com.zh.community.community.exception.CustomizeException;
import com.zh.community.community.mapper.QuestionExtMapper;
import com.zh.community.community.mapper.QuestionMapper;
import com.zh.community.community.mapper.UserMapper;
import com.zh.community.community.model.Question;
import com.zh.community.community.model.QuestionExample;
import com.zh.community.community.model.User;
import com.zh.community.community.model.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * created by ${host}
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    public List<QuestionDto> queryQuestion() {
        List<QuestionDto> questionDtos = new ArrayList<>();
        // 首先把问题数据捞出来

        List<Question> questionList = questionMapper.selectByExample(new QuestionExample());
        for (Question question : questionList) {
            //遍历数据添加用户信息
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(users.get(0));
            questionDtos.add(questionDto);
        }
        return questionDtos;
    }

    /**
     * 最新
     * @param paginationDto
     * @return
     */
    public PaginationDto queryPaginationQuestions(PaginationDto paginationDto) {
        int totalQuestions = (int)questionMapper.countByExample(new QuestionExample());
        paginationDto.setTotalSize(totalQuestions);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("GMT_CREATE DESC");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(paginationDto.getOffSize(), paginationDto.getPageSize()));
        List<QuestionDto> questionDtos = new ArrayList<>();
        // 首先把问题数据捞出来
        for (Question question : questions) {
            //遍历数据添加用户信息
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(users.get(0));
            questionDtos.add(questionDto);
        }
        paginationDto.setListQuestion(questionDtos);
        paginationDto.calculate();
        return paginationDto;
    }

    /**
     * 查询零回复
     * @param paginationDto
     * @return
     */
    public PaginationDto queryPaginationQuestionsForZeroComment(PaginationDto paginationDto) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCommentCountLessThanOrEqualTo(0);
        int totalQuestions = (int)questionMapper.countByExample(questionExample);
        paginationDto.setTotalSize(totalQuestions);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCommentCountLessThanOrEqualTo(0);
        example.setOrderByClause("GMT_CREATE DESC");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(paginationDto.getOffSize(), paginationDto.getPageSize()));
        List<QuestionDto> questionDtos = new ArrayList<>();
        // 首先把问题数据捞出来
        for (Question question : questions) {
            //遍历数据添加用户信息
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(users.get(0));
            questionDtos.add(questionDto);
        }
        paginationDto.setListQuestion(questionDtos);
        paginationDto.calculate();
        return paginationDto;
    }
    /**
     * 查询评论最多也就是最活跃话题
     * @param paginationDto
     * @return
     */
    public PaginationDto queryPaginationQuestionsForHot(PaginationDto paginationDto) {
        int totalQuestions = (int)questionMapper.countByExample(new QuestionExample());
        paginationDto.setTotalSize(totalQuestions);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("comment_count DESC");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(paginationDto.getOffSize(), paginationDto.getPageSize()));
        List<QuestionDto> questionDtos = new ArrayList<>();
        // 首先把问题数据捞出来
        for (Question question : questions) {
            //遍历数据添加用户信息
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(users.get(0));
            questionDtos.add(questionDto);
        }
        paginationDto.setListQuestion(questionDtos);
        paginationDto.calculate();
        return paginationDto;
    }
    public PaginationDto queryMyQuestions(Integer userId, PaginationDto paginationDto) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        int totalQuestions = (int)questionMapper.countByExample(questionExample);
        paginationDto.setTotalSize(totalQuestions);
        QuestionExample questionExampleQuery = new QuestionExample();
        questionExampleQuery.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExampleQuery, new RowBounds(paginationDto.getOffSize(), paginationDto.getPageSize()));
        List<QuestionDto> questionDtos = new ArrayList<>();
        // 首先把问题数据捞出来
        for (Question question : questions) {
            //遍历数据添加用户信息
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(users.get(0));
            questionDtos.add(questionDto);
        }
        paginationDto.setListQuestion(questionDtos);
        paginationDto.calculate();
        return paginationDto;
    }

    public QuestionDto getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(null == question){
            throw new CustomizeException(CustomizeErrorCodeEnum.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(userExample);
        questionDto.setUser(users.get(0));
        return questionDto;
    }


    public void insertOrUpdate(Question question) {
        if (question.getId() != null) {
            // 更新操作
            Question question1 = new Question();
            question1.setGmtModified(System.currentTimeMillis());
            question1.setCreator(question.getCreator());
            question1.setTitle(question.getTitle());
            question1.setDescription(question.getDescription());
            question1.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question1, questionExample);
        } else {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setPraiseCount(0);
            question.setViewCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }
    }

    public List<Question> quesryRecomendQuestions(QuestionDto questionDto){
        String[] split = questionDto.getTag().split("，");
        // 拼接查询正则表达式
        String collect = Arrays.stream(split).filter(str -> !str.isEmpty()).collect(Collectors.joining("|"));
        // 查询内容
        Map<String, Object> map = new HashMap<>();
        map.put("id", questionDto.getId());
        map.put("regexp", collect);
        List<Question> relatedQuestions = questionExtMapper.queryRelatedQuestions(map);
        return relatedQuestions;
    }

    public Question queryQuestionById(Long outerId) {
        Question question = questionMapper.selectByPrimaryKey(outerId.intValue());
        return question;
    }
}
