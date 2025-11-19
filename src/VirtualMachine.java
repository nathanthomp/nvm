public class VirtualMachine {
    public static VirtualMachine start() {
        System.out.println("Starting the nvm!");
        return new VirtualMachine();
    }

    public final FileSystem fileSystem;

    private VirtualMachine() {
        this.fileSystem = new FileSystem();
    }

    public void stop() {
        System.out.println("Stopping the nvm.");
    }
}
