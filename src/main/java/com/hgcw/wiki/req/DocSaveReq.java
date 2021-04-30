package com.hgcw.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DocSaveReq {
    private Long id;
    @NotNull(message = "【电子书id】不能为空")
    private Long ebookId;
    @NotNull(message = "【父id】不能为空")
    private Long parent;
    @NotNull(message = "【名称】不能为空")
    private String name;
    @NotNull(message = "【顺序】不能为空")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

    @NotNull(message = "【内容】不能为空")
    private String content;

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", content='" + content + '\'' +
                '}';
    }
}