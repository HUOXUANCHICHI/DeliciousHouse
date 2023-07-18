package com.ablaze;

import org.junit.jupiter.api.Test;

class DeliciousHouseApplicationTests {

    @Test
    public void test1() {
        String fileName = "erererw.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }

}
