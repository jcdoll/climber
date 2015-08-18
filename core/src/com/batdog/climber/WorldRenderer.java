package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

class WorldRenderer {
    // TODO: Texture vs. sprite (see http://www.gamefromscratch.com/post/2014/09/22/LibGDX-Tutorial-Part-14-Gamepad-support.aspx)

    // Matches aspect ratio of window
    float aspectRatio = 16/9f;
    float viewHeight = 21f;
    float viewWidth = viewHeight * aspectRatio;

    String message = "Please install a controller";

    World world;
    OrthographicCamera cam;
    SpriteBatch batch;

    Texture texture;
    BitmapFont font;
    GlyphLayout glyphLayout;

    public WorldRenderer(SpriteBatch batch, World world) {
        this.world = world;
        this.cam = new OrthographicCamera(viewWidth, viewHeight);
        this.cam.position.set(world.player.position.x, world.player.position.y, 0);
        this.batch = batch;

//        font = new BitmapFont();
//        font.setColor(Color.WHITE);
//        glyphLayout = new GlyphLayout();
    }

//    public void resize (int width, int height) {
//        cam.setToOrtho(false, VIRTUAL_HEIGHT * width / (float) height, VIRTUAL_HEIGHT);
//        batch.setProjectionMatrix(cam.combined);
//    }

    void dispose() {
        texture.dispose();
        batch.dispose();
    }

    void render () {
        // TODO: Filtered updating of camera position to track player
//        cam.position.set(world.player.position.x, world.player.position.y, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        // TODO: Add HUD / text
        renderPlayer();
        renderBoundaries();

        for (Box block : world.blocks) {
            block.render(batch);
        }

        for (Platform platform : world.platforms) {
            platform.render(batch);
        }

        batch.end();
    }

    private void renderText() {
        glyphLayout.setText(font, message);
        font.draw(batch, glyphLayout, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    private void renderPlayer() {
        world.player.render(batch);
    }

    private void renderBoundaries() {
        // TODO: Render boundaries using a tileset / not perfectly uniform color

        for (Boundary boundary : world.boundaries) {
            float boundaryLength = 100f;
            float boundaryDepth = 100f;
            float x_dot = new Vector2(0f, 1f).dot(boundary.normal);
            float y_dot = new Vector2(1f, 0f).dot(boundary.normal);

            float width = Math.abs(x_dot) * boundaryLength + boundaryDepth;
            float height = Math.abs(y_dot) * boundaryLength + boundaryDepth;
            float x = boundary.point.x - width / 2 - y_dot * boundaryDepth / 2;
            float y = boundary.point.y - height / 2 - x_dot * boundaryDepth / 2;

            batch.draw(Assets.boundary, x, y, width, height);
//            batch.draw(Assets.boundary, x, y, x, y, width, height,
//                    1f, 1f, boundary.normal.angle() + 90f, 1, 1, 1, 1, false, false);
        }
    }

    private void renderPlatforms() {

    }
}
