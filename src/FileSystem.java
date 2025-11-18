import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    private Folder rootFolder;
    private Folder currentFolder;

    public FileSystem() {
        Folder rootFolder = new Folder("root");
        this.rootFolder = rootFolder;
        this.currentFolder = rootFolder;
    }

    public Folder getCurrentFolder() {
        return this.currentFolder;
    }

    public void printCurrentFolder() {
        System.out.println("Current Folder: " + this.currentFolder.getName());
    }

    public void listCurrentFolder() {
        this.currentFolder.list("");
    }

    private interface FileSystemComponent {
        void list(String prefix);

        String getName();
    }

    public static class Folder implements FileSystemComponent {
        private String name;
        private List<FileSystemComponent> children = new ArrayList<FileSystemComponent>();

        public Folder(String name) {
            this.name = name;
        }

        public FileSystemComponent get(String name) {
            for (FileSystemComponent fileSystemComponent : children) {
                if (fileSystemComponent.getName().equals(name)) {
                    return fileSystemComponent;
                }
            }
            throw new IllegalArgumentException("Cannot find " + name);
        }

        public void add(FileSystemComponent component) {
            this.children.add(component);
        }

        public void remove(FileSystemComponent component) {
            this.children.remove(component);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void list(String prefix) {
            System.out.println(prefix + "Folder: " + this.name);
            for (FileSystemComponent fileSystemComponent : children) {
                fileSystemComponent.list(prefix + "  ");
            }
        }
    }

    public static class File implements FileSystemComponent {
        private String name;
        private String content;

        public File(String name) {
            this.name = name;
            this.content = "";
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void list(String prefix) {
            System.out.println(prefix + "File: " + this.name);
        }
    }
}
