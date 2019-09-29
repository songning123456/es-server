package com.es.elasticsearch.service;

import com.es.elasticsearch.dto.BlogDTO;
import com.es.elasticsearch.dto.CommonDTO;
import com.es.elasticsearch.vo.BlogVO;
import com.es.elasticsearch.vo.CommonVO;

/**
 * @author sonning
 */
public interface BlogService {
    /**
     * 新增文章
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> saveArticle(CommonVO<BlogVO> commonVO);

    /**
     * 查询摘要等信息
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getAbstract(CommonVO<BlogVO> commonVO);

    /**
     * 获取文章内容
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getContent(CommonVO<BlogVO> commonVO);

    /**
     * 查询热门文章
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getHotArticle(CommonVO<BlogVO> commonVO);

    /**
     * 高亮搜索文章内容
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getHighlightArticle(CommonVO<BlogVO> commonVO);

    /**
     * 爬虫文章
     *
     * @param blog
     */
    <T> void saveArticle(T blog);
}
