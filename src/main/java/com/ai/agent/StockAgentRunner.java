package com.ai.agent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StockAgentRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        // Run your existing logic
        StockAgent.main(args);

        // Exit after completion (important for GitHub Actions)
        System.exit(0);
    }
}
