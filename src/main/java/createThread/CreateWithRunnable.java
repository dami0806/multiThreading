package createThread;

/**
 * Runnable 구현 방식: "스레드와 실행 코드를 각각 다른 사람(클래스)에 맡기는 방식"
 */
public class CreateWithRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Thread 실행 중: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        CreateWithRunnable runnable = new CreateWithRunnable(); // Runnable 구현 객체 생성
        Thread thread = new Thread(runnable); // Thread 생성자에 전달
        thread.start();
    }
}
