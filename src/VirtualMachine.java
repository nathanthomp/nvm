public class VirtualMachine {
    public static VirtualMachine start() {
        System.out.println("Starting the nvm!");
        return new VirtualMachine();
    }

    public final FileSystem fileSystem;
    private User currentUser;

    private VirtualMachine() {
        /**
         * Typical startup of Linux:
         * POST (Power On Self Test) - Hardware diagnostics
         * BIOS (Basic Input Output System) or UEFI (Unified Extensible Firmware
         * Interface) - Finds and starts the boot process
         * Kernel - Initializes devices and drivers, mounts the filesystem, starts
         * process with ID of 1, and sets user space
         * Systemd - Initial program is run to set up the machine
         */
        this.fileSystem = new FileSystem();
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void stop() {
        System.out.println("Stopping the nvm.");
    }
}
