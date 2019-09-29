package com.es.elasticsearch.repository;

import com.es.elasticsearch.entity.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sonning
 */
@Repository
public interface BlogRepository extends ElasticsearchRepository<Blog, String> {

    List<Blog> findByKinds(String kinds, Pageable pageable);
}
