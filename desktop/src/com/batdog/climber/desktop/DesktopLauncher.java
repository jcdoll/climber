package com.batdog.climber.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.batdog.climber.Climber;

public class DesktopLauncher {
    private static final short DEBUG_SIZE = 0;

    private static final short FULLSCREEN = 0;
    private static final short BIG_WINDOW = 1;
    private static final short SMALL_PHONE = 2;
    private static final short NEXUS_ONE = 3;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Climber";

        if (DEBUG_SIZE == FULLSCREEN) {
            config.width = 1920;
            config.height = 1080;
            config.fullscreen = true;
        } else if (DEBUG_SIZE == BIG_WINDOW) {
            config.width = 1900;
            config.height = 1020;
            config.fullscreen = false;
        } else if (DEBUG_SIZE == SMALL_PHONE) {
            config.width = 480;
            config.height = 320;
            config.fullscreen = false;
        } else if (DEBUG_SIZE == NEXUS_ONE) {
            config.width = 800;
            config.height = 480;
            config.fullscreen = false;
        }
        config.resizable = false;
        config.vSyncEnabled = true;
        config.audioDeviceBufferCount = 16;

        // TODO: Rework physics to be independent of frame rate
		config.foregroundFPS = 60;
		config.backgroundFPS = 15;
		new LwjglApplication(new Climber(), config);
	}
}
