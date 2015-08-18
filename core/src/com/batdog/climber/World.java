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
    float epsilon = 0.1f;
    float gravity = 30f; // m/s^2
    Vector2 gravityDir = new Vector2(0f, -1f);

    Climber controller;

    public World() {
        // Create player
        player = new Player(this);
        player.setPosition(10f, 14f);
        player.setVelocity(0f, 0f);
        player.setExtents(PLAYER_WIDTH, PLAYER_HEIGHT);
        player.setTexture(Assets.player);
        player.setMass(PLAYER_MASS);
        player.jumpDir = gravityDir.cpy().scl(-1f);

        generateWorld();
    }

    private void generateWorld () {

        // Generate static boundaries
        Boundary leftWall = new Boundary(this, new Vector2(0f, 0f), new Vector2(1f, 0f));
        Boundary rightWall = new Boundary(this, new Vector2(20f, 0f), new Vector2(-1f, 0f));
        Boundary floor = new Boundary(this, new Vector2(0f, 0f), new Vector2(0f, 1f));
        boundaries.add(leftWall);
        boundaries.add(rightWall);
        boundaries.add(floor);

        // Create normal blocks
        Box testBlock = new Box(1f, 2f, 18f, 2f);
        testBlock.setTexture(Assets.boundary);
        blocks.add(testBlock);

        testBlock = new Box(12f, 8f, 2f, 10f);
        testBlock.setTexture(Assets.boundary);
        blocks.add(testBlock);


//        // Generate a few random platforms
//        Rectangle first = new Rectangle(5f, 5f, 1f, 1f);
//        blocks.add(first);
    }

    void update (float dt) {
        player.updateState(dt);
        checkPlayerCollisions();
    }

    private void checkPlayerCollisions() {

        // Default: player is in mid-air
        player.surfaceContact = false;
        player.jump = true;

        // Check all blocks
        Vector2 positionCorrection;
        float normalVelocity;
        for (Box block : blocks) {
            positionCorrection = player.penetrationVector(block);
            if (!positionCorrection.isZero()) {
                player.position.add(positionCorrection);
                normalVelocity = Math.abs(player.velocity.dot(positionCorrection.cpy().nor()));
                player.velocity.add(new Vector2(positionCorrection).nor().scl(normalVelocity));

                player.floorContact = Math.abs(positionCorrection.dot(gravityDir)) > 0.5;
                player.surfaceContact = !player.floorContact;
                player.jump = false;
                player.jumpDir = gravityDir.cpy().scl(-1f).add(positionCorrection.cpy().nor()).nor();
            }
        }

//        // Check all boundaries in the world (any orientation)
//        for (Boundary boundary : boundaries) {
//
//            // Check position of a point, u1 = (x1, y1), with respect to a line
//            // Consider a line that passes through u0 = (x0, y0) with normal vector n = (n1, n2)
//            // If dot(u1 - u0, n) > 0 then the point is inside the boundary
//            // TODO: Generalize to work with non-square boxes
//            float distanceFromBoundary = new Vector2(player.position).sub(boundary.point).dot(boundary.normal) - player.getHeight() / 2;
//
//            // Calculate the velocity with respect to the surface similarly (< 0 means approaching surface)
//            float normalVelocity = player.velocity.dot(boundary.normal);
//
//            // If the player is approaching the surface and is at / beyond it, push the player back
//            if (distanceFromBoundary <= 0 && normalVelocity < 0) {
//
//                // Calculate player velocity in the basis defined by the boundary
//                // Subtract the normal velocity component from the player velocity
//                // Push the player back to the boundary
//                player.velocity.sub(new Vector2(boundary.normal).scl(normalVelocity));
//                player.position.sub(new Vector2(boundary.normal).scl(distanceFromBoundary));
//            }
//
//            // Handle close proximity to surfaces nicely (e.g. rapid wall jumping)
//            // Update jump direction to have a component normal to boundary
//            if (distanceFromBoundary < epsilon) {
//                player.floorContact = Math.abs(boundary.normal.dot(gravityDir)) > 0.5;
//                player.surfaceContact = !player.floorContact;
//
//                player.jump = false;
//                player.jumpDir = gravityDir.cpy().scl(-1f).add(boundary.normal).nor();
//            }
//        }
    }
}
