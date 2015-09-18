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
    float airDragCoefficient = 2e-2f;
    float movementFrictionCoefficient= 2f; // N-s/m
    float runMultiplier = 1.6f; // Force multiplier while run button is held down

    Player (World world) {
        super(world);
    }

    void jump () {
        // First press of button while not in mid-air: jumpSound
        // Continuing to hold button while in mid-air: jumpSound boost
        // Continuing to hold button after hitting ground: nothing
        // Handle wall jumping nicely (vertical gain same for floor jumpSound vs. surface jumpSound)
        if (!jump && !jumpHold) {
            velocity.y = 0f;
            float forceScaling = (floorContact) ? 1f : 1.414f;
            force.add(jumpDir.cpy().scl(forceScaling * jumpForce));
            jump = true;
            jumpHold = true;
            Assets.playSound(Assets.jumpSound, 0.5f);
        }
    }

    boolean wallSliding () {
        return (surfaceContact && velocity.y < 0);
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

        // Apply air drag if falling
        if (!surfaceContact && velocity.y < 0) {
            force.y -= Math.signum(velocity.y) * Math.pow(velocity.y, 2) * airDragCoefficient;
        }

        // Apply horizontal friction regardless of player state
        // Apply vertical friction if player is sliding down along a wall
        force.x -= velocity.x * movementFrictionCoefficient;
        if (wallSliding()) {
            force.y -= velocity.y * wallFrictionCoefficient;
            Assets.startSoundLoop(Assets.slideSound);
        } else {
            Assets.stopSoundLoop(Assets.slideSound);
        }

        // Once player releases the jumpSound button, set vertical velocity to zero on the next frame
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
