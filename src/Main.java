import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        VirtualMachine virtualMachine = VirtualMachine.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

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

        scanner.close();
        virtualMachine.stop();
    }
}
