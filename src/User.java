public class User {
    public static final User[] users = { new User("nathan") };

    public static boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean verifyUser(String username, String password) {
        return false;
    }

    private String username;
    private String password;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public FileSystem.Folder getHomeFolder() {
        return null;
    }
}
