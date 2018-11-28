package net.ys.controller;

import net.ys.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "frame")
public class FreeMarkerController {

    static String rootPath = "/html";

    /**
     * 生成html
     *
     * @param request
     * @param response
     * @param session
     * @return
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping("create")
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {

        String realPath = session.getServletContext().getRealPath(rootPath + "/frame/create.html");
        File file = new File(realPath);
        if (file.exists()) {
            request.getRequestDispatcher(rootPath + "/frame/create.html").forward(request, response);
            return null;
        }

        ModelAndView mav = new ModelAndView("index");
        User user = new User();
        user.setUsername("<h2>小明</h2>");
        user.setPassword("<a href='https://www.hao123.com/'>百度</a>");
        List<User> users = new ArrayList<User>();
        users.add(user);
        mav.addObject("users", users);
        mav.addObject("title", "这是标题");
        return mav;
    }

    /**
     * 不生成html
     *
     * @return
     */
    @RequestMapping("no")
    public ModelAndView no() {
        ModelAndView mav = new ModelAndView("index");
        User user = new User();
        user.setUsername("<h2>小明</h2>");
        user.setPassword("<a href='https://www.hao123.com/'>百度</a>");
        List<User> users = new ArrayList<User>();
        users.add(user);
        mav.addObject("users", users);
        mav.addObject("title", "网站标题");
        mav.addObject("CREATE_HTML", false);
        return mav;
    }
}