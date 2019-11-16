package com.sonphan.mathmaniac.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

@Entity(indexes = {@Index(value = "fbId", unique = true)})
public class FacebookFriendEntity {
    public long fbId;
    public String displayName;
    public String avatarUrl;
    @Keep
    public FacebookFriendEntity(long fbId, String displayName, String avatarUrl) {
        this.fbId = fbId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }
    @Keep
    public FacebookFriendEntity() {
    }
    @Override
    public String toString() {
        return "FacebookFriendEntity: " + fbId + ", \n" + displayName + ", \n" + avatarUrl;
    }
    public long getFbId() {
        return this.fbId;
    }
    public void setFbId(long fbId) {
        this.fbId = fbId;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}