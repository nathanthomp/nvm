import java.util.Scanner;

public class Terminal {
    private final VirtualMachine virtualMachine;
    private final Scanner scanner;

    private State state;
    private boolean running = true;

    public Terminal(VirtualMachine virtualMachine, Scanner scanner) {
        this.virtualMachine = virtualMachine;
        this.scanner = scanner;
        this.state = new Terminal.State.LoginState(this);
    }

    public void run() {
        while (this.running) {
            this.state.queryInput();
            String input = this.scanner.nextLine();
            this.state.handleInput(input);
        }
    }

    private static abstract class State {
        private Terminal terminal;

        public State(Terminal terminal) {
            this.terminal = terminal;
        }

        public abstract void queryInput();

        public abstract void handleInput(String input);

        public static class LoginState extends State {
            public LoginState(Terminal terminal) {
                super(terminal);
            }

            @Override
            public void queryInput() {
                System.out.println("\nEnter username:");
                System.out.print("> ");
            }

            @Override
            public void handleInput(String input) {
                User user = User.getUser(input);
                if (user == null) {
                    System.out.println("User " + input + " not found");
                    return;
                }
                super.terminal.virtualMachine.setCurrentUser(user);
                System.out.println("Hello, " + user.getUsername() + "\n");

                super.terminal.state = new CommandState(super.terminal);
                super.terminal.virtualMachine.fileSystem.changeCurrentFolder("user");
                super.terminal.virtualMachine.fileSystem.changeCurrentFolder(user.getUsername());
            }
        }

        private static class CommandState extends State {
            public CommandState(Terminal terminal) {
                super(terminal);
            }

            @Override
            public void queryInput() {
                System.out.print(super.terminal.virtualMachine.fileSystem.getCurrentFolder().getPath() + "> ");
            }

            @Override
            public void handleInput(String input) {
                if (input.isEmpty()) {
                    return;
                }
                if (input.equals("exit")) {
                    super.terminal.running = false;
                    return;
                }
                try {
                    Command command = Command.getCommand(input);
                    command.execute(super.terminal);
                } catch (Exception e) {
                    System.out.println("Invalid command or error: " + e.getMessage());
                }
            }
        }
    }

    private static abstract class Command {
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
                    case "path":
                        return new PathCommand(tokens[1]);
                    default:
                        throw new IllegalArgumentException("Unknown command");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Missing argument");
            }
        }

        public abstract void execute(final Terminal terminal);

        private static class TestCommand extends Command {
            @Override
            public void execute(final Terminal terminal) {
                System.out.println("Executing: TestCommand.");
            }
        }

        private static class PrintCurrentFolderCommand extends Command {
            @Override
            public void execute(final Terminal terminal) {
                System.out
                        .println("Current Folder: " + terminal.virtualMachine.fileSystem.getCurrentFolder().getName());
            }
        }

        private static class ListCurrentFolderCommand extends Command {
            @Override
            public void execute(final Terminal terminal) {
                terminal.virtualMachine.fileSystem.getCurrentFolder().list("");
            }
        }

        private static class AddFolderCommand extends Command {
            private String name;

            public AddFolderCommand(String name) {
                this.name = name;
            }

            @Override
            public void execute(final Terminal terminal) {
                FileSystem.Folder folder = new FileSystem.Folder(this.name,
                        terminal.virtualMachine.fileSystem.getCurrentFolder());
                terminal.virtualMachine.fileSystem.getCurrentFolder().add(folder);
            }
        }

        private static class AddFileCommand extends Command {
            private String name;

            public AddFileCommand(String name) {
                this.name = name;
            }

            @Override
            public void execute(final Terminal terminal) {
                FileSystem.Folder folder = terminal.virtualMachine.fileSystem.getCurrentFolder();
                FileSystem.File file = new FileSystem.File(this.name, folder);
                folder.add(file);
            }
        }

        private static class RemoveFileSystemComponentCommand extends Command {
            private String name;

            public RemoveFileSystemComponentCommand(String name) {
                this.name = name;
            }

            @Override
            public void execute(final Terminal terminal) {
                /**
                 * Change to allow only name into the remove function
                 */
                terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .remove(terminal.virtualMachine.fileSystem.getCurrentFolder().get(this.name));
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
            public void execute(final Terminal terminal) {
                boolean canRead = this.permissions.charAt(0) == 'r';
                boolean canWrite = this.permissions.charAt(1) == 'w';
                boolean canExecute = this.permissions.charAt(2) == 'x';
                terminal.virtualMachine.fileSystem.getCurrentFolder().get(name).setPermissions(canRead, canWrite,
                        canExecute);
            }
        }

        private static class ChangeFolderCommand extends Command {
            private String path;

            public ChangeFolderCommand(String path) {
                this.path = path;
            }

            @Override
            public void execute(final Terminal terminal) {
                // terminal.virtualMachine.fileSystem.changeCurrentFolder(this.name);

                // FileSystem.SearchVisitor searchVisitor = new
                // FileSystem.SearchVisitor(this.name);
                // terminal.virtualMachine.fileSystem.getCurrentFolder().accept(searchVisitor);
                // if (searchVisitor.getResult() == null) {
                // throw new IllegalArgumentException(this.name + " not found");
                // }
                // if (searchVisitor.getResult() instanceof FileSystem.File) {
                // throw new IllegalArgumentException(this.name + " is not a folder");
                // }
                // FileSystem.Folder folder = (FileSystem.Folder) searchVisitor.getResult();
                // terminal.virtualMachine.fileSystem.setCurrentFolder(folder);

                FileSystem.Component component = terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .resolve(this.path);
                if (!(component instanceof FileSystem.Folder)) {
                    throw new IllegalArgumentException(this.path + " is not a folder");
                }
                FileSystem.Folder folder = (FileSystem.Folder) component;
                terminal.virtualMachine.fileSystem.setCurrentFolder(folder);
            }
        }

        private static class ReadFileCommand extends Command {
            private String name;

            public ReadFileCommand(String name) {
                this.name = name;
            }

            @Override
            public void execute(final Terminal terminal) {
                if (!(terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .get(this.name) instanceof FileSystem.File)) {
                    throw new IllegalArgumentException(this.name + " is not a file");
                }
                FileSystem.File file = (FileSystem.File) terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .get(this.name);
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
            public void execute(final Terminal terminal) {
                if (!(terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .get(this.name) instanceof FileSystem.File)) {
                    throw new IllegalArgumentException(this.name + " is not a file");
                }
                FileSystem.File file = (FileSystem.File) terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .get(this.name);
                file.setContent(this.content);
            }
        }

        private static class InfoCommand extends Command {
            @Override
            public void execute(final Terminal terminal) {
                System.out.println("currentUser: " + terminal.virtualMachine.getCurrentUser().getUsername());
                System.out.println("currentFolder: " + terminal.virtualMachine.fileSystem.getCurrentFolder().getName());
            }
        }

        private static class LogoutCommand extends Command {
            @Override
            public void execute(final Terminal terminal) {
                terminal.virtualMachine.setCurrentUser(null);
                terminal.state = new Terminal.State.LoginState(terminal);
            }
        }

        private static class PathCommand extends Command {
            private String name;

            public PathCommand(String name) {
                this.name = name;
            }

            @Override
            public void execute(final Terminal terminal) {
                FileSystem.Component component = terminal.virtualMachine.fileSystem.getCurrentFolder()
                        .get(this.name);
                System.out.println(component.getPath());
            }
        }
    }
}
