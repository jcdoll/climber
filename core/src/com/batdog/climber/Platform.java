package com.batdog.climber;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Platform {
    public List<Box> blocks = new ArrayList<Box>();

    Platform () {
    }

    void render(SpriteBatch batch) {
        for (Box block : blocks) {
            block.render(batch);
        }
    }
}
