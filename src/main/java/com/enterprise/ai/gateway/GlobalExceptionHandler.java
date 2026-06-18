// Subsystem Name: Diagnostics & Exception Domain Layer
// Domain Context: Perimeter Boundary Group
// File Location: src/main/java/com/enterprise/ai/gateway/GlobalExceptionHandler.java

package com.enterprise.ai.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleEnterpriseCollapse(Exception ex) {
        String uniqueTraceId = "TRC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                ex.getMessage()
        );
        
        problemDetail.setTitle("INTERNAL_GATEWAY_FAILURE");
        problemDetail.setType(URI.create("https://healthcare360.enterprise.com/errors/runtime-collapse"));
        
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("trace_id", uniqueTraceId);
        problemDetail.setProperty("remediation", "Verify connection pooling or port availability definitions.");

        System.err.println("CRITICAL FAILURE LOGGED WITH ID: " + uniqueTraceId);

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}