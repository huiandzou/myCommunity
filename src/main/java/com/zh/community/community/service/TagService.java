package com.zh.community.community.service;

import com.zh.community.community.dto.TagDto;
import com.zh.community.community.mapper.TagMapper;
import com.zh.community.community.model.Tag;
import com.zh.community.community.model.TagExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by ${host}
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;
    public List<TagDto> getParentTag(){
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andParentIdIsNull();
        List<Tag> tags = tagMapper.selectByExample(tagExample);
        List<Tag> tagList = tags.stream().sorted(Comparator.comparing(Tag::getId).reversed()).collect(Collectors.toList());
        List<TagDto> tagDtos = tagList.stream().map(tag -> {
            TagDto tagDto = new TagDto();
            BeanUtils.copyProperties(tag, tagDto);
            List<Tag> childTags = getChildTags(tag.getId());
            tagDto.setChildTags(childTags);
            return tagDto;
        }).collect(Collectors.toList());
        return tagDtos;
    }

    public List<Tag> getChildTags(Integer parentId){
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andParentIdEqualTo(parentId);
        List<Tag> tags = tagMapper.selectByExample(tagExample);
        return tags;
    }
}
