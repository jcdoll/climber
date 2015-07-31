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
    Vector2 jumpDir = new Vector2();

    boolean jump = true;
    boolean jumpHold = false;
    boolean run = false;
    boolean surfaceContact = false;

    float jumpForce = 300f; // 1 N
    float walkForce = 50f;
    float surfaceFrictionCoefficient = 7f; // N-s/m
    float dragCoefficient = 3f; // N-s/m
    float mass = 1f; // 1 kg

    float runMultiplier = 1.6f;

    float jumpTime = 0f;
    float jumpHoldMultiplier = 0.4f;
    float jumpHoldMaxTime = 0.1f;

    Player (World world) {
        this.world = world;
    }

    void jump () {
        // First press of button while not in mid-air: jump
        // Continuing to hold button while in mid-air: jump boost
        // Continuing to hold button after hitting ground: nothing
        if (!jump && !jumpHold) {
            velocity.y = 0f; // Remove vertical velocity before wall jump
            force.add(jumpDir.cpy().scl(jumpForce));
            jump = true;
            jumpHold = true;
        } else if (jump && jumpHold) {
            float jumpForceScaling = jumpHoldMultiplier * MathUtils.clamp(1 - jumpTime / jumpHoldMaxTime, 0f, 1f);
            force.add(jumpDir.cpy().scl(jumpForce * jumpForceScaling));
        }
    }

    // TODO: Force greater distance from wall during wall jump
    // TODO: Modify jump direction based upon user input
    void moveLeft () {
        force.x -= walkForce;
        if (run) force.x *= runMultiplier;
    }

    void moveRight () {
        force.x += walkForce;
        if (run) force.x *= runMultiplier;
    }

    void run () {
        run = true;
    }

    void calculateForces (float dt) {
        // Apply gravity (collision handling prevents falling through floor)
        force.add(world.gravityDir.cpy().scl(world.gravity));

        // TODO: Apply bonus downwards force depending on jump time / jumpHold for less floaty jumping

        // Apply friction (all directions if in contact with boundary, otherwise just horizontal
        if (surfaceContact)
            force.sub(velocity.cpy().scl(surfaceFrictionCoefficient));
        else
            force.x -= velocity.x * dragCoefficient;

        // Calculate velocity and position changes
        velocity.add(force.scl(dt / mass));
    }

    void updateState (float dt) {
        position.add(velocity.cpy().scl(dt));

        // Cleanup for the next frame
        force.x = force.y = 0f;
        jumpTime += dt;
        run = false;
    }
}
