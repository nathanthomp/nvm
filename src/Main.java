import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        VirtualMachine virtualMachine = VirtualMachine.start();
        Scanner scanner = new Scanner(System.in);

        // Temp
        String correctUsername = "nathan";

        while (true) {
            if (virtualMachine.getCurrentUser() == null) {
                System.out.println("Enter username:");
                String username = getInput(scanner);
                if (!correctUsername.equals(username)) {
                    System.out.println("User " + username + " not found");
                    continue;
                }
                virtualMachine.setCurrentUser(new User(username));
                System.out.println("Hello, " + username);
            } else {
                String input = getInput(scanner);

                if (input.equals("exit")) {
                    break;
                }

                try {
                    Command command = Command.getCommand(input);
                    command.execute(virtualMachine);
                } catch (Exception e) {
                    System.err.println("Invalid command or error: " + e.getMessage());
                }
            }
        }

        scanner.close();
        virtualMachine.stop();
    }

    private static String getInput(Scanner scanner) {
        System.out.print("> ");
        return scanner.nextLine();
    }
}
