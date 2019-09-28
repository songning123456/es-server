package com.es.elasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.es.elasticsearch.common.CommonConstant;
import com.es.elasticsearch.dto.BlogDTO;
import com.es.elasticsearch.dto.CommonDTO;
import com.es.elasticsearch.entity.Blog;
import com.es.elasticsearch.repository.BlogRepository;
import com.es.elasticsearch.service.BlogService;
import com.es.elasticsearch.util.ClassConvertUtil;
import com.es.elasticsearch.util.DateUtil;
import com.es.elasticsearch.vo.BlogVO;
import com.es.elasticsearch.vo.CommonVO;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private JestClient jestClient;

    @Override
    public CommonDTO<BlogDTO> saveArticle(CommonVO<BlogVO> commonVO) {
        String title = commonVO.getCondition().getTitle();
        String content = commonVO.getCondition().getContent();
        String summary = commonVO.getCondition().getSummary();
        String kinds = commonVO.getCondition().getKinds();
        Integer readTimes = commonVO.getCondition().getReadTimes();
        String author = commonVO.getCondition().getAuthor();
        Blog blog = Blog.builder().title(title).content(content).summary(summary).kinds(kinds).readTimes(readTimes).author(author).updateTime(new Date()).build();
        blogRepository.save(blog);
        return new CommonDTO<>();
    }

    @Override
    public CommonDTO<BlogDTO> getAbstract(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        String kinds = commonVO.getCondition().getKinds();
        String[] includes = {"id", "title", "updateTime", "summary", "author", "kinds"};
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(includes, new String[]{});
        searchSourceBuilder.query(QueryBuilders.matchQuery("kinds", kinds));
        searchSourceBuilder.sort("updateTime", SortOrder.DESC);
        searchSourceBuilder.from(recordStartNo);
        searchSourceBuilder.size(pageRecordNum);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(CommonConstant.INDEX_NAME).addType(CommonConstant.TYPE_NAME).build();
        JestResult jestResult = null;
        try {
            jestResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<BlogDTO> esBlogDTOList = JSON.parseObject(JSON.toJSONString(jestResult.getValue("hits"))).getJSONArray("hits").stream().map(hit -> {
            BlogDTO item = new BlogDTO();
            JSONObject hitObject = JSON.parseObject(hit.toString());
            item.setId(hitObject.getString("_id"));
            item.setTitle(hitObject.getJSONObject("_source").getString("title"));
            item.setSummary(hitObject.getJSONObject("_source").getString("summary"));
            item.setUpdateTime(hitObject.getJSONObject("_source").getDate("updateTime"));
            item.setAuthor(hitObject.getJSONObject("_source").getString("author"));
            return item;
        }).collect(Collectors.toList());
        commonDTO.setData(esBlogDTOList);
        commonDTO.setTotal((long) esBlogDTOList.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getContent(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String id = commonVO.getCondition().getId();
        Optional<Blog> blog = blogRepository.findById(id);
        Integer readTimes = blog.get().getReadTimes();
        blog.get().setReadTimes(++readTimes);
        blogRepository.save(blog.get());
        BlogDTO esBlogDTO = new BlogDTO();
        ClassConvertUtil.populate(blog.get(), esBlogDTO);
        commonDTO.setData(Collections.singletonList(esBlogDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getHotArticle(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String kinds = commonVO.getCondition().getKinds();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        String[] includes = {"id", "title", "updateTime", "author", "kinds"};
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(includes, new String[]{});
        searchSourceBuilder.query(QueryBuilders.matchQuery("kinds", kinds));
        searchSourceBuilder.sort("readTimes", SortOrder.DESC);
        searchSourceBuilder.size(pageRecordNum);
        searchSourceBuilder.from(recordStartNo);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(CommonConstant.INDEX_NAME).addType(CommonConstant.TYPE_NAME).build();
        JestResult jestResult = null;
        try {
            jestResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<BlogDTO> esBlogDTOList = JSON.parseObject(JSON.toJSONString(jestResult.getValue("hits"))).getJSONArray("hits").stream().map(hit -> {
            BlogDTO item = new BlogDTO();
            JSONObject hitObject = JSON.parseObject(hit.toString());
            item.setId(hitObject.getString("_id"));
            item.setTitle(hitObject.getJSONObject("_source").getString("title"));
            item.setAuthor(hitObject.getJSONObject("_source").getString("author"));
            item.setUpdateTime(hitObject.getJSONObject("_source").getDate("updateTime"));
            item.setKinds(hitObject.getJSONObject("_source").getString("kinds"));
            return item;
        }).collect(Collectors.toList());
        commonDTO.setData(esBlogDTOList);
        commonDTO.setTotal((long) esBlogDTOList.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getHighlightArticle(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String[] includes = {"id", "title", "updateTime", "author"};
        String content = commonVO.getCondition().getContent();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(includes, new String[]{});
        searchSourceBuilder.query(QueryBuilders.matchQuery("content", content));
        searchSourceBuilder.sort("updateTime", SortOrder.DESC);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("content");
        highlightBuilder.preTags("<span style='color: #ffa500;font-weight: bold;font-size: 16px !important;'>").postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(CommonConstant.INDEX_NAME).addType(CommonConstant.TYPE_NAME).build();
        JestResult jestResult = null;
        try {
            jestResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<BlogDTO> blogDTOList = JSON.parseObject(JSON.toJSONString(jestResult.getValue("hits"))).getJSONArray("hits").stream().map(hit -> {
            BlogDTO item = new BlogDTO();
            JSONObject hitObject = JSON.parseObject(hit.toString());
            item.setId(hitObject.getString("_id"));
            item.setTitle(hitObject.getJSONObject("_source").getString("title"));
            item.setSearchResult(hitObject.getJSONObject("highlight").getJSONArray("content").toJavaList(String.class));
            item.setAuthor(hitObject.getJSONObject("_source").getString("author"));
            item.setUpdateTime(DateUtil.strToSqlDate(hitObject.getJSONObject("_source").getString("updateTime"), "yyyy-MM-dd HH:mm:ss"));
            return item;
        }).collect(Collectors.toList());
        commonDTO.setData(blogDTOList);
        commonDTO.setTotal((long) blogDTOList.size());
        return commonDTO;
    }

    @Override
    public <T> void saveArticle(T blog) {
        blogRepository.save((Blog) blog);
    }
}
