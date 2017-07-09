package hu.bets.apigateway.command;

import com.netflix.hystrix.HystrixCommand;

import java.util.Map;

public class CommandFacade {

    private Map<String, ? extends HystrixCommand<String>> commands;

    public CommandFacade(Map<String, ? extends HystrixCommand<String>> commands) {
        this.commands = commands;
    }

    public HystrixCommand<String> getRetrieveSchedulesCommand() {
        return commands.get("SCHEDULES");
    }
}
