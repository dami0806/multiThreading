package joinThread;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinThread {
    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(1000000L, 3435L, 35435L, 2324L, 4656L, 23L, 5556L);

        List<FactorialThread> threads = new ArrayList<>();

        for (long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }

        // main,과 나머지 thread들 동시 실행
        // 작업 스레드들이 얼마나 걸릴지는 계산할 숫자의 크기와 스레드 스케줄링에 따라 다르다
        for (Thread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }


        for (Thread thread : threads) {
            thread.join(2000L);
        }

        // 결과 가져오기
        for (int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);

            if (factorialThread.isFinished()) {
                System.out.println(inputNumbers.get(i) + "의 팩토리얼값 :" + factorialThread.getResult());
            } else {
                System.out.println(inputNumbers.get(i) + " 의 팩토리얼 계산은 진행중입니다.");
            }
        }
    }

    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger((Long.toString(i))));
            }
            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }
    }
}
