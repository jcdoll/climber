package com.batdog.climber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.FloatArray;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class Plotter {
    float labelInset; // pixels to inset labels
    float x;
    float y;
    float width;
    float height;
    FloatArray data;
    float[] plotLimits = new float[2]; // plot limits [min, max]
    float[] plotColor = new float[3];
    BitmapFont font;
    GlyphLayout glyphLayout;
    String title;
    Supplier<Float> dataGenerator;

    Plotter(String title, float x, float y, float width, float height, int size, float[] plotLimits, Supplier<Float> dataGenerator) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.plotLimits = plotLimits;
        data = new FloatArray(new float[size]);
        this.dataGenerator = dataGenerator;

        labelInset = 5f;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        glyphLayout = new GlyphLayout();
        plotColor[0] = 11/255f;
        plotColor[1] = 132/255f;
        plotColor[2] = 199/255f;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        float newValue = dataGenerator.get();
        data.removeIndex(0);
        data.add(newValue);

        // Update plot limits from default if needed
        if (newValue < plotLimits[0]) plotLimits[0] = MathUtils.floor(newValue / 10)*10;
        if (newValue > plotLimits[1]) plotLimits[1] = MathUtils.ceil(newValue / 10)*10;

        float x0 = -Gdx.graphics.getWidth() / 2 + x;
        float y0 = Gdx.graphics.getHeight() / 2 - y - height;
        float widthScale = width / data.size;
        float heightScale = height / (plotLimits[1] - plotLimits[0]);

        // Draw label
        batch.begin();
        glyphLayout.setText(font, title);
        font.draw(batch, glyphLayout, x0 + width - glyphLayout.width - labelInset, y0 + height - labelInset);
        glyphLayout.setText(font, String.valueOf(plotLimits[0]));
        font.draw(batch, glyphLayout, x0 + labelInset, y0 + glyphLayout.height + labelInset);
        glyphLayout.setText(font, String.valueOf(plotLimits[1]));
        font.draw(batch, glyphLayout, x0 + labelInset, y0 + height - labelInset);
        batch.end();

        // Draw outline
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(x0, y0, width, height);

        // Draw data
        shapeRenderer.setColor(plotColor[0], plotColor[1], plotColor[2], 1);
        for (int i = 0; i < data.size - 1; i++) {
            shapeRenderer.line(
                    x0 + i * widthScale,
                    y0 + (data.get(i) - plotLimits[0]) * heightScale,
                    x0 + (i + 1) * widthScale,
                    y0 + (data.get(i + 1) - plotLimits[0]) * heightScale);
        }
        shapeRenderer.end();
    }
}
