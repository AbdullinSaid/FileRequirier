import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Class for analyzing files and building graph by them
 */
public class FilesAnalyzer {
    private final Map<String, FileGraphNode> graphNodeMap;

    public FilesAnalyzer() {
        this.graphNodeMap = new HashMap<>();
    }

    /**
     * Add file to graph
     * @param path path to file
     */
    public void recursiveBuild(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            throw new RuntimeException("Incorrect path " + dir.getPath());
        }
        if (dir.isFile()) {
            FileGraphNode node;
            if (!graphNodeMap.containsKey(dir.getPath())) {
                node = new FileGraphNode(dir.getPath());
                graphNodeMap.put(dir.getPath(), node);
            } else {
                node = graphNodeMap.get(dir.getPath());
            }
            analyzeFile(node);
        } else {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                recursiveBuild(file.getPath());
            }
        }
    }

    /**
     * Analyze file content and adding new edges to graph
     * @param node graph node to analyze
     */
    private void analyzeFile(FileGraphNode node) {
        try (BufferedReader br = new BufferedReader(new FileReader(node.getFilePath().toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (Pattern.matches("require '.+'", line)) {
                    String parentPath = line.substring(9, line.length() - 1);
                    if (Path.of(parentPath).toFile().exists()) {
                        if (!graphNodeMap.containsKey(parentPath)) {
                            graphNodeMap.put(parentPath, new FileGraphNode(parentPath));
                        }
                        graphNodeMap.get(parentPath).getChildrenList().add(node);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Exception occurred while reading file {}", ex);
        }
    }

    public Collection<FileGraphNode> getValues() {
        return graphNodeMap.values();
    }
}
