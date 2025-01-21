package terminateThread;

public class BlokingThread {
    public static void main(String[] args) {
        Thread thread = new Thread(new BlockingTask());
        thread.start();
        thread.interrupt(); // thread를 INTERRUPT
    }
    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try{
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("bloking thread가 실행중이였습니다.");
            }
        }
    }
}
