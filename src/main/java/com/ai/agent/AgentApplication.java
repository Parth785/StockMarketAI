package com.ai.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgentApplication {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AgentApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

}
