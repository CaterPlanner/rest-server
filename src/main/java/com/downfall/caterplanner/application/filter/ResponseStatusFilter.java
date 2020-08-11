package com.downfall.caterplanner.application.filter;


import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/**")
public class ResponseStatusFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        //when Response..
        response.setCharacterEncoding("UTF-8");
        try {
            ResponseHeader header = new ObjectMapper().readValue(request.getReader(), ResponseHeader.class);
            ((HttpServletResponse)response).setStatus(header.getStatus().value());

        }catch (Exception e){
            e.printStackTrace();
            log.error("Not response JSON Data");
        }
    }
}
