package com.video.platform.service;


import com.video.platform.dao.FollowingGroupDao;
import com.video.platform.domain.FollowingGroup;
import com.video.platform.dao.UserFollowingDao;
import com.video.platform.domain.User;
import com.video.platform.domain.UserFollowing;
import com.video.platform.domain.UserInfo;
import com.video.platform.domain.constant.UserConstant;
import com.video.platform.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    // get user following list
    // according to following user id to search user info
    // add the following user into following classification list
    public List<FollowingGroup> getUserFollowings(Long userId) {
        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }
        for(UserFollowing userFollowing : list){
            for(UserInfo userInfo : userInfoList) {
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userFollowing.setUserInfo(userInfo);
                }
            }

        }
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup group : groupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if (group.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;
    }

}
