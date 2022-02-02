package me.pangtrue;

public class CreamDecorator extends Cake {

    private Cake cake;

    public CreamDecorator(Cake cake) {
        this.cake = cake;
    }

    @Override
    public String getCake() {
        return "생크림 " + cake.getCake() + " 생크림";
    }
}
