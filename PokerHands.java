package xjtu.zyh.task3;

import java.io.BufferedReader;
import java.io.InputStreamReader;


//终稿，为第三次提交

public class PokerHands {

    /**
     * 主函入口
     * @param args
     */
    public static void main(String[] args) {
        char[][] white = new char[5][3];
        char[][] black = new char[5][3];
        //String s;

        System.out.println("请输入黑白两方牌:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String s1 = in.readLine();
            String s2 = s1.replaceAll(" ", "");
            char[] chars = s2.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                System.out.println(chars[i]);
            }

            System.out.println("填充后结果");

            int count = 0;
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 2; k++) {
                    white[j][k] = chars[count];
                    black[j][k] = chars[count + 10];
                    count++;
                }
            }

			//此处为调试信息
            //System.out.println();
            System.out.println("white:");
            showArrays(white);
            System.out.println();
            System.out.println("black:");
            showArrays(black);
            System.out.println();
			
			
			//结果输出
            int value = tests(black) - tests(white);
            if (value < 0)
                System.out.println("Black wins.");
            else if (value > 0)
                System.out.println("White wins.");
            else
                System.out.println("Tie.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int value(char ch) {
        if (ch == 'T') return 8;
        if (ch == 'J') return 9;
        if (ch == 'Q') return 10;
        if (ch == 'K') return 11;
        if (ch == 'A') return 12;
        return ch - '2';
    }

    public static int color(char ch) {
        /*
            方片 D
            黑桃 S
            红桃 H
            梅花 C
        */
        if (ch == 'S') return 0;
        if (ch == 'H') return 1;
        if (ch == 'D') return 2;
        if (ch == 'C') return 3;
        return 0;
    }

    public static void showArrays(char[][] myArray) {
        for (char[] cells : myArray) {
            for (char cell : cells) {
                System.out.print(cell + "");
            }
            //System.out.println();
        }
    }

	//核心算法
    public static int tests(char[][] cards) {
        char temp;
        int[][] maps = new int[5][13];
        //Arrays.fill(maps,0);
        //showArrays(maps);

        for (int i = 5; i > 1; --i) {
            for (int j = 1; j < i; ++j) {
                if (cards[j - 1][0] > cards[j][0]) {
                    temp = cards[j - 1][0];
                    cards[j - 1][0] = cards[j][0];
                    cards[j][0] = temp;

                    temp = cards[j - 1][1];
                    cards[j - 1][1] = cards[j][1];
                    cards[j][1] = temp;
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            maps[color(cards[i][1])][value(cards[i][0])]++;
            maps[4][value(cards[i][0])]++;
        }

        //royal-flush | straight-flush
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 9; ++j)
                if ((maps[i][j] & maps[i][j + 1] & maps[i][j + 2] & maps[i][j + 3] & maps[i][j + 4]) > 0)
                    return 13 * 13 * 13 * 13 * 13 + 40 + j;
        //four-of-a-kind
        for (int i = 0; i < 13; ++i)
            if (maps[4][i] >= 4) return 13 * 13 * 13 * 13 * 13 + 20 + i;
        //full-house
        int three = 0, two = 0;
        for (int i = 12; i >= 0; --i) {
            if (maps[4][i] == 2)
                two = two * 15 + i + 1;
            if (maps[4][i] == 3)
                three = i + 1;
        }


        if (two > 0 || three > 0) return 13 * 13 * 13 * 13 * 13 + three + 1;
        //flush
        for (int i = 0; i < 4; ++i) {
            int count = 0, number = 0;
            for (int j = 12; j >= 0; --j)
                for (int k = 0; k < maps[i][j]; ++k) {
                    ++count;
                    number = number * 13 + j;
                }
            if (count == 5) return number;
        }
        //straight
        for (int i = 0; i < 9; ++i)
            if ((maps[4][i] & maps[4][i + 1] & maps[4][i + 2] & maps[4][i + 3] & maps[4][i + 4]) > 0)
                return i - 20;
        //three-of-a-kind
        if (three > 0) return three - 40;

        int ans = 0;
        for (int i = 12; i >= 0; --i)
            if (maps[4][i] == 1)
                ans = ans * 13 + i;
        //two-pairs
        if (two >= 15)
            return two * 15 + ans - 8000;
        //one-pair
        if (two > 0) return two * 13 * 13 * 13 + ans - 13 * 13 * 13 * 13 * 30;
        //high-card
        return ans - 13 * 13 * 13 * 13 * 50;
    }


}
