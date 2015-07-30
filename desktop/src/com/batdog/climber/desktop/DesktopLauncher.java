package com.batdog.climber.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.batdog.climber.Climber;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Climber";
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		new LwjglApplication(new Climber(), config);
	}
}
