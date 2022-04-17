package me.servlet.web.frontcontroller.v3;

import me.servlet.web.frontcontroller.ModelView;
import me.servlet.web.frontcontroller.MyView;
import me.servlet.web.frontcontroller.v2.ControllerV2;
import me.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import me.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import me.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import me.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import me.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import me.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 1. 해당 요청 경로에 매핑되는 컨트롤러를 실행합니다.
        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        // 2. 컨트롤러가 반환한 논리 뷰 경로에 prefix, suffix 를 붙이도록 ViewResolver 에게 요청합니다.
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        // 3. 뷰를 렌더링하도록 호출합니다.
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
