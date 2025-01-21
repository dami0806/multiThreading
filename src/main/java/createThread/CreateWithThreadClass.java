package createThread;


/**
 * Thread 상속 방식: "스레드와 실행 코드를 한 사람(클래스)에게 모두 맡기는 방식
 */
public class CreateWithThreadClass extends Thread {
    @Override
    public void run() {
        System.out.println("Thread 실행 중: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        CreateWithThreadClass thread = new CreateWithThreadClass(); // 새로운 Thread 객체 생성
        thread.start(); // 스레드 실행
    }
}
