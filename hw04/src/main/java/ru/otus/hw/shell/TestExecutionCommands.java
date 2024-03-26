package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Test execution Commands")
@RequiredArgsConstructor
public class TestExecutionCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Start test")
    public String start() throws Exception {
        testRunnerService.run();
        return "Test has been completed";
    }

}
