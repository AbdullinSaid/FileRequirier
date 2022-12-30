import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphOperations {
    private static HashMap<FileGraphNode, FileGraphNode> parent;
    private static FileGraphNode cycleStart, cycleEnd;

    private static boolean dfsCheckCycles(FileGraphNode node) {
        node.used = 1;
        for (FileGraphNode to : node.childrenList) {
            if (to.filePath.toFile().exists()) {
                if (to.used == 1) {
                    cycleEnd = node;
                    cycleStart = to;
                    return true;
                } else if (to.used == 0) {
                    parent.put(node, to);
                    return dfsCheckCycles(to);
                }
            }
        }
        node.used = 2;
        return false;
    }

    public static boolean checkCycles(Collection<FileGraphNode> graph) {
        for (FileGraphNode node : graph) {
            node.used = 0;
        }
        parent = new HashMap<>();
        cycleStart = null;
        cycleEnd = null;
        for (FileGraphNode node : graph) {
            if (dfsCheckCycles(node)) {
                break;
            }
        }

        if (cycleStart == null) {
            return false;
        } else {
            System.out.println("Found cycle:");
            System.out.print(cycleStart.filePath + " ");
            while (cycleStart != cycleEnd) {
                cycleStart = parent.get(cycleStart);
                System.out.print(cycleStart.filePath + " ");
            }
            System.out.println();
            return true;
        }
    }

    private static void dfsTopologicalSort(FileGraphNode node, List<FileGraphNode> answer) {
        node.used = 1;
        for (FileGraphNode to : node.childrenList) {
            if (to.used == 0) {
                dfsTopologicalSort(to, answer);
            }
        }
        answer.add(node);
    }

    public static void printByTopologicalSorted(Collection<FileGraphNode> graph) {
        if (checkCycles(graph)) {
            return;
        }
        for (FileGraphNode node : graph) {
            node.used = 0;
        }
        List<FileGraphNode> answer = new ArrayList<>();
        for (FileGraphNode node : graph) {
            if (node.used == 0) {
                dfsTopologicalSort(node, answer);
            }
        }
        Collections.reverse(answer);
        for (FileGraphNode node : answer) {
            try (BufferedReader br = new BufferedReader(new FileReader(node.filePath.toFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                System.out.println("Exception occurred while printing file " + ex.getMessage());
                break;
            }
        }
    }
}
