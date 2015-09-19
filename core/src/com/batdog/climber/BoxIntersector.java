package com.batdog.climber;

import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.List;

// Handles collision detection between boxes (primitives for player and obstacles)
// TODO: Implement swept collision detection to avoid popping at high velocity
//
// References
// http://hamaluik.com/posts/simple-aabb-collision-using-the-minkowski-difference/
// http://hamaluik.com/posts/swept-aabb-collision-detection-using-the-minkowski-difference/
public class BoxIntersector {
    World world;
    Box md;
    Box mainBox;
    Box obstacleBox;
    Vector2 normalVector;
    Vector2 penetrationVector;

    public BoxIntersector(World world, Box mainBox) {
        this.world = world;
        this.mainBox = mainBox;
    }

    public void setObstacle(Box obstacleBox) {
        this.obstacleBox = obstacleBox;
        updateMinkowskiDifference();
        updatePenetrationAndNormalVectors();
    }

    private void updateMinkowskiDifference() {
        float x = mainBox.getLeft() - obstacleBox.getLeft() - obstacleBox.getWidth();
        float y = obstacleBox.getBottom() - mainBox.getBottom() - mainBox.getHeight();
        float w = mainBox.getWidth() + obstacleBox.getWidth();
        float h = mainBox.getHeight() + obstacleBox.getHeight();
        md = new Box(world, x, y, w, h);
    }

    // Vectors are outward normals of otherBox
    private void updatePenetrationAndNormalVectors() {
        List<Vector2> edgeVectors = Arrays.asList(
                new Vector2(-md.getLeft(), 0),
                new Vector2(-md.getRight(), 0),
                new Vector2(0, md.getBottom()),
                new Vector2(0, md.getTop()));
        List<Vector2> normalVectors = Arrays.asList(
                new Vector2(+1, 0),
                new Vector2(-1, 0),
                new Vector2(0, -1),
                new Vector2(0, +1));
        float minDist = md.getWidth() + md.getHeight();
        int minIndex = 1;
        for (int i=0; i < edgeVectors.size(); i++) {
            Vector2 edgeVector = edgeVectors.get(i);
            float currDist = edgeVector.len();
            if (currDist < minDist) {
                minDist = currDist;
                minIndex = i;
            }
        }
        penetrationVector = edgeVectors.get(minIndex);
        normalVector = normalVectors.get(minIndex);
    }

    // If MD includes origin, then boxes are intersecting
    public boolean intersecting() {
        return ((md.getLeft() <= world.epsilon) && (md.getRight() >= -world.epsilon) &&
                (md.getBottom() <= world.epsilon) && (md.getTop() >= -world.epsilon));
    }
}
