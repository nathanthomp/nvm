import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    private final Folder rootFolder;
    private Folder currentFolder;

    public FileSystem() {
        Folder rootFolder = new Folder("root", null);
        Folder userFolder = new Folder("user", rootFolder);
        rootFolder.add(userFolder);
        Folder nathanFolder = new Folder("nathan", userFolder);
        userFolder.add(nathanFolder);
        Folder nicoleFolder = new Folder("nicole", userFolder);
        userFolder.add(nicoleFolder);
        this.rootFolder = rootFolder;
        this.currentFolder = rootFolder;
    }

    public Folder getRootFolder() {
        return this.rootFolder;
    }

    public Folder getCurrentFolder() {
        return this.currentFolder;
    }

    public void setCurrentFolder(Folder folder) {
        this.currentFolder = folder;
    }

    // Probably remove this
    public void changeCurrentFolder(String name) {
        if (name.equals("..")) {
            if (this.currentFolder.parent != null) {
                this.currentFolder = this.currentFolder.parent;
                return;
            }
            throw new IllegalArgumentException("Already at root");
        }
        Component component = this.currentFolder.get(name);
        if (component instanceof File) {
            throw new IllegalArgumentException(name + " is not a folder");
        }
        Folder folder = (Folder) component;
        if (!folder.canExecute) {
            throw new SecurityException("Insufficient execute permissions");
        }
        this.currentFolder = folder;
    }

    public static abstract class Component {
        protected String name;
        protected boolean canRead = true;
        protected boolean canWrite = true;
        protected boolean canExecute = true;

        protected final Folder parent;

        public Component(String name, Folder parent) {
            this.name = name;
            this.parent = parent;
        }

        public abstract void list(String prefix);

        public abstract void accept(FileSystemVisitor visitor);

        /**
         * TODO: boolean isFolder and boolean isFile
         */

        public String getName() {
            return this.name;
        }

        private String getPermissions() {
            String permissions = "";
            permissions += this.canRead ? "r" : "-";
            permissions += this.canWrite ? "w" : "-";
            permissions += this.canExecute ? "x" : "-";
            return permissions;
        }

        public void setPermissions(boolean canRead, boolean canWrite, boolean canExecute) {
            this.canRead = canRead;
            this.canWrite = canWrite;
            this.canExecute = canExecute;
        }

        public String getPath() {
            if (this.parent == null) {
                return this.name;
            }
            return this.parent.getPath() + "/" + this.name;
        }
    }

    public static class Folder extends Component {
        private List<Component> children = new ArrayList<Component>();

        public Folder(String name, Folder parent) {
            super(name, parent);
        }

        // This can all be in get method
        public Component resolve(String path) {
            if (path.isEmpty()) {
                return this;
            }
            String[] paths = path.split("/");
            if (paths[0].equals("..")) {
                if (this.parent == null) {
                    throw new IllegalArgumentException("Already at root");
                }
                return this.parent;
            }
            Component component = this.get(paths[0]);
            if (component instanceof File) {
                return component;
            }
            Folder folder = (Folder) component;
            String newPath = "";
            for (int i = 1; i < paths.length; i++) {
                newPath += paths[i] + "/";
            }
            return folder.resolve(newPath);
        }

        public Component get(String name) {
            if (!super.canRead) {
                throw new SecurityException("Insufficient read permissions");
            }
            for (Component component : children) {
                if (component.getName().equals(name)) {
                    return component;
                }
            }
            throw new IllegalArgumentException("Cannot find " + name);
        }

        public void add(Component component) {
            if (!super.canWrite) {
                throw new SecurityException("Insufficient write permissions");
            }
            this.children.add(component);
        }

        public void remove(Component component) {
            if (!super.canWrite) {
                throw new SecurityException("Insufficient write permissions");
            }
            this.children.remove(component);
        }

        @Override
        public void list(String prefix) {
            if (!super.canRead) {
                throw new SecurityException("Insufficient read permissions");
            }
            System.out.println(prefix + "Folder: (" + super.getPermissions() + ") " + super.name);
            for (Component component : children) {
                component.list(prefix + "  ");
            }
        }

        @Override
        public void accept(FileSystemVisitor visitor) {
            visitor.visit(this);
            for (Component child : children) {
                child.accept(visitor);
            }
        }
    }

    public static class File extends Component {
        private String content;

        public File(String name, Folder parent) {
            super(name, parent);
            this.content = "";
        }

        public String getContent() {
            if (!super.canRead) {
                throw new SecurityException("Insufficient read permissions");
            }
            return this.content;
        }

        public void setContent(String content) {
            if (!super.canWrite) {
                throw new SecurityException("Insufficient write permissions");
            }
            this.content = content;
        }

        /**
         * Get size
         */

        @Override
        public void list(String prefix) {
            System.out.println(prefix + "File: (" + super.getPermissions() + ") " + super.name);
        }

        @Override
        public void accept(FileSystemVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static interface FileSystemVisitor {
        void visit(File file);

        void visit(Folder folder);
    }

    // public static class SearchVisitor implements FileSystemVisitor {
    // private final String name;
    // private FileSystemComponent result;

    // public SearchVisitor(String name) {
    // this.name = name;
    // }

    // public FileSystemComponent getResult() {
    // return this.result;
    // }

    // @Override
    // public void visit(File file) {
    // if (file.name.equals(this.name)) {
    // this.result = file;
    // }
    // }

    // @Override
    // public void visit(Folder folder) {
    // if (folder.name.equals(this.name)) {
    // this.result = folder;
    // }
    // }
    // }

    // PathVisitor: Gets the path for a name
    // SizeVisitor
    // NavigateVisitor
    // PrintVisitor (replaces list)
    /**
     * Use the visitor to be able to do things like "from root, navigate to this
     * user/nathan" or
     */
}
