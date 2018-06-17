package com.fmi.rsa;

import org.apache.commons.math3.complex.Complex;

import java.awt.image.BufferedImage;

import static com.fmi.rsa.Main.coordinates;

public class PointCalculator extends Thread {

    private static final Complex E = new Complex(Math.E, 0.0);

    private int id;
    private BufferedImage bufferedImage;
    private int startRow;
    private int numberOfRows;

    public PointCalculator(int id, int startRow, int numberOfRows, BufferedImage bufferedImage) {
        this.id = id;
        this.startRow = startRow;
        this.numberOfRows = numberOfRows;
        this.bufferedImage = bufferedImage;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        if (!Main.quiet){
            System.out.printf("Thread-%d started.%n", this.id);
        }

        for (int i = startRow; i < numberOfRows * this.id; i++) {

            double py = Main.coordinates[3] - 4.0 * calculatePY(i);

            int pyScr = (int) (Math.abs((py + coordinates[2])) * (Main.dimHeight / 4.0));

            double tx = 1.0/(Main.dimWidth * 1.2);
            for (int j = 0; j < (int) (Main.dimWidth * 1.2); j++) {

                double px = coordinates[0] + 4.0 * tx;
                int pxScr = (int) ((px + coordinates[1]) * (Main.dimWidth / 4.0));

                int point = zCheck(new Complex(px, py));

                int color = PointToColor.getColorForPoint(point);
                bufferedImage.setRGB(pxScr, pyScr, color);
                tx += 1.0/(Main.dimWidth * 1.2);
            }
        }

        long endTime = System.currentTimeMillis();
        if (!Main.quiet) {
            System.out.printf("Thread-%d stopped.%n", this.id);
            System.out.printf("Thread-%d execution time was (millis): %d%n", this.id, endTime - startTime);
        }
    }

    private double calculatePY(int i) {
        return 1.0/(Main.dimHeight * 1.2) * (i+1);
    }

    private static Complex zIterate(Complex z, Complex c) {
        return (z.multiply(z).multiply(E.pow(z.multiply(z)))).add(c);
    }

    private static int zCheck(Complex c) {

        Complex zPrevious = new Complex(0.0, 0.0);
        Complex zIterable;

        int steps = 0;
        Double realPartOfZ;

        for(int i = 0; i < Main.dimWidth; i++) {

            zIterable = zIterate(zPrevious, c);
            zPrevious = zIterable;

            realPartOfZ = zPrevious.getReal();

            if (realPartOfZ.isInfinite() || realPartOfZ.isNaN()) {
                steps = i;
                break;
            }
        }

        return steps;
    }
}
