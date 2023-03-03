package com.video.platform.service;

import com.video.platform.dao.VideoDao;
import com.video.platform.domain.PageResult;
import com.video.platform.domain.Video;
import com.video.platform.domain.VideoTag;
import com.video.platform.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VideoService {

    @Autowired
    private VideoDao videoDao;

    @Transactional
    public void addVideos(Video video) {
        Date now = new Date();
        video.setCreateTime(new Date());
        videoDao.addVideos(video);
        Long videoId = video.getId();
        List<VideoTag> tagList = video.getVideoTagList();
        tagList.forEach(item -> {
            item.setCreateTime(now);
            item.setVideoId(videoId);
        });
        videoDao.batchAddVideoTags(tagList);
    }

    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if(size == null || no == null){
            throw new ConditionException("parameter error！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", (no-1)*size);
        params.put("limit", size);
        params.put("area" , area);
        List<Video> list = new ArrayList<>();
        Integer total = videoDao.pageCountVideos(params);
        if(total > 0){
            list = videoDao.pageListVideos(params);
        }
        return new PageResult<>(total, list);
    }
}