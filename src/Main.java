import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int testCases = input.nextInt();

        while (testCases-- > 0) {
            int rows = input.nextInt();
            int cols = input.nextInt();

            int[][] labyrinth = readLabyrinth(rows, cols, input);

            Dijkstra shortestPath = new Dijkstra(labyrinth);

            int result = (int) shortestPath.getDistance(rows * cols - 1) + labyrinth[0][0];
            System.out.println(result);
        }
    }

    private static int[][] readLabyrinth(int rows, int cols, Scanner input) {
        int[][] labyrinth = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                labyrinth[i][j] = input.nextInt();
            }
        }

        return labyrinth;
    }
}

class Dijkstra {

    private final double[] distance;
    private final IndexMinPQ<Double> priorityQueue;

    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};

    public Dijkstra(int[][] labyrinth) {

        int rows = labyrinth.length;
        int cols = labyrinth[0].length;
        int totalVertices = rows * cols;

        distance = new double[totalVertices];
        priorityQueue = new IndexMinPQ<>(totalVertices);

        initializeDistances(totalVertices);

        distance[0] = 0.0;
        priorityQueue.insert(0, distance[0]);

        while (!priorityQueue.isEmpty()) {

            int currentVertex = priorityQueue.delMin();

            int currentRow = currentVertex / cols;
            int currentCol = currentVertex % cols;

            exploreNeighbors(
                    currentRow,
                    currentCol,
                    currentVertex,
                    rows,
                    cols,
                    labyrinth
            );
        }
    }

    private void initializeDistances(int totalVertices) {
        for (int vertex = 0; vertex < totalVertices; vertex++) {
            distance[vertex] = Double.POSITIVE_INFINITY;
        }
    }

    private void exploreNeighbors(
            int row,
            int col,
            int currentVertex,
            int totalRows,
            int totalCols,
            int[][] labyrinth
    ) {

        for (int direction = 0; direction < 4; direction++) {

            int nextRow = row + DX[direction];
            int nextCol = col + DY[direction];

            if (!isValidPosition(nextRow, nextCol, totalRows, totalCols)) {
                continue;
            }

            int nextVertex = nextRow * totalCols + nextCol;
            double weight = labyrinth[nextRow][nextCol];

            relax(currentVertex, nextVertex, weight);
        }
    }

    private boolean isValidPosition(
            int row,
            int col,
            int totalRows,
            int totalCols
    ) {

        return row >= 0
                && row < totalRows
                && col >= 0
                && col < totalCols;
    }

    private void relax(int from, int to, double weight) {

        if (distance[to] > distance[from] + weight) {

            distance[to] = distance[from] + weight;

            if (priorityQueue.contains(to)) {
                priorityQueue.decreaseKey(to, distance[to]);
            } else {
                priorityQueue.insert(to, distance[to]);
            }
        }
    }

    public double getDistance(int vertex) {
        return distance[vertex];
    }
}

class IndexMinPQ<Key extends Comparable<Key>> {

    private int size;

    private final int[] priorityQueue;
    private final int[] queuePosition;
    private final Key[] keys;

    @SuppressWarnings("unchecked")
    public IndexMinPQ(int maxSize) {

        keys = (Key[]) new Comparable[maxSize + 1];
        priorityQueue = new int[maxSize + 1];
        queuePosition = new int[maxSize + 1];

        for (int i = 0; i <= maxSize; i++) {
            queuePosition[i] = -1;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(int index) {
        return queuePosition[index] != -1;
    }

    public void insert(int index, Key key) {

        size++;

        queuePosition[index] = size;
        priorityQueue[size] = index;
        keys[index] = key;

        swim(size);
    }

    public int delMin() {

        int min = priorityQueue[1];

        exchange(1, size--);

        sink(1);

        queuePosition[min] = -1;
        keys[min] = null;

        return min;
    }

    public void decreaseKey(int index, Key key) {

        keys[index] = key;

        swim(queuePosition[index]);
    }

    private boolean greater(int i, int j) {

        return keys[priorityQueue[i]]
                .compareTo(keys[priorityQueue[j]]) > 0;
    }

    private void exchange(int i, int j) {

        int temp = priorityQueue[i];

        priorityQueue[i] = priorityQueue[j];
        priorityQueue[j] = temp;

        queuePosition[priorityQueue[i]] = i;
        queuePosition[priorityQueue[j]] = j;
    }

    private void swim(int position) {

        while (position > 1 && greater(position / 2, position)) {

            exchange(position, position / 2);

            position /= 2;
        }
    }

    private void sink(int position) {

        while (2 * position <= size) {

            int child = 2 * position;

            if (child < size && greater(child, child + 1)) {
                child++;
            }

            if (!greater(position, child)) {
                break;
            }

            exchange(position, child);

            position = child;
        }
    }
}