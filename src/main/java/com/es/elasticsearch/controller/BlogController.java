package com.es.elasticsearch.controller;

import com.es.elasticsearch.annotation.ControllerAspectAnnotation;
import com.es.elasticsearch.dto.BlogDTO;
import com.es.elasticsearch.dto.CommonDTO;
import com.es.elasticsearch.service.BlogService;
import com.es.elasticsearch.vo.BlogVO;
import com.es.elasticsearch.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping("/insertArticle")
    @ControllerAspectAnnotation(description = "新增文章")
    public CommonDTO<BlogDTO> insertArticle(@RequestBody CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = blogService.saveArticle(commonVO);
        return commonDTO;
    }

    @PostMapping("/queryAbstract")
    @ControllerAspectAnnotation(description = "查询摘要等信息")
    public CommonDTO<BlogDTO> queryAbstract(@RequestBody CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = blogService.getAbstract(commonVO);
        return commonDTO;
    }

    @PostMapping("/queryContent")
    @ControllerAspectAnnotation(description = "查询文章内容")
    public CommonDTO<BlogDTO> queryContent(@RequestBody CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = blogService.getContent(commonVO);
        return commonDTO;
    }

    @PostMapping("/queryHotArticle")
    @ControllerAspectAnnotation(description = "查询热门文章")
    public CommonDTO<BlogDTO> queryHotArticle(@RequestBody CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = blogService.getHotArticle(commonVO);
        return commonDTO;
    }

    @PostMapping("/searchArticle")
    @ControllerAspectAnnotation(description = "高亮搜索文章")
    public CommonDTO<BlogDTO> queryHighlightArticle(@RequestBody CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = blogService.getHighlightArticle(commonVO);
        return commonDTO;
    }
}
