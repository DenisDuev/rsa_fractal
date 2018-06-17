package com.fmi.rsa;

import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static int dimWidth = 640;
    public static int dimHeight = 480;
    private static int numberOfTasks = 1;
    public static double[] coordinates = {-2.0, 2.0, -2.0, 2.0};
    private static String outputFileName = "zad20.png";
    public static boolean quiet = false;

    public static void main(String[] args) throws ParseException {
        long startTime = System.currentTimeMillis();
        parseConsoleInput(args);

        BufferedImage bufferedImage = new BufferedImage(dimWidth + 1, dimHeight + 1, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, dimWidth + 1, dimHeight + 1);

        List<PointCalculator> pointCalculators = new ArrayList<PointCalculator>(numberOfTasks);

        final int numOfRows = ((int) (dimHeight * 1.2));
        final int numOfRowsForThread = numOfRows / numberOfTasks;
        final int remainingRows = numOfRows % numberOfTasks;

        //if the py is not divided equally
        int index = 1, start = 0;
        for (; index <= remainingRows; index++) {
            PointCalculator point = new PointCalculator(index, start, numOfRowsForThread + 1, bufferedImage);
            pointCalculators.add(point);
            point.start();
            start += numOfRowsForThread + 1;
        }

        for (; index <= numberOfTasks; index++) {
            PointCalculator point = new PointCalculator(index, start, numOfRowsForThread, bufferedImage);
            pointCalculators.add(point);
            point.start();
            start += numOfRowsForThread;
        }
        try {
            for (PointCalculator p : pointCalculators) {
                p.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        g2d.setColor(Color.GRAY);
        g2d.drawRect(0, 0, dimWidth - 2, dimHeight - 2);

        try {
            ImageIO.write(bufferedImage, "PNG", new File(outputFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        if (quiet){
            System.out.println(endTime - startTime);
        } else {
            System.out.printf("Threads used in current run: %d%n", numberOfTasks);
            System.out.printf("Total execution time for current run (millis): %d%n", endTime - startTime);
        }

    }


    private static void parseConsoleInput(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("s", true, "dimension ex. 640x480");
        options.addOption("r", true, "rect ex. -2.0:2.0:-1.0:1.0");
        options.addOption("t", true, "number of tasks");
        options.addOption("o", true, "output file");
        options.addOption("q", false, "quiet running of program, without additional logs");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("s")) {
            String[] dims = cmd.getOptionValue("s").split("x");
            dimWidth = Integer.parseInt(dims[0]);
            dimHeight = Integer.parseInt(dims[1]);
        }


        if (cmd.hasOption("r")) {
            String[] rect = cmd.getOptionValue("r").split(":");
            for (int i = 0; i < rect.length; i++) {
                coordinates[i] = Double.parseDouble(rect[i]);
            }
        }

        if (cmd.hasOption("t")) {
            numberOfTasks = Integer.parseInt(cmd.getOptionValue("t"));
        }

        if (cmd.hasOption("o")) {
            outputFileName = cmd.getOptionValue("o");
        }

        if (cmd.hasOption("q")) {
            quiet = true;
        }
    }


}
