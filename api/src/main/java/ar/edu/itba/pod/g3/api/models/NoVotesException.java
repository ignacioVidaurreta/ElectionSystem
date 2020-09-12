package ar.edu.itba.pod.g3.api.models;

public class NoVotesException extends Exception {
    public NoVotesException() {
        super("No votes");
    }

    public NoVotesException(String message) {
        super(message);
    }
}
