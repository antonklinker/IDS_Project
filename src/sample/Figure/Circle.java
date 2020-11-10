package sample.Figure;

import javafx.scene.canvas.GraphicsContext;

public class Circle extends Figure {
    @Override
    public String toString() {
        return("Circle");
    }

    @Override
    public Figure getCopy() {
        return new Circle();
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.strokeOval(start.getX(), start.getY(), end.getX()-start.getX(), end.getY()-start.getY());
    }
}
