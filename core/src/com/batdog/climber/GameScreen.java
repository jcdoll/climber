package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen extends ScreenAdapter {
    final float VIEW_WIDTH = 150f;
    final float VIEW_HEIGHT = 100f;

    Climber game;
    World world;
    WorldRenderer renderer;
    OrthographicCamera guiCam;

    public GameScreen (Climber game) {
        this.game = game;

        guiCam = new OrthographicCamera(VIEW_WIDTH, VIEW_HEIGHT);
        guiCam.position.set(VIEW_WIDTH / 2, VIEW_HEIGHT / 2, 0);
        world = new World();
        renderer = new WorldRenderer(game, world);
    }

    public void update (float dt) {
        // Handle user input
        // TODO: Refactor to cleanup and eliminate potential double updates per frame
        if (game.hasControllers) {
            PovDirection pov = game.controller.getPov(XBox360Pad.POV);
            if (game.controller.getButton(XBox360Pad.BUTTON_X) || game.controller.getButton(XBox360Pad.BUTTON_RB))
                world.player.run();
            if (pov == XBox360Pad.BUTTON_DPAD_LEFT || pov == XBox360Pad.BUTTON_DPAD_UP_LEFT || pov == XBox360Pad.BUTTON_DPAD_DOWN_LEFT)
                world.player.moveLeft();
            if (pov == XBox360Pad.BUTTON_DPAD_RIGHT || pov == XBox360Pad.BUTTON_DPAD_UP_RIGHT || pov == XBox360Pad.BUTTON_DPAD_DOWN_RIGHT)
                world.player.moveRight();

            // Apply jumpSound and update player jumpHold state
            // TODO: Eliminate jumpHold
            if (game.controller.getButton(XBox360Pad.BUTTON_A)) {
                world.player.jump();
                world.player.jumpHold = true;
            } else {
                world.player.jumpHold = false;
            }

            // TODO: Transition to pause screen rather than main menu
            if (game.controller.getButton(XBox360Pad.BUTTON_START))
                game.setScreen(game.menuScreen);
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                world.player.run();
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                world.player.moveLeft();
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                world.player.moveRight();
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                world.player.jump();
                world.player.jumpHold = true;
            } else {
                world.player.jumpHold = false;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
                game.setScreen(game.menuScreen);
        }
        world.update(dt);
    }

    public void draw () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
    }

    @Override
    public void render (float dt) {
        update(dt);
        draw();
    }

    public void dispose() {
        renderer.dispose();
        world.dispose();
    }
}
