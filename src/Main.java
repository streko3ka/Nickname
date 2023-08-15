import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static volatile int var3 = 0;
    public static volatile int var4 = 0;
    public static volatile int var5 = 0;

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        CountName countName1 = new CountName();
        CountName countName2 = new CountName();
        CountName countName3 = new CountName();

        int length3 = 3;
        Thread thread1 = new Thread(() -> countName1.niceNames(texts, length3));
        thread1.start();

        int length4 = 4;
        Thread thread2 = new Thread(() -> countName2.niceNames(texts, length4));
        thread2.start();

        int length5 = 5;
        Thread thread3 = new Thread(() -> countName3.niceNames(texts, length5));
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        var3 = countName1.getWords();
        var4 = countName2.getWords();
        var5 = countName3.getWords();

        System.out.format("Красивых слов с длиной 3: %d шт\n" +
                        "Красивых слов с длиной 4: %d шт\n" +
                        "Красивых слов с длиной 5: %d шт",
                var3,
                var4,
                var5);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

class CountName {
    AtomicInteger countName = new AtomicInteger(0);

    public void niceNames(String[] texts, int length) {
        for (String name : texts) {
            if (name.length() == length) {
                if (name.chars().distinct().count() == 1) {
                    countName.incrementAndGet();
                } else if (name.charAt(0) == name.charAt(length - 1)) {
                    for (int j = 0; j < (name.length() / 2); j++) {
                        if (name.charAt(j) != name.charAt(name.length() - j - 1)) {
                            countName.incrementAndGet();
                        }
                    }
                } else {
                    for (int i = 1; i < name.length(); i++) {
                        if (name.charAt(i) == name.charAt(i - 1) || name.charAt(i) < name.charAt(i - 1)) {
                            countName.incrementAndGet();
                        }
                    }
                }
            }
        }
    }

    public int getWords() {
        return countName.get();
    }
}