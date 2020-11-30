package bgu.spl.mics.application;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @RepeatedTest(100)
    void main() {
        String[] s = {"src/main/java/bgu/spl/mics/application/input.json"};
        Main.main(s);
    }
}