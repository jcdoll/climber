package com.batdog.climber;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

class World {
    Player player;
    public final List<Boundary> boundaries;

    float timeScale = 1;
    float gravity = 9.81f; // m/s^2

    Climber controller;

    public World() {
        player = new Player(this);

        player.position.x = 10f;
        player.position.y = 10f;
        player.velocity.x = player.velocity.y = 0;

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
        checkCollisions();
        player.updateState(dt);
        // TODO: Check game over
    }

    private void checkCollisions () {
        // Check all boundaries in the world (any orientation)
        int len = boundaries.size();
        for (int i = 0; i < len; i++) {
            Boundary boundary = boundaries.get(i);
            float distanceFromBoundary = new Vector2(player.position).sub(boundary.point).dot(boundary.normal) - player.PLAYER_HEIGHT / 2;

            // Check position of a point, u1 = (x1, y1), with respect to a line
            // Consider a line that passes through u0 = (x0, y0) with normal vector n = (n1, n2)
            // If dot(u1 - u0, n) > 0 then the point is inside the boundary
            if (distanceFromBoundary < 0) {

                // Calculate player velocity in the basis defined by the boundary
                float normalVelocity = player.velocity.dot(boundary.normal);

                // Subtract the normal velocity component from the player (i.e. normal velocity = 0)
                player.velocity.sub(new Vector2(boundary.normal).scl(normalVelocity));

                // Push the player back to the boundary
                player.position.sub(new Vector2(boundary.normal).scl(distanceFromBoundary));
           }
        }
    }

    float getTimeScale() {
        return timeScale;
    }
}
