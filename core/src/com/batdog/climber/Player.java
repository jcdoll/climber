package com.batdog.climber;

import com.badlogic.gdx.math.Vector2;

public class Player extends Box {
    Vector2 jumpDir = new Vector2();
    boolean jump = true;
    boolean jumpHold = false;
    boolean run = false;
    boolean surfaceContact = false;
    boolean floorContact = false;

    float jumpForce = 800f; // 1 N
    float walkForce = 40f;
    float wallFrictionCoefficient = 6f; // N-s/m
    float movementFrictionCoefficient= 2f; // N-s/m

    float runMultiplier = 1.6f;

    Player (World world) {
        super(world);
    }

    void jump () {
        // First press of button while not in mid-air: jump
        // Continuing to hold button while in mid-air: jump boost
        // Continuing to hold button after hitting ground: nothing
        if (!jump && !jumpHold) {
            velocity.y = 0f; // Remove vertical velocity before wall jump
            float forceScaling = (surfaceContact) ? 1.414f : 1f;
            force.add(jumpDir.cpy().scl(forceScaling * jumpForce));
            jump = true;
            jumpHold = true;
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

    void updateState (float dt) {
        // Apply gravity (collision handling prevents falling through floor)
        force.add(world.gravityDir.cpy().scl(world.gravity));

        // Apply horizontal friction regardless of player state
        // Apply vertical friction if player is sliding down along a wall
        force.x -= velocity.x * movementFrictionCoefficient;
        if (surfaceContact && velocity.y < 0)
            force.y -= velocity.y * wallFrictionCoefficient;

        // Once player releases the jump button while mid-air, set vertical velocity to zero on the next frame
        if (jump && velocity.y > 0 && !jumpHold)
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
