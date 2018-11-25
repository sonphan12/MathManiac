package com.sonphan.user.mathmaniac.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {@Index(value = "fbId", unique = true)})
public class Player {
    public long fbId;
    public String displayName;
    public String avatarUrl;
    public int highScore;
    @Generated(hash = 1164538032)
    public Player(long fbId, String displayName, String avatarUrl, int highScore) {
        this.fbId = fbId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.highScore = highScore;
    }
    @Generated(hash = 30709322)
    public Player() {
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
    public int getHighScore() {
        return this.highScore;
    }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public String toString() {
        return "Player: " + fbId + ", \n" + displayName + ", \n" + avatarUrl + ", \n" + highScore;
    }
}