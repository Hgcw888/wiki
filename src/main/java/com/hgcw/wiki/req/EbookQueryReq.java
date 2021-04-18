package com.hgcw.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hgcw
 * @date 2021/4/18 22:11
 *查询端口请求类
 */
@Data
public class EbookQueryReq extends PageReq {
    private Long id;
    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;
}
