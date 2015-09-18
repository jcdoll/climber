package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

class WorldRenderer {
    float cameraViewHeightMin = 20f;
    float cameraViewHeightMax = 60f;
    float cameraViewGain = 1f;
    float cameraViewHeight = cameraViewHeightMin;
    float cameraViewAspectRatio = 16/9f;
    float cameraViewHeightGainP = 2e-3f;
    float cameraViewHeightGainI = 1e-5f;
    float cameraViewHeightErrorIntegral = 0f;

    float cameraPositionGainP = 8e-2f;
    float cameraPositionGainI = 1e-4f;
    Vector3 cameraPositionError = new Vector3();
    Vector3 cameraPositionErrorIntegral = new Vector3();

    Climber game;
    World world;
    OrthographicCamera cam;
    OrthographicCamera hudCam;
    SpriteBatch batch;

    public final List<Plotter> plots = new ArrayList<>();

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    BitmapFont font;
    GlyphLayout glyphLayout;

    public WorldRenderer(Climber game, World world) {
        this.game = game;
        this.world = world;
        this.cam = new OrthographicCamera(cameraViewHeight * cameraViewAspectRatio, cameraViewHeight);
        this.cam.position.set(world.player.position.x, world.player.position.y, 0);
        this.hudCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch = game.batch;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        glyphLayout = new GlyphLayout();

        plots.add(new Plotter("fps", 50f, 50f,  200f, 100f, 240, new float[]{30, 70}, () -> (float) Gdx.graphics.getFramesPerSecond()));
        plots.add(new Plotter("v",   50f, 200f, 200f, 100f, 240, new float[]{0, 1000}, () -> world.player.velocity.len2()));
        plots.add(new Plotter("h",   50f, 350f, 200f, 100f, 240, new float[]{0, 10}, () -> world.player.getTop()));
    }

    // Sprite batch is disposed of in the main game class
    void dispose() {
        shapeRenderer.dispose();
    }

    void render () {
        // PI control loops for camera view height and position based upon user movement
        float newViewHeight = MathUtils.clamp(world.player.velocity.len()*cameraViewGain,
                                              cameraViewHeightMin, cameraViewHeightMax);
        float cameraViewHeightError = (newViewHeight - cam.viewportHeight);
        cameraViewHeightErrorIntegral += cameraViewHeightError;
        cam.viewportHeight += cameraViewHeightError * cameraViewHeightGainP
                            + cameraViewHeightErrorIntegral * cameraViewHeightGainI;
        cam.viewportWidth = cam.viewportHeight * cameraViewAspectRatio;

        Vector2 targetPosition = world.player.position.cpy(); // TODO: Include player orientation and velocity
        cameraPositionError = new Vector3(targetPosition, 0).sub(cam.position);
        cameraPositionErrorIntegral.add(cameraPositionError);
        Vector3 positionFeedback = cameraPositionError.cpy().scl(cameraPositionGainP)
                                        .add(cameraPositionErrorIntegral.cpy().scl(cameraPositionGainI));
        cam.position.add(positionFeedback);
        cam.update();

        // Draw world components
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        world.player.render(batch);
        for (Box block : world.blocks) {
            block.render(batch);
        }
        batch.end();

        // Draw HUD
        if (game.debugMode) {
            batch.setProjectionMatrix(hudCam.combined);
            shapeRenderer.setProjectionMatrix(hudCam.combined);
            for (Plotter plot : plots) {
                plot.render(batch, shapeRenderer);
            }
        }
    }
}
