package com.example.java_final_assignment.configuration;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter implements Filter {

    private static final String TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // Cuts a clean, short 8-character unique string for context clarity
            String traceId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put(TRACE_ID, traceId);

            if (request instanceof HttpServletRequest) {
                MDC.put("uri", ((HttpServletRequest) request).getRequestURI());
            }

            chain.doFilter(request, response);
        } finally {
            // Wipe clean after the request exits to keep the thread pool immaculate
            MDC.clear();
        }
    }
}