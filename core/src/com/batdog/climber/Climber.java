package com.batdog.climber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class Climber extends Game {

	SpriteBatch batch;
	GameScreen gameScreen;
    MenuScreen menuScreen;
    BitmapFont font;
    boolean debugMode = true;
    Controller controller;
    boolean hasControllers = false;
    GlyphLayout glyphLayout;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Assets.load();
		batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        // Setup controllers
        if (Controllers.getControllers().size > 0) {
            hasControllers = true;
            controller = Controllers.getControllers().first();
        }

        // Start playing music
        Assets.startMusic(Assets.backgroundMusic);

		gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);

		setScreen(menuScreen);
	}

	@Override
	public void render () {
		super.render();
	};

	@Override
	public void dispose () {
		Assets.dispose();
		batch.dispose();
        font.dispose();
		gameScreen.dispose();
        menuScreen.dispose();
	}
}
