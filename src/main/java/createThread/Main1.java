package createThread;

public class Main1 {
    public static void main(String[] args) {

        // 새 스레드 생성
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Code that will run in  a new thread
                System.out.println("현재 진행 중인 쓰레드:" + Thread.currentThread().getName());
                System.out.println("현재 쓰레드 priority :" + Thread.currentThread().getPriority());
            }
        });

        thread.setName("새 작업자 쓰레드");
        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("새 쓰레드 시작 전 - 현재 쓰레드:" + Thread.currentThread().getName());
        thread.start(); // 실행 가능한 상태(RUNNABLE) * 하지만 실행 순서는 CPU 스케줄러에 의해 결정 *
        System.out.println("새 쓰레드 시작 후 - 현재 쓰레드:" + Thread.currentThread().getName());
    }
}
