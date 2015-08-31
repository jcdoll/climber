package com.batdog.climber;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

class World {
    Player player;
    public final List<Box> blocks = new ArrayList<Box>();

    final float PLAYER_HEIGHT = 1f;
    final float PLAYER_WIDTH = 1f;
    final float PLAYER_MASS = 1f;

    float dt_physics = 1/30f; // separate physics from rendering frame rate
    float epsilon = 0f;
    float gravity = 30f; // m/s^2
    Vector2 gravityDir = new Vector2(0f, -1f);

    Climber controller;

    public World() {
        // Create player
        player = new Player(this);
        player.setPosition(0f, 5f);
        player.setVelocity(0f, 0f);
        player.setExtents(PLAYER_WIDTH, PLAYER_HEIGHT);
        player.setTexture(Assets.player);
        player.setMass(PLAYER_MASS);
        player.jumpDir = gravityDir.cpy().scl(-1f);

        generateWorld();
    }

    private void generateWorld () {
        // Create normal blocks
        Box testBlock = new Box(-10f, -2f, 20f, 2f);
        testBlock.setTexture(Assets.boundary);
        blocks.add(testBlock);

        // TODO: Dynamically generate blocks above player
        // TODO: Death mechanic for falling and returning to previous spot
        // TODO: Other block shapes
        // TODO: Rule-based generation
        float currentMaxHeight = testBlock.getTop();
        for (int i = 0; i < 1000; i++) {
            float x = MathUtils.random(-10, 10);
            float y = currentMaxHeight + MathUtils.random(1, 5);
            float w = MathUtils.random(1, 3);
            float h = MathUtils.random(3, 10);

            testBlock = new Box(x, y, w, h);
            testBlock.setTexture(Assets.boundary);
            blocks.add(testBlock);
            currentMaxHeight = y + h;
        }
    }

    void update (float dt) {
        player.updateState(dt);
        checkPlayerCollisions(); // TODO: Do not check all objects every frame
    }

    private void checkPlayerCollisions() {

        // Default: player is in mid-air
        player.jump = true;
        player.surfaceContact = false;

        Vector2 penetrationVector;
        Vector2 normalVector;
        float normalVelocity;

        BoxIntersector intersector = new BoxIntersector(this, player);

        for (Box block : blocks) {
            intersector.setObstacle(block);
            if (intersector.intersecting()) {
                penetrationVector = intersector.penetrationVector;
                normalVector = intersector.normalVector;
                normalVelocity = Math.abs(player.velocity.dot(normalVector));

                player.position.add(penetrationVector);
                player.velocity.add(normalVector.cpy().scl(normalVelocity));

                player.floorContact = !gravityDir.isPerpendicular(intersector.normalVector);
                player.surfaceContact = true;
                player.jump = false;

                // Handle wall jumping
                player.jumpDir = gravityDir.cpy().scl(-1f).add(normalVector).nor();
            }
        }
    }
}
