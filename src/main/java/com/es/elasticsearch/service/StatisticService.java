package com.es.elasticsearch.service;

import com.es.elasticsearch.dto.CommonDTO;
import com.es.elasticsearch.dto.StatisticDTO;
import com.es.elasticsearch.vo.CommonVO;
import com.es.elasticsearch.vo.StatisticVO;

/**
 * @author songning
 * @date 2019/9/29
 * description
 */
public interface StatisticService {
    /**
     * 获取统计结果
     *
     * @param commonVO
     * @return
     */
    CommonDTO<StatisticDTO> getStatisticResult(CommonVO<StatisticVO> commonVO);
}
