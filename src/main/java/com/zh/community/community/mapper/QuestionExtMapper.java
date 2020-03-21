package com.zh.community.community.mapper;

import com.zh.community.community.model.Question;
import com.zh.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface QuestionExtMapper {
   List<Question> queryRelatedQuestions(Map<String, Object> objectMap);

}