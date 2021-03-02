package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class GreetingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println( req.getReader().lines().collect(Collectors.joining()));
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        JSONObject json = new JSONObject();
        json.put("email", req.getParameter("text"));
        writer.println(json);
        writer.flush();
    }
}
