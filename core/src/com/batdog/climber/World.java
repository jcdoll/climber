package com.batdog.climber;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

class World {
    Player player;
    public final List<Boundary> boundaries = new ArrayList<Boundary>();;
    public final List<Platform> platforms = new ArrayList<Platform>();;
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
        player.setPosition(15f, 8f);
        player.setVelocity(0f, 0f);
        player.setExtents(PLAYER_WIDTH, PLAYER_HEIGHT);
        player.setTexture(Assets.player);
        player.setMass(PLAYER_MASS);
        player.jumpDir = gravityDir.cpy().scl(-1f);

        generateWorld();
    }

    private void generateWorld () {
        // Create normal blocks
        Box testBlock = new Box(1f, 2f, 18f, 2f);
        testBlock.setTexture(Assets.boundary);
        blocks.add(testBlock);

        testBlock = new Box(12f, 8f, 2f, 10f);
        testBlock.setTexture(Assets.boundary);
        blocks.add(testBlock);

        testBlock = new Box(4f, 12f, 1f, 12f);
        testBlock.setTexture(Assets.boundary);
        blocks.add(testBlock);
    }

    void update (float dt) {
        player.updateState(dt);
        checkPlayerCollisions();
    }

    private void checkPlayerCollisions() {

        // Default: player is in mid-air
        player.jump = true;
        player.surfaceContact = false;

        // Check all blocks
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

                player.surfaceContact = true;
                player.jump = false;

                // Handle wall jumping
                player.jumpDir = gravityDir.cpy().scl(-1f).add(normalVector).nor();
            }
        }
    }
}
