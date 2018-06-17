package com.fmi.rsa;

public class PointToColor {
    public static int getColorForPoint(int point) {
        int color;
        if (point == 0) {
            // inside ...
            color = 0x00ff00;
        } else if (point <= 10) {
            // outside ... (rapid move)
            color = 0xFFFFFF;
        } else if (point == 11) {
            color = 0x0000ff;
        } else if (point == 12) {
            color = 0x0000ee;
        } else if (point == 13) {
            color = 0x0000dd;
        } else if (point == 14) {
            color = 0x0000cc;
        } else if (point == 15) {
            color = 0x0000bb;
        } else if (point == 16) {
            color = 0x0000aa;
        } else if (point == 17) {
            color = 0x000099;
        } else if (point == 18) {
            color = 0x000088;
        } else if (point == 19) {
            color = 0x000077;
        } else if (point == 20) {
            color = 0x000066;
        } else if (point <= 30) {
            color = 0x666600;
        } else if (point <= 40) {
            color = 0x777700;
        } else if (point <= 50) {
            color = 0x888800;
        } else if (point <= 100) {
            color = 0x999900;
        } else if (point <= 150) {
            color = 0xaaaa00;
        } else if (point <= 200) {
            color = 0xbbbb00;
        } else if (point <= 250) {
            color = 0xcccc00;
        } else if (point <= 350) {
            color = 0xdddd00;
        } else {
            color = 0xeeee00;
        }
        return color;
    }
}
