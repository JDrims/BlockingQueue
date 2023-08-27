import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    private static ArrayBlockingQueue<String> listA = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> listB = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> listC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        Thread threadPutList = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    listA.put(generateText("abc", 100_000));
                    listB.put(generateText("abc", 100_000));
                    listC.put(generateText("abc", 100_000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread threadListA = new Thread(() -> {
            int maxCount = 0;
            for (String str : listA) {
                int count = maxCountChar(str, 'a');
                if (count > maxCount) {
                    maxCount = count;
                }
                try {
                    listA.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное символов \"а\" в строке : " + maxCount);
        });
        Thread threadListB = new Thread(() -> {
            int maxCount = 0;
            for (String str : listB) {
                int count = maxCountChar(str, 'b');
                if (count > maxCount) {
                    maxCount = count;
                }
                try {
                    listB.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное символов \"b\" в строке : " + maxCount);
        });
        Thread threadListC = new Thread(() -> {
            int maxCount = 0;
            for (String str : listC) {
                int count = maxCountChar(str, 'c');
                if (count > maxCount) {
                    maxCount = count;
                }
                try {
                    listC.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное символов \"c\" в строке : " + maxCount);
        });
        threadPutList.start();
        threadPutList.join();
        threadListA.start();
        threadListB.start();
        threadListC.start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxCountChar(String str, char chr) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == chr) {
                count++;
            }
        }
        return count;
    }
}