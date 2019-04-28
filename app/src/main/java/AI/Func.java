package AI;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Func {
    private Context mContext;
    public Func() throws IOException {
        Scanner scan = new Scanner(new File("in2.txt"));

        //y = bx
        double tempx = scan.nextDouble();
        double tempy = scan.nextDouble();
        double b = tempy / tempx;
        int count = 0;

        while (scan.hasNextDouble()) {
            count++;
            double x = scan.nextDouble();
            double y = scan.nextDouble();
            b = ((b) + (y / x * count)) / count;
        }
    }
}
