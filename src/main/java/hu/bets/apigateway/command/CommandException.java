package hu.bets.apigateway.command;

public class CommandException extends RuntimeException{

    public CommandException(String message) {
        super(message);
    }
}
