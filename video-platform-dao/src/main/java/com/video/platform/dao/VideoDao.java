package com.video.platform.dao;

import com.video.platform.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface VideoDao {

    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> videoTagList);

    Integer pageCountVideos(Map<String, Object> params);

    List<Video> pageListVideos(Map<String, Object> params);


}
