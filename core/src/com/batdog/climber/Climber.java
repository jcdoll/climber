package com.batdog.climber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Climber extends Game {

	public SpriteBatch batcher;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		batcher = new SpriteBatch();
		Assets.load();
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	};
}
