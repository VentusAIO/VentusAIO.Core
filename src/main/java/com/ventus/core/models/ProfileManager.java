package com.ventus.core.models;

import com.ventus.core.interfaces.IProfile;

import java.util.LinkedList;
import java.util.List;

public class ProfileManager {
    private final LinkedList<IProfile> profiles;

    public ProfileManager(List<IProfile> profiles){
        this.profiles = new LinkedList<>(profiles);
    }

    public IProfile getProfile(){
        if(profiles.isEmpty()) return null;
        return profiles.pop();
    }
}
