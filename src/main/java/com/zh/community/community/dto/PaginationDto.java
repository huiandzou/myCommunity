package com.zh.community.community.dto;

import com.zh.community.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ${host}
 */
@Data
public class PaginationDto {
    private List<QuestionDto> listQuestion;
    private List<Integer> currentPageList = new ArrayList<>();
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private int totalSize;
    private int offSize;
    private boolean showFirstPage;
    private boolean showPreviousPage;
    private boolean showLastPage;
    private boolean showNextPage;

    public PaginationDto() {
        super();
    }

    public PaginationDto(List<QuestionDto> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public PaginationDto(int currentPage, int pageSize, int totalSize) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalSize = totalSize;
        if (currentPage > 0) {
            this.offSize = (currentPage - 1) * this.pageSize;
        }
    }

    public PaginationDto(int currentPage, int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        if (currentPage > 0) {
            this.offSize = (currentPage - 1) * this.pageSize;
        }
    }

    public void calculate() {
        this.totalPage = this.totalSize % this.pageSize == 0 ? this.totalSize / this.pageSize : this.totalSize / this.pageSize + 1;
        // 根据当前页对条数取模就行展示页码
        int initPage = 1;
        int mod = this.currentPage % this.pageSize;
        if (mod == 0) {
            initPage = this.currentPage - this.pageSize + 1;
        }else {
            initPage = this.currentPage - mod + 1;
        }
        for (int i = 0; i < this.pageSize; i++) {
            if((initPage+i)>this.totalPage){
                break;
            }
            this.currentPageList.add(initPage+i);
        }
        this.showFirstPage = true;
        this.showPreviousPage = true;
        this.showLastPage = true;
        this.showNextPage = true;
        if (this.currentPage == 1) {
            this.showFirstPage = false;
            this.showPreviousPage = false;
        }
        if (this.currentPage == this.totalPage) {
            this.showLastPage = false;
            this.showNextPage = false;
        }
        if (this.totalPage <= 5) {
            this.showFirstPage = false;
            this.showPreviousPage = false;
            this.showLastPage = false;
            this.showNextPage = false;
        }
        if (this.currentPageList.contains(1)) {
            this.showFirstPage = false;
        }
        if (this.currentPageList.contains(this.totalPage)) {
            this.showLastPage = false;
        }
    }
}
