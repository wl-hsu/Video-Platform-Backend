package com.video.platform.dao;

import com.video.platform.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface VideoDao {

    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> videoTagList);

}
