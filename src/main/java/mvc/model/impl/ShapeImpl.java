package mvc.model.impl;

import mvc.model.api.Shape;
import mvc.model.api.SliceableTypeEnum;

public class ShapeImpl implements Shape {
    
    private double lenght;
    private double perimeter;
    private double area;
    private double apothem;
    private SliceableTypeEnum type;

    ShapeImpl(SliceableTypeEnum type, double lenght) {
        this.type = type;
        this.lenght = lenght;
        this.perimeter = perimeter();
        this.area = area();
        this.apothem = apothem();
    }

    private double apothem() {
        return switch(this.type) {
            case SliceableTypeEnum.POWERUP -> this.lenght;
            default -> this.area * 2 / this.perimeter * 2;
        };
    }

    private double area() {
        return switch(this.type) {
            case SliceableTypeEnum.TRIANGLE -> Math.pow(this.lenght, 2) * Math.sqrt(3) / 4;
            case SliceableTypeEnum.SQUARE -> Math.pow(this.lenght, 2);
            case SliceableTypeEnum.PENTAGON -> Math.pow(this.lenght, 2) * 1.72;
            case SliceableTypeEnum.HEXAGON -> Math.pow(this.lenght, 2) * 2.598;
            default -> Math.PI * Math.pow(this.lenght, 2);
        };
    }

    private double perimeter() {
        return switch(this.type) {
            case SliceableTypeEnum.TRIANGLE -> this.lenght * 3;
            case SliceableTypeEnum.SQUARE -> this.lenght * 4;
            case SliceableTypeEnum.PENTAGON -> this.lenght * 5;
            case SliceableTypeEnum.HEXAGON -> this.lenght * 5;
            default -> this.lenght;
        };
    }

    @Override
    public double getApothem() {
        return this.apothem;
    }

     @Override
    public double getLenght() {
        return this.lenght;
    }
}
