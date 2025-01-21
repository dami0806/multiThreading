package terminateThread;

import java.math.BigInteger;

public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {

        // 200000^ 100000000 거듭 계산 시키 20000 * 20000 * 20000... 100000000번동안
        Thread thread = new Thread(new DaemonThread.LongComputationTask(new BigInteger("200000"), new BigInteger("100000000")));
        thread.setDaemon(true);
        thread.start();
        //Thread.sleep(100);
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
                result = result.multiply(base); // 안전한 중단 필요 없을때 -> set
            }
            return result;
        }
    }
}
