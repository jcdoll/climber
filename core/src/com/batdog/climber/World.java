package com.batdog.climber;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

class World {
    Player player;
    public final List<Boundary> boundaries;

    float epsilon = 0.01f;
    float gravity = 20f; // m/s^2
    Vector2 gravityDir = new Vector2();

    Climber controller;

    public World() {
        gravityDir.x = 0f;
        gravityDir.y = -1f;

        player = new Player(this);

        player.position.x = 10f;
        player.position.y = 5f;
        player.velocity.x = player.velocity.y = 0f;

        // Default jump direction: opposed to gravity
        player.jumpDir = gravityDir.cpy().scl(-1f);

        this.boundaries = new ArrayList<Boundary>();
        generateWorld();
    }

    private void generateWorld () {
        Boundary leftWall = new Boundary(this, new Vector2(0f, 0f), new Vector2(1f, 0f));
        Boundary rightWall = new Boundary(this, new Vector2(20f, 0f), new Vector2(-1f, 0f)); // rightWall collision is screwed up
        Boundary floor = new Boundary(this, new Vector2(0f, 0f), new Vector2(0f, 1f));
        boundaries.add(leftWall);
        boundaries.add(rightWall);
        boundaries.add(floor);
    }

    void update (float dt) {
        player.calculateForces(dt);
        checkPlayerCollisions();
        player.updateState(dt);
    }

    private void checkPlayerCollisions() {

        // Default: player is in mid-air
        player.surfaceContact = false;
        player.jump = true;

        // Check all boundaries in the world (any orientation)
        for (Boundary boundary : boundaries) {

            // Check position of a point, u1 = (x1, y1), with respect to a line
            // Consider a line that passes through u0 = (x0, y0) with normal vector n = (n1, n2)
            // If dot(u1 - u0, n) > 0 then the point is inside the boundary
            float distanceFromBoundary = new Vector2(player.position).sub(boundary.point).dot(boundary.normal) - player.PLAYER_HEIGHT / 2;

            // Calculate the velocity with respect to the surface similarly (< 0 means approaching surface)
            float normalVelocity = player.velocity.dot(boundary.normal);

            // If the player is approaching the surface and is at / beyond it, push the player back
            if (distanceFromBoundary <= 0 && normalVelocity < 0) {

                // Calculate player velocity in the basis defined by the boundary
                // Subtract the normal velocity component from the player velocity
                // Push the player back to the boundary
                player.velocity.sub(new Vector2(boundary.normal).scl(normalVelocity));
                player.position.sub(new Vector2(boundary.normal).scl(distanceFromBoundary));
            }

            // Handle close proximity to surfaces nicely (e.g. rapid wall jumping)
            // Update jump direction to have a component normal to boundary
            if (distanceFromBoundary < epsilon) {
                player.surfaceContact = true;
                player.jump = false;
                player.jumpTime = 0f;
                player.jumpDir = gravityDir.cpy().scl(-1.5f).add(boundary.normal).nor();
            }
        }
    }
}
