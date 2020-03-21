package com.zh.community.community.dto;

import com.zh.community.community.model.Tag;
import lombok.Data;

import java.util.List;

/**
 * created by ${host}
 */
@Data
public class TagDto {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<Tag> childTags;
}
