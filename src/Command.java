public abstract class Command {
    public static Command getCommand(String input) {
        String[] tokens = input.split(" ");
        try {
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
                case "rm":
                    return new RemoveFileSystemComponentCommand(tokens[1]);
                case "chmod":
                    return new ChangePermissionsCommand(tokens[1], tokens[2]);
                case "cd":
                    return new ChangeFolderCommand(tokens[1]);
                case "cat":
                    return new ReadFileCommand(tokens[1]);
                case "echo":
                    return new WriteFileCommand(tokens[1], input.split("\"")[1]);
                case "info":
                    return new InfoCommand();
                case "logout":
                    return new LogoutCommand();
                default:
                    throw new IllegalArgumentException("Unknown command");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Missing argument");
        }
    }

    public abstract void execute(final VirtualMachine virtualMachine);

    private static class TestCommand extends Command {
        @Override
        public void execute(final VirtualMachine virtualMachine) {
            System.out.println("Executing: TestCommand.");
        }
    }

    private static class PrintCurrentFolderCommand extends Command {
        @Override
        public void execute(final VirtualMachine virtualMachine) {
            virtualMachine.fileSystem.printCurrentFolder();
        }
    }

    private static class ListCurrentFolderCommand extends Command {
        @Override
        public void execute(final VirtualMachine virtualMachine) {
            virtualMachine.fileSystem.listCurrentFolder();
        }
    }

    private static class AddFolderCommand extends Command {
        private String name;

        public AddFolderCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            FileSystem.Folder folder = new FileSystem.Folder(this.name, virtualMachine.fileSystem.getCurrentFolder());
            virtualMachine.fileSystem.getCurrentFolder().add(folder);
        }
    }

    private static class AddFileCommand extends Command {
        private String name;

        public AddFileCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            FileSystem.File file = new FileSystem.File(this.name);
            virtualMachine.fileSystem.getCurrentFolder().add(file);
        }
    }

    private static class RemoveFileSystemComponentCommand extends Command {
        private String name;

        public RemoveFileSystemComponentCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            /**
             * Change to allow only name into the remove function
             */
            virtualMachine.fileSystem.getCurrentFolder()
                    .remove(virtualMachine.fileSystem.getCurrentFolder().get(this.name));
        }
    }

    private static class ChangePermissionsCommand extends Command {
        private String name;
        private String permissions;

        public ChangePermissionsCommand(String name, String permissions) {
            this.name = name;
            this.permissions = permissions;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            boolean canRead = this.permissions.charAt(0) == 'r';
            boolean canWrite = this.permissions.charAt(1) == 'w';
            boolean canExecute = this.permissions.charAt(2) == 'x';
            virtualMachine.fileSystem.getCurrentFolder().get(name).setPermissions(canRead, canWrite, canExecute);
        }
    }

    private static class ChangeFolderCommand extends Command {
        private String name;

        public ChangeFolderCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            virtualMachine.fileSystem.changeCurrentFolder(this.name);
        }
    }

    private static class ReadFileCommand extends Command {
        private String name;

        public ReadFileCommand(String name) {
            this.name = name;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            if (!(virtualMachine.fileSystem.getCurrentFolder().get(this.name) instanceof FileSystem.File)) {
                throw new IllegalArgumentException(this.name + " is not a file");
            }
            FileSystem.File file = (FileSystem.File) virtualMachine.fileSystem.getCurrentFolder().get(this.name);
            System.out.println(file.getContent());
        }
    }

    private static class WriteFileCommand extends Command {
        private String name;
        private String content;

        public WriteFileCommand(String name, String content) {
            this.name = name;
            this.content = content;
        }

        @Override
        public void execute(final VirtualMachine virtualMachine) {
            if (!(virtualMachine.fileSystem.getCurrentFolder().get(this.name) instanceof FileSystem.File)) {
                throw new IllegalArgumentException(this.name + " is not a file");
            }
            FileSystem.File file = (FileSystem.File) virtualMachine.fileSystem.getCurrentFolder().get(this.name);
            file.setContent(this.content);
        }
    }

    private static class InfoCommand extends Command {
        @Override
        public void execute(final VirtualMachine virtualMachine) {
            System.out.println("currentUser: " + virtualMachine.getCurrentUser().getUsername());
            System.out.println("currentFolder: " + virtualMachine.fileSystem.getCurrentFolder().getName());
        }
    }

    private static class LogoutCommand extends Command {
        @Override
        public void execute(final VirtualMachine virtualMachine) {
            virtualMachine.setCurrentUser(null);
        }
    }
}