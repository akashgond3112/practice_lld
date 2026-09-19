package com.java.lld.design;

import com.java.lld.design.command.CookieFilterCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import picocli.CommandLine;

@SpringBootApplication
public class CookieFilterApplication {

    public static void main(String[] args) {
        int exitCode = SpringApplication.exit(SpringApplication.run(CookieFilterApplication.class, args));
        System.exit(exitCode);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner commandLineRunner(
            CookieFilterCommand command,
            CommandLine.IFactory factory,
            CliExitCodeGenerator cliExitCodeGenerator
    ) {
        return args -> {
            int exitCode = new CommandLine(command, factory).execute(args);
            cliExitCodeGenerator.setExitCode(exitCode);
        };
    }
}
