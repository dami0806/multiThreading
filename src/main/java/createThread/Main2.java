package createThread;

/**
 *
 */
public class Main2 {
    public static void main(String [] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //새로운 스래드에서 실행될 코드
                throw new RuntimeException("테스트용 Exception");
            }
        });

        thread.setName("Misbehaving thread");
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("쓰레드에서 에러 발생 " + t.getName()
                        + " 에러 메시지: " + e.getMessage());
            }
        });
        thread.start();
    }
}
