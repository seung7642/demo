package me.pangtrue;

public class Proxy implements IService {

    // 클라이언트가 사용하고자하는 객체를 참조로 가지고 있습니다.
    private final IService service = new Service();

    @Override
    public String runSomething() {
        System.out.println("호출에 대한 흐름 제어가 주 목적입니다. 반환 결과를 그대로 전달합니다.");
        return service.runSomething();
    }
}
