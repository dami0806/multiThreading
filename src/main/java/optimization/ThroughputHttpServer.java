package optimization;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    public static final String SOURCE_FILE = "./resources/cosmos.jpg";
    public static final String DESTINATION_FILE = "./out/cosmos.jpg";

    public static void main(String[] args) throws IOException {

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        //long startTime = System.currentTimeMillis();
        //recolorSingleThreaded(originalImage, resultImage);
//        int numberOfThreads = 1;
//        recolorMultithreaded(originalImage, resultImage, numberOfThreads);
//        long endTime = System.currentTimeMillis();
//
//        long duration = endTime - startTime;

//        File outputFile = new File(DESTINATION_FILE);
//        ImageIO.write(resultImage, "jpg", outputFile);
        int[] threadCounts = {1, 2, 4, 6, 8, 10, 12};

        for (int threads : threadCounts) {
            System.out.println("스레드 수: " + threads);
            runTest(originalImage, threads);
        }
        // System.out.println(String.valueOf(duration));
    }
    public static void runTest(BufferedImage originalImage, int numberOfThreads) throws IOException {
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();

        // 멀티스레드 처리
        if (numberOfThreads == 1) {
            recolorSingleThreaded(originalImage, resultImage);
        } else {
            recolorMultithreaded(originalImage, resultImage, numberOfThreads);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("처리 시간: " + (endTime - startTime) + "ms");

        // 출력 파일 저장 (선택적)
        String outputFile = DESTINATION_FILE.replace(".jpg", "_" + numberOfThreads + "threads.jpg");
        ImageIO.write(resultImage, "jpg", new File(outputFile));
    }

    public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for(int i = 0; i < numberOfThreads ; i++) {
            final int threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int xOrigin = 0 ;
                int yOrigin = height * threadMultiplier;

                recolorImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
            });

            threads.add(thread);
        }

        for(Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner,
                                    int width, int height) {
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < originalImage.getHeight() ; y++) {
                recolorPixel(originalImage, resultImage, x , y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if(isShadeOfGray(red, green, blue)) {
            // 회색이 많이 도는 흰색 꽃-> 보라색
            newRed = clamp(red + 10, 0, 255); //Math.min(255, red + 10);
            newGreen = clamp(green - 80, 0, 255); // Math.max(0, green - 80);
            newBlue = clamp(blue - 20, 0, 255); //Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }
        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }
    // 범위 제한 두기
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    //회색은 RGB 색상 공간에서 빨강, 초록, 파랑 값이 거의 동일할 때
    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs( green - blue) < 30;
    }

    //  색 조합 하기
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    // RGB 시프트로 10진수 숫자 얻기
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
