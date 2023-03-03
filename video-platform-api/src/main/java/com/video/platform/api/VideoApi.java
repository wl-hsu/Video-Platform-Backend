package com.video.platform.api;


import com.video.platform.api.support.UserSupport;
import com.video.platform.domain.JsonResponse;
import com.video.platform.domain.Video;
import com.video.platform.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class VideoApi {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserSupport userSupport;

    /**
     * video publish
     */
    @PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideos(video);
        return JsonResponse.success();
    }

}
