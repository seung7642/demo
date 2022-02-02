package me.pangtrue;

public class Main {

    public static void main(String[] args) {
        // 프록시 객체의 메서드를 호출합니다.
        // 클라이언트가 사용하고자 하는 객체와 프록시 객체는 똑같은 인터페이스를 implements 하기 때문에 전혀 문제가 없습니다.
        IService proxy = new Proxy();
        System.out.println(proxy.runSomething());
    }
}
