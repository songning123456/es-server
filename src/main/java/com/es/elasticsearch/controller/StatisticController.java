package com.es.elasticsearch.controller;

import com.es.elasticsearch.annotation.ControllerAspectAnnotation;
import com.es.elasticsearch.dto.CommonDTO;
import com.es.elasticsearch.dto.StatisticDTO;
import com.es.elasticsearch.service.StatisticService;
import com.es.elasticsearch.vo.CommonVO;
import com.es.elasticsearch.vo.StatisticVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songning
 * @date 2019/9/29
 * description
 */
@RestController
@RequestMapping("/es/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @RequestMapping("/get")
    @ControllerAspectAnnotation(description = "获取统计结果")
    public CommonDTO<StatisticDTO> statisticResult(@RequestBody CommonVO<StatisticVO> commonVO) {
        CommonDTO<StatisticDTO> commonDTO = statisticService.getStatisticResult(commonVO);
        return commonDTO;
    }
}
