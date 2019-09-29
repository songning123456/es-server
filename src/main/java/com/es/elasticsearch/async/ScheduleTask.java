package com.es.elasticsearch.async;

import com.es.elasticsearch.entity.Blog;
import com.es.elasticsearch.repository.BlogRepository;
import com.es.elasticsearch.util.DateUtil;
import com.es.elasticsearch.util.RandomUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songning
 * @date 2019/9/29
 * description
 */
public class ScheduleTask {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 每天10，12，16点触发该事件
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void theftArticle() throws Exception {
        this.theftMeituan();
        this.theftBoke();
    }

    private void theftMeituan() throws Exception {
        String url = "https://tech.meituan.com/";
        List<String> authors = Arrays.asList("吃顿好的", "cest mom seul", "zhou", "重零开始");
        List<String> labels = Arrays.asList("WebSocket", "Vuex", "Chrome", "jQuery", "正则表达式", "HTTP", "MySQL", "ECMAScript 6", "Git", "HTML", "设计模式", "代码规范", "图片资源", "Linux", "机器学习", "Android", "iOS", "Java", "JavaScript");
        String random = RandomUtil.getRandom(2, 18);
        // 爬虫美团
        Document document = Jsoup.connect(url + "/page/" + random + ".html").get();
        List<String> articleUrls = document.getElementsByClass("post-title").stream().map(o -> o.getElementsByTag("a").get(0).attr("href")).collect(Collectors.toList());
        for (int j = 0; j < articleUrls.size(); j++) {
            Document doc = Jsoup.connect(articleUrls.get(Integer.parseInt(random))).get();
            String title = doc.getElementsByClass("post-title").get(0).getElementsByTag("a").html();
            String readTimes = RandomUtil.getRandom(1, 1000);
            String content = doc.getElementsByClass("post-content").html();
            String author = authors.get(Integer.parseInt(RandomUtil.getRandom(0, 3)));
            String kinds = labels.get(Integer.parseInt(RandomUtil.getRandom(0, labels.size() - 1)));
            Date updateTime = DateUtil.getBeforeByCurrentTime(Integer.parseInt(RandomUtil.getRandom(1, 23)));
            final String[] summary = new String[1];
            doc.getElementsByClass("post-content").get(0).getElementsByClass("content").get(0).getElementsByTag("p").forEach(item -> {
                if (!StringUtils.isEmpty(item.html()) && StringUtils.isEmpty(summary[0])) {
                    summary[0] = item.html();
                }
            });
            Blog blog = Blog.builder().title(title).content(content).summary(summary[0]).readTimes(Integer.parseInt(readTimes)).kinds(kinds).author(author).updateTime(updateTime).build();
            blogRepository.save(blog);
        }
    }

    private void theftBoke() throws Exception {
        String url = "https://www.boke.la/wenzhang/";
        List<String> authors = Arrays.asList("吃顿好的", "cest mom seul", "zhou", "重零开始");
        List<String> labels = Arrays.asList("WebSocket", "Vuex", "Chrome", "jQuery", "正则表达式", "HTTP", "MySQL", "ECMAScript 6", "Git", "HTML", "设计模式", "代码规范", "图片资源", "Linux", "机器学习", "Android", "iOS", "Java", "JavaScript");
        String random = RandomUtil.getRandom(2, 35);
        Document document = Jsoup.connect(url + random + "/").get();
        List<String> articleUrls = document.getElementsByClass("news").get(0).getElementsByTag("li").stream().map(o -> o.getElementsByTag("a").get(0).attr("href")).collect(Collectors.toList());
        for (String articleUrl : articleUrls) {
            Document doc = Jsoup.connect(articleUrl).get();
            String title = doc.getElementsByClass("zwtit2").get(0).getElementsByTag("h3").html();
            String readTimes = RandomUtil.getRandom(1, 1000);
            String kinds = labels.get(Integer.parseInt(RandomUtil.getRandom(0, labels.size() - 1)));
            final String[] summary = new String[1];
            doc.getElementsByClass("zhengwen").get(0).getElementsByTag("p").forEach(item -> {
                if (!StringUtils.isEmpty(item.html()) && StringUtils.isEmpty(summary[0])) {
                    summary[0] = item.html();
                }
            });
            String content = doc.getElementsByClass("zhengwen").get(0).getElementsByTag("p").html();
            String author = authors.get(Integer.parseInt(RandomUtil.getRandom(0, 3)));
            Date updateTime = DateUtil.getBeforeByCurrentTime(Integer.parseInt(RandomUtil.getRandom(1, 12)));
            Blog blog = Blog.builder().title(title).summary(summary[0]).content(content).readTimes(Integer.parseInt(readTimes)).kinds(kinds).author(author).updateTime(updateTime).build();
            blogRepository.save(blog);
        }
    }
}
