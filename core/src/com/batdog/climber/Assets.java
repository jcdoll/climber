package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture player;
    public static Texture boundary;
    public static Sound jumpSound;
    public static Sound slideSound;
    public static Music backgroundMusic;
    public static boolean slideSoundIsPlaying;

     public static void load () {
         player = loadTexture("player.png");
         player.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
         boundary = loadTexture("boundary.png");
         jumpSound = loadSound("jump.wav");
         slideSound = loadSound("slide.wav");
         backgroundMusic = loadMusic("ascension.mp3");
     }

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static Sound loadSound (String file) {
        return Gdx.audio.newSound(Gdx.files.internal(file));
    }

    public static Music loadMusic (String file) {
        return Gdx.audio.newMusic(Gdx.files.internal(file));
    }

    public static void playSound (Sound sound) {
        sound.play(1.0f);
    }

    public static void playSound (Sound sound, float volume) {
        sound.play(volume);
    }

    public static void startSoundLoop (Sound sound) {
        if (!slideSoundIsPlaying) {
            long id = sound.play(1.0f);
            sound.setLooping(id, true);
            slideSoundIsPlaying = true;
        }
    }

    public static void stopSoundLoop (Sound sound) {
        sound.stop();
        slideSoundIsPlaying = false;
    }

    public static void startMusic (Music music) {
        music.play();
        music.setLooping(true);
    }

    public static void dispose() {
        player.dispose();
        boundary.dispose();
        jumpSound.dispose();
        slideSound.dispose();
        backgroundMusic.dispose();
    }
}
