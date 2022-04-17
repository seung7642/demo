package me.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.servlet.basic.HelloData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        var helloData = new HelloData();
        helloData.setUsername("seungho");
        helloData.setAge(20);

        var objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(helloData);
        response.getWriter().write(result);
    }
}
