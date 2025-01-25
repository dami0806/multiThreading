package optimization.joinThread;



import java.math.BigInteger;

/**
 * 숫자를 거듭제곱을 하는 작업은 복잡한 연산이므로, 다음 식을 실행하려 합니다.
 * <p>
 * result1 = x1 ^ y1
 * result2 = x2 ^ y2
 * <p>
 * 둘을 동시에 실행하고,
 * <p>
 * 마지막에는 result = result1 + result2
 * <p>
 */
public class CalculateThread {
    public static void main(String[] args) {

        BigInteger base1 = new BigInteger("10");
        BigInteger power1 = new BigInteger("2");
        BigInteger base2 = new BigInteger("100");
        BigInteger power2 = new BigInteger("2");

        BigInteger result = new CalculateThread().calculateResult(base1, power1, base2, power2);
        System.out.println("result = " + result);
    }

    public BigInteger calculateResult(BigInteger base1,BigInteger power1,
                                      BigInteger base2,BigInteger power2) {
        BigInteger result;
        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = thread1.getResult().add(thread2.getResult());
        return result;
    }

        private static class PowerCalculatingThread extends Thread { // 이유
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            this.result = pow(base, power);
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
        public BigInteger getResult() { return result; }
    }
}