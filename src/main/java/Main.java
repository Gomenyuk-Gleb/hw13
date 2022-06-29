import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@WebServlet("/servlet")
public class Main extends HttpServlet {

    private final static Map<String, String> ACTIVE_SESSION = new HashMap<>();
    private final static String DEF_NAME = "unknown";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var writer = resp.getWriter();
        var name = req.getParameter("name");
        var cookies = req.getCookies();
        if (Objects.nonNull(cookies) && cookies.length > 0) {
           name = createSessionOrGet(cookies[0], name);
        }
        if (Objects.isNull(name)) {
            writer.println("Good evening, " + DEF_NAME);
        } else {
            writer.println("Good evening, " + name);
        }
        writer.flush();
    }

    private String createSessionOrGet(Cookie cookie, String name) {
        var sessionName = ACTIVE_SESSION.get(cookie.getValue());
        if (Objects.nonNull(sessionName) && sessionName.equals(name) || name == null) {
            return sessionName;
        } else {
            ACTIVE_SESSION.put(cookie.getValue(), name);
            return name;
        }
    }

}
