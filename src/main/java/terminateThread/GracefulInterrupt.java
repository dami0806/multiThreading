package terminateThread;

import java.math.BigInteger;

/**
 * 개발자가 직접 인터럽트 처리 로직을 작성해야 한다.
 */
public class GracefulInterrupt {
    public static void main(String[] args) {

        // 200000^ 100000000 거듭 계산 시키 20000 * 20000 * 20000... 100000000번동안
        Thread thread = new Thread(new LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));

        thread.start();
        thread.interrupt();
    }

    private static class LongComputationTask implements Runnable {
        private BigInteger base; // 밑
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;// 1

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("계산중 중단되었습니다.");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }
}
