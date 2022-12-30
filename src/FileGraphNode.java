import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class FileGraphNode {
    int used;
    Path filePath;
    List<FileGraphNode> childrenList;

    public FileGraphNode(String filePath) {
        used = 0;
        this.filePath = Path.of(filePath);
        childrenList = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FileGraphNode)) {
            return false;
        }
        return filePath.equals(((FileGraphNode)obj).filePath);
    }
}