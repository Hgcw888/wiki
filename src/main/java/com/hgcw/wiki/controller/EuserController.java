package com.hgcw.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.hgcw.wiki.domin.Euser;
import com.hgcw.wiki.req.EuserLoginReq;
import com.hgcw.wiki.req.EuserQueryReq;
import com.hgcw.wiki.req.EuserResetPasswordReq;
import com.hgcw.wiki.req.EuserSaveReq;
import com.hgcw.wiki.resp.CommonResp;
import com.hgcw.wiki.resp.EuserLoginResp;
import com.hgcw.wiki.resp.EuserQueryResp;
import com.hgcw.wiki.resp.PageResp;
import com.hgcw.wiki.service.EbookService;
import com.hgcw.wiki.service.EuserService;
import com.hgcw.wiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hgcw
 * @date 2021/4/18 21:37
 */
@RestController
@RequestMapping("/euser")
@Slf4j
public class EuserController {
    private final static String TOKEN_SUECCSS = "token";
    @Autowired
    private EuserService euserService;
    /**
     * Redis内置类RedisTemplate,StringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 写话算法实体类SnowFlake生成token
     */
    @Autowired
    private SnowFlake snowFlake;

    @GetMapping("/selectList")
    public CommonResp selectList() {
        CommonResp<List<Euser>> resp = new CommonResp<>();
        List<Euser> eusers = euserService.selectList();
        resp.setContent(eusers);
        return resp;
    }

    /**
     * 查询列表
     *
     * @param req
     * @return
     * @Valid开启参数校验规则
     */
    @GetMapping("/dimSelect")
    public CommonResp dimSelect(@Valid EuserQueryReq req) {
        PageResp<EuserQueryResp> eusers = euserService.dimSelect(req);
        CommonResp<PageResp<EuserQueryResp>> objectCommonResp = new CommonResp<>();
        objectCommonResp.setContent(eusers);
        return objectCommonResp;
    }

    /**
     * 删除电子书
     *
     * @PathVariable映射{id}到Long id
     */
    @DeleteMapping("/delectEuser/{id}")
    public CommonResp delectEuser(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp<>();
        euserService.delectEuser(id);
        return objectCommonResp;
    }

    /**
     * 编辑电子书
     *
     * @Valid 实体类设定的字段是否为空
     */
    @PostMapping("/updateEuser")
    public CommonResp updateEuser(@Valid @RequestBody EuserSaveReq euserSaveReq) {
        //传入密码加密
        euserSaveReq.setPassword(DigestUtils.md5DigestAsHex(euserSaveReq.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        euserService.updateEuser(euserSaveReq);
        return resp;
    }

    /**
     * 修改密码接口
     */
    @PostMapping("/updatepassword")
    public CommonResp updatepassword(@Valid @RequestBody EuserResetPasswordReq euserResetPasswordReq) {
        euserResetPasswordReq.setPassword(DigestUtils.md5DigestAsHex(euserResetPasswordReq.getPassword().getBytes()));
        CommonResp objectCommonResp = new CommonResp<>();
        euserService.updatepassword(euserResetPasswordReq);
        return objectCommonResp;
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody EuserLoginReq euserLoginReq) {
        //密码加密前后端都要
        euserLoginReq.setPassword(DigestUtils.md5DigestAsHex(euserLoginReq.getPassword().getBytes()));
        CommonResp<EuserLoginResp> objectCommonResp = new CommonResp<>();
        EuserLoginResp euserLoginResp = euserService.login(euserLoginReq);
        //登陆成功生成token,
        Long token = snowFlake.nextId();
        log.info("生成的token：{}", token);
        euserLoginResp.setToken(token.toString());
        //放入redis。set方法以token为key，euserLoginResp对象为数据，后面为时间
        stringRedisTemplate.opsForValue().set(String.format(TOKEN_SUECCSS, euserLoginResp.getId()), JSONObject.toJSONString(euserLoginResp), 3600 * 4, TimeUnit.SECONDS);
        objectCommonResp.setContent(euserLoginResp);
        return objectCommonResp;
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout/{token}")
    public CommonResp delectEuser(@PathVariable String token) {
        CommonResp objectCommonResp = new CommonResp<>();
        //直接调用stringRedisTemplate的delete方法删除token，从redis中清除
        stringRedisTemplate.delete(token);
        log.info("从redis中清除;{}",token);
        return objectCommonResp;
    }
}
