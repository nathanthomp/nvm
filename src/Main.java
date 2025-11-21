import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        VirtualMachine virtualMachine = VirtualMachine.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (virtualMachine.getCurrentUser() == null) {
                handleLogin(virtualMachine, scanner);
            } else {
                if (!handleCommand(virtualMachine, scanner)) {
                    break;
                }
            }
        }

        scanner.close();
        virtualMachine.stop();
    }

    private static void handleLogin(VirtualMachine virtualMachine, Scanner scanner) {
        System.out.println("Enter username:");
        String username = getInput(scanner);
        if (!User.userExists(username)) {
            System.out.println("User " + username + " not found\n");
            return;
        }
        virtualMachine.setCurrentUser(new User(username));
        System.out.println("Hello, " + username + "\n");
    }

    private static boolean handleCommand(VirtualMachine virtualMachine, Scanner scanner) {
        String input = getInput(scanner);
        if (input.isEmpty()) {
            return true;
        }
        if (input.equals("exit")) {
            return false;
        }
        try {
            Command command = Command.getCommand(input);
            command.execute(virtualMachine);
        } catch (Exception e) {
            System.out.println("Invalid command or error: " + e.getMessage());
        }
        return true;
    }

    private static String getInput(Scanner scanner) {
        System.out.print("> ");
        return scanner.nextLine();
    }
}
