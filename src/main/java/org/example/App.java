package org.example;

import io.vertx.core.Vertx;
import org.example.controller.verticleController;

public class App
{
    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new verticleController());
    }
}
