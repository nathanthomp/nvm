public abstract class Command {
    public abstract void execute(Nvm vm);

    public static Command getCommand(String input) {
        String[] tokens = input.split(" ");
        switch (tokens[0]) {
            case "test":
                return new TestCommand();
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }

    private static class TestCommand extends Command {
        @Override
        public void execute(Nvm vm) {
            System.out.println("Executing: TestCommand.");
        }
    }

}