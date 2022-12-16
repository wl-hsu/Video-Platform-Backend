package com.video.platform.api;

import com.video.platform.domain.JsonResponse;
import com.video.platform.domain.User;
import com.video.platform.domain.UserInfo;
import com.video.platform.service.UserService;
import com.video.platform.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.video.platform.api.support.UserSupport;


@RestController
public class UserApi {


    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }


    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/user-tokens")
    public JsonResponse<String> login (@RequestBody User user) throws Exception{
        String token = userService.login(user);
        return  new JsonResponse<>(token);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    @PutMapping("/user-infos")
    public JsonResponse<String> updateUsersInfos(@RequestBody UserInfo userInfo) {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();

    }
}
