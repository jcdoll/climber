package com.batdog.climber;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player {
    final float PLAYER_HEIGHT = 1f;
    final float PLAYER_WIDTH = 1f;

    World world;

    Vector2 position = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 force = new Vector2();

    boolean jump = false;
    boolean jumpHold = false;
    boolean run = false;

    float jumpForce = 200f; // 1 N
    float walkForce = 60f;
    float drag = 7f; // kg/s
    float mass = 1f; // 1 kg

    float runMultiplier = 1.5f;

    float jumpTime = 0f;
    float jumpHoldMultiplier = 0.7f;
    float jumpHoldMaxTime = 1f;

    Player (World world) {
        this.world = world;
    }

    void jump () {
        if (!jump) {
            force.y += jumpForce;
            jump = true;
            jumpHold = true;
        }
    }

    void moveLeft () {
        force.x -= walkForce;
        if(run) force.x *= runMultiplier;
    }

    void moveRight () {
        force.x += walkForce;
        if(run) force.x *= runMultiplier;
    }

    void run () {
        run = true;
    }

    void calculateForces (float dt) {
        if (position.y > 0)
            force.y -= world.gravity;

        // Apply jump modifiers
        if (jumpHold)
            force.y += mass * world.gravity * jumpHoldMultiplier * MathUtils.clamp(1 - jumpTime / jumpHoldMaxTime, 0f, 1f);
        else
            force.y -= mass * world.gravity * jumpHoldMultiplier;

        // Apply drag (if in-contact with the ground)
        force.x -= velocity.x * drag;

        // Calculate velocity and position changes
        velocity.x += dt * (force.x / mass);
        velocity.y += dt * (force.y / mass);
    }

    void updateState (float dt) {
        position.x += dt * velocity.x;
        position.y += dt * velocity.y;

        // Snap to ground
        if (position.y - PLAYER_HEIGHT / 2 <= 0) {
            position.y = PLAYER_HEIGHT / 2;
            velocity.y = 0f;
            jump = false;
            jumpHold = false;
            jumpTime = 0f;
        }

        // Cleanup for the next frame
        force.x = force.y = 0f;
        jumpTime += dt;
        run = false;
    }
}
