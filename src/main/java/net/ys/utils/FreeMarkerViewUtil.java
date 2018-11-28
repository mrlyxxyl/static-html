package net.ys.utils;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Locale;
import java.util.Map;

public class FreeMarkerViewUtil extends FreeMarkerView {

    static String rootPath = "/html";

    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        exposeModelAsRequestAttributes(model, request);
        SimpleHash fmModel = buildTemplateModel(model, request, response);

        Locale locale = RequestContextUtils.getLocale(request);

        if (Boolean.FALSE.equals(model.get("CREATE_HTML"))) {
            processTemplate(getTemplate(locale), fmModel, response);
        } else {
            createHTML(getTemplate(locale), fmModel, request, response);
        }
    }

    public void createHTML(Template template, SimpleHash model, HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException, ServletException {
        String basePath = request.getSession().getServletContext().getRealPath(rootPath);
        String requestHTML = this.getRequestHTML(request);
        String htmlPath = basePath + requestHTML;
        File htmlFile = new File(htmlPath);
        if (!htmlFile.getParentFile().exists()) {
            htmlFile.getParentFile().mkdirs();
        }
        if (!htmlFile.exists()) {
            htmlFile.createNewFile();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
        template.process(model, out);
        out.flush();
        out.close();
        request.getRequestDispatcher(rootPath + requestHTML).forward(request, response);
    }

    private String getRequestHTML(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        requestURI = requestURI.replaceFirst(contextPath, "");
        requestURI = requestURI.substring(0, requestURI.indexOf(".")) + ".html";
        return requestURI;
    }
}