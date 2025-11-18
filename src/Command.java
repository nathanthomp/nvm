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
                return new AddFolderCommand();
            case "rmdir":
                return new RemoveFolderCommand();
            case "touch":
                return new AddFileCommand();
            case "rm":
                return new RemoveFileCommand();
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
        @Override
        public void execute(VirtualMachine virtualMachine) {
            System.out.println("Executing: AddFolderCommand.");
        }
    }

    private static class RemoveFolderCommand extends Command {
        @Override
        public void execute(VirtualMachine virtualMachine) {
            System.out.println("Executing: RemoveFolderCommand.");
        }
    }

    private static class AddFileCommand extends Command {
        @Override
        public void execute(VirtualMachine virtualMachine) {
            System.out.println("Executing: AddFileCommand.");
        }
    }

    private static class RemoveFileCommand extends Command {
        @Override
        public void execute(VirtualMachine virtualMachine) {
            System.out.println("Executing: RemoveFileCommand.");
        }
    }

}