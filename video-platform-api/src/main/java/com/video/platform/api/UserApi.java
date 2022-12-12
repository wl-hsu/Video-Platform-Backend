package com.video.platform.api;

import com.video.platform.domain.JsonResponse;
import com.video.platform.domain.User;
import com.video.platform.service.UserService;
import com.video.platform.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserApi {


    @Autowired
    private UserService userService;


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
    public JsonResponse<String> login (@RequestBody User user) {
        String token = userService.login(user);
        return  new JsonResponse<>(token);
    }
}
