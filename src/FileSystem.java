import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    private Folder rootFolder;
    private Folder currentFolder;

    public FileSystem() {
        Folder rootFolder = new Folder("root", null);
        this.rootFolder = rootFolder;
        this.currentFolder = rootFolder;
    }

    public Folder getCurrentFolder() {
        return this.currentFolder;
    }

    public void changeCurrentFolder(String name) {
        if (name.equals("..")) {
            this.currentFolder = this.currentFolder.parent;
            return;
        }
        FileSystemComponent component = this.currentFolder.get(name);
        if (component instanceof File) {
            throw new IllegalArgumentException(name + "is not a folder");
        }
        Folder folder = (Folder) component;
        this.currentFolder = folder;
    }

    public void printCurrentFolder() {
        System.out.println("Current Folder: " + this.currentFolder.getName());
    }

    public void listCurrentFolder() {
        this.currentFolder.list("");
    }

    public static abstract class FileSystemComponent {
        public abstract void list(String prefix);

        protected String name;
        private boolean canRead = true;
        private boolean canWrite = true;
        private boolean canExecute = true;

        public FileSystemComponent(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setPermissions(boolean canRead, boolean canWrite, boolean canExecute) {
            this.canRead = canRead;
            this.canWrite = canWrite;
            this.canExecute = canExecute;
        }

        private String getPermissions() {
            String permissions = "";
            permissions += this.canRead ? "r" : "-";
            permissions += this.canWrite ? "w" : "-";
            permissions += this.canExecute ? "x" : "-";
            return permissions;
        }
    }

    public static class Folder extends FileSystemComponent {
        private Folder parent;
        private List<FileSystemComponent> children = new ArrayList<FileSystemComponent>();

        public Folder(String name, Folder parent) {
            super(name);
            this.parent = parent;
        }

        public FileSystemComponent get(String name) {
            if (!super.canRead) {
                throw new SecurityException("Insignificant read permissions");
            }
            for (FileSystemComponent fileSystemComponent : children) {
                if (fileSystemComponent.getName().equals(name)) {
                    return fileSystemComponent;
                }
            }
            throw new IllegalArgumentException("Cannot find " + name);
        }

        public void add(FileSystemComponent component) {
            if (!super.canWrite) {
                throw new SecurityException("Insignificant write permissions");
            }
            this.children.add(component);
        }

        public void remove(FileSystemComponent component) {
            if (!super.canWrite) {
                throw new SecurityException("Insignificant write permissions");
            }
            this.children.remove(component);
        }

        @Override
        public void list(String prefix) {
            if (!super.canRead) {
                throw new SecurityException("Insignificant read permissions");
            }
            System.out.println(prefix + "Folder: (" + super.getPermissions() + ") " + super.name);
            for (FileSystemComponent fileSystemComponent : children) {
                fileSystemComponent.list(prefix + "  ");
            }
        }
    }

    public static class File extends FileSystemComponent {
        private String content;

        public File(String name) {
            super(name);
            this.content = "";
        }

        public String getContent() {
            if (!super.canRead) {
                throw new SecurityException("Insignificant read permissions");
            }
            return this.content;
        }

        public void setContent(String content) {
            if (!super.canWrite) {
                throw new SecurityException("Insignificant write permissions");
            }
            this.content = content;
        }

        @Override
        public void list(String prefix) {
            System.out.println(prefix + "File: (" + super.getPermissions() + ") " + super.name);
        }
    }
}
