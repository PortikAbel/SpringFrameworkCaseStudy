package edu.bbte.idde.paim1949.desktop;

import edu.bbte.idde.paim1949.backend.ExampleLibrary;
import java.io.IOException;

public class HelloWorld {
    public static void main(String[] args) throws IOException {

        // külső saját modul függőségéből importált osztály
        new ExampleLibrary().logHello();
    }
}