package com.video.platform.api;

import com.video.platform.api.support.UserSupport;
import com.video.platform.domain.JsonResponse;
import com.video.platform.domain.UserMoment;
import com.video.platform.domain.annotation.ApiLimitedRole;
import com.video.platform.domain.annotation.DataLimited;
import com.video.platform.domain.constant.AuthRoleConstant;
import com.video.platform.service.UserMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserMomentsApi {

    @Autowired
    private UserMomentsService userMomentsService;

    @Autowired
    private UserSupport userSupport;

    /**
     * add access permission for specific role
     * @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
     *
     * @DataLimited make the Lv1 user can only send the '0' data
     * It's a kind of data permission control example.
     * Please for free to customize it or remove it.
     */
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
    @DataLimited
    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentsService.addUserMoments(userMoment);
        return JsonResponse.success();
    }

    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment> list = userMomentsService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }

}
