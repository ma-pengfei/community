package com.nowcoder.community;

import java.io.IOException;

/**
 * @author Administrator
 * @date 2022/7/19 15:48
 */
public class WkTests {
    public static void main(String[] args) {


        String cmd = "F:/Java/software/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://www.nowcoder.com F:/work/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            // 异步执行:调用完该命令后继续向下执行，图片被异步的生成
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
