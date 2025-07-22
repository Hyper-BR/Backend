package br.com.hyper.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseUrls {
    public static final String BASE_URL = System.getProperty("user.dir");
    public static final String STORAGE = "storage";
    public static final String AVATAR_URL = "/assets/defaults/avatar.png";
    public static final String PLAYLIST_URL = "/assets/defaults/playlist.png";
}
