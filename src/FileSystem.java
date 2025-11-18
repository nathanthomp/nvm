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

    public class Folder implements FileSystemComponent {
        private String name;
        private List<FileSystemComponent> children = new ArrayList<FileSystemComponent>();

        private Folder(String name) {
            this.name = name;
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

        /**
         * Recommended to only have an add and remove method, making use of the FileSystemComponent.
         */

        public void addFolder(String name) {
            this.children.add(new Folder(name));
        }

        public void addFile(String name) {
            this.children.add(new File(name));
        }
    }

    private class File implements FileSystemComponent {
        private String name;

        private File(String name) {
            this.name = name;
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
