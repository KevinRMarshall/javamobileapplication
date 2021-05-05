package ca.on.conestogac.kmarshalldlacelle.matchgame;

import android.media.MediaPlayer;
import java.util.HashMap;

//Allows us to add sounds with keys so we can access and play sounds anywhere
public class SoundHandler {

    HashMap<String, MediaPlayer> media;

    public SoundHandler() {
        media = new HashMap<String, MediaPlayer>();
    }

    public void Add(String key, MediaPlayer value) {
        media.put(key,value);
    }

    public MediaPlayer getMedia(String key) {
        return media.get(key);
    }
}
