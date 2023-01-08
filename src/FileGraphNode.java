import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class FileGraphNode {
    private Integer used;
    private final Path filePath;
    private final List<FileGraphNode> childrenList;

    public FileGraphNode(String filePath) {
        this.used = 0;
        this.filePath = Path.of(filePath);
        this.childrenList = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }

    public Boolean usedEquals(Integer value) {
        return Objects.equals(used, value);
    }

    public void setUsed(Integer value) {
        used = value;
    }

    public Path getFilePath() {
        return filePath;
    }

    public List<FileGraphNode> getChildrenList() {
        return childrenList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FileGraphNode)) {
            return false;
        }
        return filePath.equals(((FileGraphNode)obj).filePath);
    }
}