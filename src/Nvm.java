public class Nvm {
    public static Nvm start() {
        System.out.println("Starting the nvm!");
        return new Nvm();
    }

    private Nvm() {

    }

    public void stop() {
        System.out.println("Stopping the nvm.");
    }
}
