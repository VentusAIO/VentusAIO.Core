package com.ventus.core.profile;

import java.util.LinkedList;

public class ProfileGroup {
    private volatile LinkedList<Profile> profileList = new LinkedList<>();

    public void setProfileList(LinkedList<Profile> profileList) {
        this.profileList = profileList;
    }

    public synchronized Profile getProfile() {
        return profileList.pop();
    }

    public boolean isEmpty() {
        return profileList.isEmpty();
    }
}
