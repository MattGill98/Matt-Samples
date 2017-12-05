package uk.me.mattgill.samples.grizzly.test;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        WebContainer server = new WebContainer();
        try {
            server.start(9090, 9191);
        } catch (IOException ex) {
            System.err.println("Server failed to start.");
        }
    }
}
