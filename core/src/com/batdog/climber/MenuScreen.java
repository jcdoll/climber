package com.batdog.climber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends ScreenAdapter {
    Climber game;
    OrthographicCamera camera;
    float VIEW_HEIGHT;
    float cameraViewAspectRatio = 16/9f;
    final List<MenuOption> menuOptions = new ArrayList<>();
    int selectedIndex;

    // Define inner class for menu options
    class MenuOption {
        String text;
        Runnable callback;

        public MenuOption(String text, Runnable callback) {
            this.text = text;
            this.callback = callback;
        }

        public void execute() {
            callback.run();
        }
    }

    public MenuScreen (Climber game) {
        this.game = game;
        VIEW_HEIGHT = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(VIEW_HEIGHT*cameraViewAspectRatio, VIEW_HEIGHT);
        camera.position.set(0, 0, 0);

        selectedIndex = 0;
        menuOptions.add(new MenuOption("Start", () -> game.setScreen(game.gameScreen)));
        menuOptions.add(new MenuOption("Quit", () -> Gdx.app.exit()));
    }

    @Override
    public void render (float dt) {
        update();
        draw();
    }

    public void update() {
        if (game.hasControllers) {
            PovDirection pov = game.controller.getPov(XBox360Pad.POV);
            if (pov == XBox360Pad.BUTTON_DPAD_UP)
                selectedIndex += (selectedIndex > 0) ? -1 : 0;
            if (pov == XBox360Pad.BUTTON_DPAD_DOWN)
                selectedIndex += (selectedIndex < menuOptions.size() - 1) ? 1 : 0;
            if (game.controller.getButton(XBox360Pad.BUTTON_A))
                menuOptions.get(selectedIndex).execute();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.W))
                selectedIndex += (selectedIndex > 0) ? -1 : 0;
            if (Gdx.input.isKeyPressed(Input.Keys.A))
                selectedIndex += (selectedIndex < menuOptions.size() - 1) ? 1 : 0;
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                menuOptions.get(selectedIndex).execute();
        }
    }

    public void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        float x0 = 0f;
        float y0 = 0f;
        float padding = 5f;

        game.batch.begin();

        game.font.setColor(Color.WHITE);
        game.glyphLayout.setText(game.font, "Climber");
        game.font.draw(game.batch, game.glyphLayout, x0 - game.glyphLayout.width / 2, y0 + game.glyphLayout.height / 2);
        y0 -= game.glyphLayout.height + 10*padding;

        for (int i = 0; i < menuOptions.size(); i++) {
            if (selectedIndex == i) {
                game.font.setColor(Color.WHITE);
            } else {
                game.font.setColor(Color.GRAY);
            }
            game.glyphLayout.setText(game.font, menuOptions.get(i).text);

            game.font.draw(game.batch, game.glyphLayout, x0 - game.glyphLayout.width / 2, y0 + game.glyphLayout.height / 2);
            y0 -= game.glyphLayout.height + padding;
        }

        game.batch.end();
    }

    public void dispose() {
    }
}
