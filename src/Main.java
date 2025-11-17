import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Nvm vm = Nvm.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            try {
                Command command = Command.getCommand(input);
                command.execute(vm);
            } catch (Exception e) {
                System.out.println("Invalid command or error: " + e.getMessage());
            }
        }

        scanner.close();
        vm.stop();
    }
}
