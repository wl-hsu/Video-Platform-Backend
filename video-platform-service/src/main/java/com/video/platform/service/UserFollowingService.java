package com.video.platform.service;


import com.video.platform.dao.FollowingGroupDao;
import com.video.platform.domain.FollowingGroup;
import com.video.platform.dao.UserFollowingDao;
import com.video.platform.domain.User;
import com.video.platform.domain.UserFollowing;
import com.video.platform.domain.constant.UserConstant;
import com.video.platform.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;
    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.DEFAULT_USER_FOLLOWING_TYPE);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            FollowingGroup followingGroup  = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                throw new ConditionException("group does not exist.");
            }
        }
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if(user == null) {
            throw  new ConditionException("The following user does not exist.");
        }

        userFollowingDao.deleteUserFollowing(userFollowing.getId(), followingId);
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }
}
