package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import util.JWTUtil;

@WebFilter("/*")
public class JWTFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        
        if (path.contains("/login") || path.contains("/register") || path.endsWith("login.html") || path.endsWith("signup.html") || path.endsWith("index.html") || path.endsWith("/") || path.endsWith(".css") || path.endsWith(".js")) {
        	chain.doFilter(request, response);
            return;
        }

        int userId = -1;

        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    try {
                        userId = JWTUtil.extractUserId(cookie.getValue());
                    } catch (Exception e) {
                        userId = -1;
                    }
                }
            }
        }

        if (userId == -1) {
            res.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        req.setAttribute("userId", userId);

   
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }
}