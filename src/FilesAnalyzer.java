import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class FilesAnalyzer {
    HashMap<String, FileGraphNode> graphNodeMap;

    public FilesAnalyzer() {
        graphNodeMap = new HashMap<>();
    }

    public void recursiveBuild(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            System.out.println("Incorrect path " + dir.getPath());
            return;
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

    public void analyzeFile(FileGraphNode node) {
        try (BufferedReader br = new BufferedReader(new FileReader(node.filePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (Pattern.matches("require '.+'", line)) {
                    String parentPath = line.substring(9, line.length() - 1);
                    if (Path.of(parentPath).toFile().exists()) {
                        if (!graphNodeMap.containsKey(parentPath)) {
                            graphNodeMap.put(parentPath, new FileGraphNode(parentPath));
                        }
                        graphNodeMap.get(parentPath).childrenList.add(node);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Exception occurred while reading file " + ex.getMessage());
        }
    }

}
