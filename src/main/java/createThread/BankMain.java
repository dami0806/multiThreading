package createThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class BankMain {
    public static final int MAX_PASSWORD = 990;

    public static void main(String[] args) {
        Random random = new Random();

        // 은행 계좌 생성 (랜덤 비밀번호 설정)
        BankAccount account = new BankAccount(random.nextInt(MAX_PASSWORD));

        // Runnable 작업 리스트 생성
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(new Hacker1(account));
        tasks.add(new Hacker2(account));
        tasks.add(new PoliceThread());

        // 1. 직접 Thread 생성하여 실행
//        System.out.println("\n[직접 Thread 실행 방식]");
//        new BankMain().executeAllWithThread(tasks);

        // 2. ExecutorService로 실행
        System.out.println("\n[ExecutorService 실행 방식]");
        new BankMain().executeAllWithExecutor(tasks);
    }

    // 직접 스레드 생성 방식으로 작업 실행
    public void executeAllWithThread(List<Runnable> tasks) {
        List<Thread> threads = new ArrayList<>();
        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    // ExecutorService를 활용한 작업 실행
    public void executeAllWithExecutor(List<Runnable> tasks) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (Runnable task : tasks) {
            executor.submit(task);
        }
        executor.shutdown();
    }

    public static class BankAccount {
        private int password;

        public BankAccount(int password) {
            this.password = password;
        }

        public boolean isCorrectPw(int guess) {
            try {
                Thread.sleep(5); // 추측 속도 제한
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return this.password == guess;
        }
    }

    // 제너릭 해커 스레드
    private static abstract class HackerTread extends Thread {
        protected BankAccount account;

        public HackerTread(BankAccount account) {
            this.account = account;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("쓰레드 시작: " + this.getName());
            super.start();
        }
    }

    /**
     * 0부터 비밀번호 탐색 해커
     */
    private static class Hacker1 extends HackerTread {
        public Hacker1(BankAccount account) {
            super(account);
        }

        @Override
        public void run() {
            for (int guess = 0; guess <= MAX_PASSWORD; guess++) {
                if (account.isCorrectPw(guess)) {
                    System.out.println(this.getName() + "가 금고를 열었다 [비밀번호]: " + guess);
                    System.exit(0);
                }
            }
        }
    }

    /**
     * MAX부터 비밀번호 탐색 해커
     */
    private static class Hacker2 extends HackerTread {
        public Hacker2(BankAccount account) {
            super(account);
        }

        @Override
        public void run() {
            for (int guess = MAX_PASSWORD; guess >= 0; guess--) {
                if (account.isCorrectPw(guess)) {
                    System.out.println(this.getName() + "가 금고를 열었다 [비밀번호]: " + guess);
                    System.exit(0);
                }
            }
        }
    }

    // 경찰 스레드
    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000); // 1초 대기
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(i + "초 남았습니다.");
            }
            System.out.println("Game over. 경찰이 도착했습니다.");
            System.exit(0);
        }
    }
}