package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

class WorldRenderer {
    float cameraViewHeightMin = 20f;
    float cameraViewHeightMax = 100f;
    float cameraViewGain = 1f;
    float cameraViewHeight = cameraViewHeightMin;
    float cameraViewAspectRatio = 16/9f;
    float cameraViewHeightGainP = 1e-2f;
    float cameraViewHeightGainI = 5e-5f;
    float cameraViewHeightErrorIntegral = 0f;

    float cameraPositionGainP = 5e-2f;
    float cameraPositionGainI = 1e-4f;
    Vector3 cameraPositionError = new Vector3();
    Vector3 cameraPositionErrorIntegral = new Vector3();

    String message = "Please install a controller";

    World world;
    OrthographicCamera cam;
    SpriteBatch batch;

    Texture texture;
    BitmapFont font;
    GlyphLayout glyphLayout;

    public WorldRenderer(SpriteBatch batch, World world) {
        this.world = world;
        this.cam = new OrthographicCamera(cameraViewHeight * cameraViewAspectRatio, cameraViewHeight);
        this.cam.position.set(world.player.position.x, world.player.position.y, 0);
        this.batch = batch;

//        font = new BitmapFont();
//        font.setColor(Color.WHITE);
//        glyphLayout = new GlyphLayout();
    }

    void dispose() {
        texture.dispose();
        batch.dispose();
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
        batch.setProjectionMatrix(cam.combined);

        // Draw world components
        batch.begin();
        world.player.render(batch);
        for (Box block : world.blocks) {
            block.render(batch);
        }
        batch.end();
    }

    private void renderText() {
        glyphLayout.setText(font, message);
        font.draw(batch, glyphLayout, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }
}
