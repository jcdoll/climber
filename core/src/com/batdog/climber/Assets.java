package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture player;
    public static Texture boundary;

     public static void load () {
        player = loadTexture("player.png");
        player.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        boundary = loadTexture("boundary.png");
     }

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void playSound (Sound sound) {
        sound.play(1);
    }
}
