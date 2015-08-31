package com.batdog.climber;

import com.badlogic.gdx.math.Vector2;

// TODO: Rework controls from friction to velocity change input
// TODO: Add stickiness to walls
public class Player extends Box {
    Vector2 jumpDir = new Vector2();
    boolean jump = true;
    boolean jumpHold = false;
    boolean run = false;
    boolean floorContact = false;
    boolean surfaceContact = false;

    float jumpForce = 800f; // N
    float walkForce = 40f; // N
    float wallFrictionCoefficient = 6f; // N-s/m
    float movementFrictionCoefficient= 2f; // N-s/m
    float runMultiplier = 1.6f; // Force multiplier while run button is held down

    Player (World world) {
        super(world);
    }

    void jump () {
        // First press of button while not in mid-air: jump
        // Continuing to hold button while in mid-air: jump boost
        // Continuing to hold button after hitting ground: nothing
        // Handle wall jumping nicely (vertical gain same for floor jump vs. surface jump)
        if (!jump && !jumpHold) {
            velocity.y = 0f;
            float forceScaling = (floorContact) ? 1f : 1.414f;
            force.add(jumpDir.cpy().scl(forceScaling * jumpForce));
            jump = true;
            jumpHold = true;
        }
    }

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

    void updateState (float dt) {
        // Apply gravity (collision handling prevents falling through floor)
        force.add(world.gravityDir.cpy().scl(world.gravity));

        // Apply horizontal friction regardless of player state
        // Apply vertical friction if player is sliding down along a wall
        force.x -= velocity.x * movementFrictionCoefficient;
        if (surfaceContact && velocity.y < 0)
            force.y -= velocity.y * wallFrictionCoefficient;

        // Once player releases the jump button, set vertical velocity to zero on the next frame
        if (velocity.y > 0 && !jumpHold)
            force.y -= mass * velocity.y / (2 * world.dt_physics);

        // Verlet (trapezoidal) integration
        position.add(velocity.cpy().scl(0.5f * dt));
        velocity.add(force.scl(world.dt_physics / mass));
        position.add(velocity.cpy().scl(0.5f * dt));

        // Cleanup for the next frame
        force.x = force.y = 0f;
        run = false;
    }
}
