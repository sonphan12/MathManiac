package com.sonphan.mathmaniac.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {@Index(value = "fbId", unique = true)})
public class LocalFacebookFriend {
    public long fbId;
    public String displayName;
    public String avatarUrl;
    @Generated(hash = 1560582075)
    public LocalFacebookFriend(long fbId, String displayName, String avatarUrl) {
        this.fbId = fbId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
    }
    @Generated(hash = 1285053618)
    public LocalFacebookFriend() {
    }
    @Override
    public String toString() {
        return "LocalFacebookFriend: " + fbId + ", \n" + displayName + ", \n" + avatarUrl;
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