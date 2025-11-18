public abstract class Command {
    public static Command getCommand(String input) {
        String[] tokens = input.split(" ");
        switch (tokens[0]) {
            case "test":
                return new TestCommand();
            case "pwd":
                return new PrintCurrentFolderCommand();
            case "ls":
                return new ListCurrentFolderCommand();
            case "mkdir":
                return new AddFolderCommand(tokens[1]);
            case "touch":
                return new AddFileCommand(tokens[1]);
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }

    public abstract void execute(VirtualMachine virtualMachine);

    private static class TestCommand extends Command {
        @Override
        public void execute(VirtualMachine virtualMachine) {
            System.out.println("Executing: TestCommand.");
        }
    }

    private static class PrintCurrentFolderCommand extends Command {
        @Override
        public void execute(VirtualMachine virtualMachine) {
            virtualMachine.fileSystem.printCurrentFolder();
        }
    }

    private static class ListCurrentFolderCommand extends Command {
        @Override
        public void execute(VirtualMachine virtualMachine) {
            virtualMachine.fileSystem.listCurrentFolder();
        }
    }

    private static class AddFolderCommand extends Command {
        private String name;

        public AddFolderCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(VirtualMachine virtualMachine) {
            /**
             * Ideally, we want to create a new Folder here.
             */
            virtualMachine.fileSystem.getCurrentFolder().addFolder(this.name);
        }
    }

    private static class AddFileCommand extends Command {
        private String name;

        public AddFileCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(VirtualMachine virtualMachine) {
            /**
             * Ideally, we want to create a new File here.
             */
            virtualMachine.fileSystem.getCurrentFolder().addFile(name);
        }
    }
}