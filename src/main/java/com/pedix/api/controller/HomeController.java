package com.pedix.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8">
                  <title>Pedix API</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      text-align: center;
                      margin-top: 60px;
                      background-color: #f4f6f8;
                      color: #333;
                    }
                    a {
                      color: #007bff;
                      text-decoration: none;
                      font-weight: bold;
                    }
                    a:hover {
                      text-decoration: underline;
                    }
                  </style>
                </head>
                <body>
                  <h1>🚀 API Pedix está rodando!</h1>
                  <p>Acesse o <a href="/swagger-ui/index.html">Swagger UI</a> para testar os endpoints.</p>
                </body>
                </html>
                """;
    }
}
