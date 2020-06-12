package com.rygf.config;

import com.ckfinder.connector.ConnectorServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/ckfinder/core/connector/java/connector.java", initParams = {
    @WebInitParam(name = "XMLConfig", value = "classpath:static/ckfinder.xml"),
    @WebInitParam(name = "debug", value = "false"),
    @WebInitParam(name = "configuration", value = "com.rygf.config.CKFinderConfig")
})
public class ImageBrowserServlet extends ConnectorServlet {
    @Override
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
//        prepareGetResponse(request, response, false);
        super.doGet(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
//        prepareGetResponse(request, response, true);
        super.doPost(request, response);
    }
}
