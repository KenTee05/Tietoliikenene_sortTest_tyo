import java.util.Arrays;
import java.util.Random;

public class SortTest {

    static class SortAlgorithm {
        String name;
        FunctionRef function;
        boolean quadratic;

        SortAlgorithm(String name, FunctionRef function, boolean quadratic) {
            this.name = name;
            this.function = function;
            this.quadratic = quadratic;
        }
    }

    private static final int[] SIZES = {
        5_000, 100_000, 1_000_000
    };

    private static final int QUADRATIC_LIMIT = 5_000;
    private static final int RUNS = 3;

    // 1. INSERTION SORT
    public static void insertionSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int key = data[i];
            int j = i - 1;

            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }

            data[j + 1] = key;
        }
    }

    // 2. BUBBLE SORT
    public static void bubbleSort(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    // 3. SHELL SORT
    public static void shellSort(int[] data) {
        int n = data.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = data[i];
                int j = i;

                while (j >= gap && data[j - gap] > temp) {
                    data[j] = data[j - gap];
                    j -= gap;
                }

                data[j] = temp;
            }
        }
    }

    // 4. QUICK SORT
    public static void quickSort(int[] data) {
        quickSort(data, 0, data.length - 1);
    }

    private static void quickSort(int[] data, int low, int high) {
        while (low < high) {
            int mid = low + (high - low) / 2;
            int pivot = medianOfThree(data[low], data[mid], data[high]);

            int lt = low;
            int i = low;
            int gt = high;

            while (i <= gt) {
                if (data[i] < pivot) {
                    swap(data, lt++, i++);
                } else if (data[i] > pivot) {
                    swap(data, i, gt--);
                } else {
                    i++;
                }
            }

            // Rekursio pienempään osaan; suurempi osa käsitellään silmukassa.
            if (lt - low < high - gt) {
                quickSort(data, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(data, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private static int medianOfThree(int a, int b, int c) {
        if (a < b) {
            if (b < c) return b;
            return (a < c) ? c : a;
        } else {
            if (a < c) return a;
            return (b < c) ? c : b;
        }
    }

    private static void swap(int[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    // 5. MERGE SORT
    public static void mergeSort(int[] data) {
        if (data.length < 2) {
            return;
        }

        int[] temp = new int[data.length];
        mergeSort(data, temp, 0, data.length - 1);
    }

    private static void mergeSort(int[] data, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;
        mergeSort(data, temp, left, middle);
        mergeSort(data, temp, middle + 1, right);
        merge(data, temp, left, middle, right);
    }

    private static void merge(
        int[] data, int[] temp, int left, int middle, int right
    ) {
        int i = left;
        int j = middle + 1;
        int k = left;

        while (i <= middle && j <= right) {
            if (data[i] <= data[j]) {
                temp[k++] = data[i++];
            } else {
                temp[k++] = data[j++];
            }
        }

        while (i <= middle) {
            temp[k++] = data[i++];
        }

        while (j <= right) {
            temp[k++] = data[j++];
        }

        for (int x = left; x <= right; x++) {
            data[x] = temp[x];
        }
    }

    // 6. TAULUKOIDEN LUONTI
    public static int[] createAscendingTable(int n) {
        int[] data = new int[n];

        for (int i = 0; i < n; i++) {
            data[i] = i;
        }

        return data;
    }

    public static int[] createDescendingTable(int n) {
        int[] data = new int[n];

        for (int i = 0; i < n; i++) {
            data[i] = n - i - 1;
        }

        return data;
    }

    public static int[] createRandomData(int n) {
        int[] data = new int[n];
        Random gen = new Random();

        for (int i = 0; i < n; i++) {
            data[i] = gen.nextInt(n);
        }

        return data;
    }

    // Säilytetään alkuperäisen ohjelman metodinimi yhteensopivuuden vuoksi.
    public static int[] createAscendingData(int n) {
        return createAscendingTable(n);
    }

    public static void printData(int[] data) {
        for (int x : data) {
            System.out.print(x + " ");
        }
        System.out.println();
        System.out.println("--------------------------");
    }

    // 7. TULOKSEN TARKISTUS
    private static boolean isSorted(int[] data) {
        for (int i = 1; i < data.length; i++) {
            if (data[i - 1] > data[i]) {
                return false;
            }
        }
        return true;
    }

    // 8. YLEINEN SUORITUSAJAN MITTAUS
    public static long SortMethodTester(FunctionRef sortMethod, int[] original) {
        long[] times = new long[RUNS];

        for (int i = 0; i < RUNS; i++) {
            // Kopiointi tehdään ennen ajanottoa.
            int[] copy = original.clone();

            long start = System.nanoTime();
            sortMethod.sort(copy);
            long stop = System.nanoTime();

            times[i] = stop - start;

            if (!isSorted(copy)) {
                throw new IllegalStateException(
                    "Taulukko ei järjestynyt oikein!"
                );
            }
        }

        Arrays.sort(times);
        return times[RUNS / 2]; // Kolmen mittauksen mediaani.
    }

    // 9. TESTAUKSEN SUORITTAMINEN
    private static void runTest(
        String dataType, int[] original, SortAlgorithm[] algorithms
    ) {
        System.out.println("\n" + dataType + " | N = " + original.length);
        System.out.printf("%-18s %16s %16s%n",
            "Algoritmi", "Aika (ms)", "Aika (ns)");
        System.out.println(
            "------------------------------------------------------"
        );

        for (SortAlgorithm algorithm : algorithms) {
            if (algorithm.quadratic && original.length > QUADRATIC_LIMIT) {
                System.out.printf("%-18s %16s %16s%n",
                    algorithm.name, "OHITETTU", "N liian suuri");
                continue;
            }

            long nanos = SortMethodTester(algorithm.function, original);
            double millis = nanos / 1_000_000.0;

            System.out.printf("%-18s %16.3f %16d%n",
                algorithm.name, millis, nanos);
        }
    }

    // 10. PÄÄOHJELMA
    public static void main(String[] args) {
        // Metodiviitteet välittävät algoritmit SortMethodTester-metodille.
        SortAlgorithm[] algorithms = {
            new SortAlgorithm("Insertion Sort",
                SortTest::insertionSort, true),
            new SortAlgorithm("Bubble Sort",
                SortTest::bubbleSort, true),
            new SortAlgorithm("Arrays.sort",
                Arrays::sort, false),
            new SortAlgorithm("Shell Sort",
                SortTest::shellSort, false),
            new SortAlgorithm("Quick Sort",
                SortTest::quickSort, false),
            new SortAlgorithm("Merge Sort",
                SortTest::mergeSort, false)
        };

        System.out.println("SORTTAUSALGORITMIEN SUORITUSAIKAVERTAILU");
        System.out.println("Jokaisesta mittauksesta näytetään "
            + RUNS + " ajon mediaani.");
        System.out.println("Insertion- ja Bubble Sort ohitetaan, kun N > "
            + QUADRATIC_LIMIT + ".");

        for (int size : SIZES) {
            runTest("Satunnainen taulukko",
                createRandomData(size), algorithms);

            runTest("Nouseva taulukko",
                createAscendingTable(size), algorithms);

            runTest("Laskeva taulukko",
                createDescendingTable(size), algorithms);
        }
    }
}
