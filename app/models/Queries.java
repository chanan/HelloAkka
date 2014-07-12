package models;

public class Queries {
    public static class ToUpperRequest {
        public final String input;

        public ToUpperRequest(String input) {
            this.input = input;
        }
    }

    public static class ToUpperResponse {
        public final String input;
        public final String output;

        public ToUpperResponse(String input, String output) {
            this.input = input;
            this.output = output;
        }
    }

    public static class ToFirstCharLowerRequest {
        public final String input;

        public ToFirstCharLowerRequest(String input) {
            this.input = input;
        }
    }

    public static class ToFirstCharLowerResponse {
        public final String input;
        public final String output;

        public ToFirstCharLowerResponse(String input, String output) {
            this.input = input;
            this.output = output;
        }
    }

    public static class FailureToUpResponse {
        public final Object request;
        public final String message;

        public FailureToUpResponse(Object request, String message) {
            this.request = request;
            this.message = message;
        }
    }
}
