import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        VirtualMachine virtualMachine = VirtualMachine.start();
        try (Scanner scanner = new Scanner(System.in)) {
            Terminal terminal = new Terminal(virtualMachine, scanner);
            terminal.run();
        } finally {
            virtualMachine.stop();
        }
    }
}
