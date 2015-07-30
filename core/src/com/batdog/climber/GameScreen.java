package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameScreen extends ScreenAdapter {
    final float VIEW_WIDTH = 150f;
    final float VIEW_HEIGHT = 100f;

    Climber game;
    World world;
    WorldRenderer renderer;
    Controller controller;
    OrthographicCamera guiCam;
    boolean hasControllers = true;

    final float MIN_DT = 1/30f;
    GlyphLayout glyphLayout = new GlyphLayout();

    public GameScreen (Climber game) {
        this.game = game;

        guiCam = new OrthographicCamera(VIEW_WIDTH, VIEW_HEIGHT);
        guiCam.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);
        world = new World();
        renderer = new WorldRenderer(game.batcher, world);

        // Setup controllers
        if (Controllers.getControllers().size == 0)
            hasControllers = false;
        else
            controller = Controllers.getControllers().first();
    }

    public void update (float dt) {
//        float dt = Math.min(Gdx.graphics.getDeltaTime(), MIN_DT) * world.getTimeScale();

        // Controls
        if(controller.getButton(XBox360Pad.BUTTON_X) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            world.player.run();

        PovDirection pov = controller.getPov(XBox360Pad.POV);
        if(pov == XBox360Pad.BUTTON_DPAD_LEFT || Gdx.input.isKeyPressed(Input.Keys.A))
            world.player.moveLeft();
        if(pov == XBox360Pad.BUTTON_DPAD_RIGHT || Gdx.input.isKeyPressed(Input.Keys.D))
            world.player.moveRight();

        if(controller.getButton(XBox360Pad.BUTTON_A) || Gdx.input.isKeyPressed(Input.Keys.SPACE))
            world.player.jump();
        else
            world.player.jumpHold = false; // Set to false after button release or upon hitting ground

        world.update(dt);
    }

    public void draw () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);

        game.batcher.begin();
        // Overlays
        game.batcher.end();
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }
}
