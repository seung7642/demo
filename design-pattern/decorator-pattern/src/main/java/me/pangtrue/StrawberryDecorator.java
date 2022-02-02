package me.pangtrue;

public class StrawberryDecorator extends Cake {

    private Cake cake;

    public StrawberryDecorator(Cake cake) {
        this.cake = cake;
    }

    @Override
    public String getCake() {
        return "딸기 " + cake.getCake() + " 딸기";
    }
}
