package com.batdog.climber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Climber extends Game {

	SpriteBatch batch;
	GameScreen mainScreen;
    boolean debugMode = false;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		batch = new SpriteBatch();
		Assets.load();
		mainScreen = new GameScreen(this);
		setScreen(mainScreen);
	}

	@Override
	public void render () {
		super.render();
	};

	@Override
	public void dispose () {
		Assets.dispose();
		batch.dispose();
		mainScreen.dispose();
	}
}
