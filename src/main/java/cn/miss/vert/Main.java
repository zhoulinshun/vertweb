package cn.miss.vert;

import io.vertx.core.Vertx;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-01 10:23
 */
public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new GraphqlVerticle());
    }
}
