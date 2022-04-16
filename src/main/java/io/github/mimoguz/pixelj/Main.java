package io.github.mimoguz.pixelj;

import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        final var test = new Test();
        test.run();
        test.setValue(20);
        test.run();
    }

    public static class Test {
        private final Consumer<Integer> consumer;
        private int value = 10;

        public Test() {
            // Is "value" captured by reference or by value?
            consumer = i -> System.out.println(10 * value);
        }

        public int getValue() {
            return value;
        }

        public void setValue(final int value) {
            this.value = value;
        }

        public void run() {
            consumer.accept(value);
        }
    }
}
