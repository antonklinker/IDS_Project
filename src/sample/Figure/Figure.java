package sample.Figure;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class Figure {
    public Point start;
    public Point end;

    public abstract void draw(GraphicsContext graphicsContext);

    public abstract Figure getCopy();

}