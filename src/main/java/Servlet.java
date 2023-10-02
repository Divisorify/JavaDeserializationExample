import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@WebServlet(
        name = "Servlet",
        urlPatterns = {"/"}
)
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        Data data = null;

        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("data")) {
                    try {
                        byte[] serialized = Base64.decodeBase64(cookie.getValue());
                        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        data = (Data) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (null == data || data.getName().isEmpty()) {
            data = new Data("Witaj nieznajomy!");
        }

        request.setAttribute("name", data.getName());
        request.getRequestDispatcher("page.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (null != request.getParameter("name")) {
            Data data = new Data(request.getParameter("name"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);

            Cookie cookie = new Cookie("data", Base64.encodeBase64String(baos.toByteArray()));
            response.addCookie(cookie);
        }

        response.sendRedirect("/");
    }
}
