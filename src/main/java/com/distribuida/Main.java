package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.servicios.IBookService;
import com.google.gson.Gson;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

public class Main {

    private static ContainerLifecycle lifecycle = null;

    static IBookService service;

    static Gson gson = new Gson();

    public static void main(String[] args) {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);

        service = CDI.current().select(IBookService.class).get();

        WebServer server = WebServer.builder()
                .port(8080)
                .routing(builder -> builder
                        .get("/books/{id}", Main::oneBook)
                        .get("/books", Main::allBooks)
                        .post("/books", Main::createBook)
                        .put("/books/{id}", Main::updateBook)
                        .delete("/books/{id}", Main::deleteBook)
                )
                .build();

        server.start();

        service.findAll().stream().forEach(System.out::println);

        shutdown();
    }

    public static void shutdown() {
        lifecycle.stopApplication(null);
    }

    static void oneBook(ServerRequest rq, ServerResponse res) {
        res.send(gson.toJson(service.findById(
                Integer.valueOf(rq.path().pathParameters().get("id"))
        )));
    }

    static void allBooks(ServerRequest rq, ServerResponse res) {
        res.send(gson.toJson(service.findAll()));
    }

    static void createBook(ServerRequest rq, ServerResponse res) {
        res.send(gson.toJson(
                service.create(
                        gson.fromJson(
                                rq.content().as(String.class), Book.class
                        )
                )
        ));
    }

    static void updateBook(ServerRequest rq, ServerResponse res) {
        var book = gson.fromJson(
                rq.content().as(String.class), Book.class
        );

        book.setId(Integer.valueOf(rq.path().pathParameters().get("id")));

        res.send(gson.toJson(
                service.update(book)
        ));
    }

    static void deleteBook(ServerRequest rq, ServerResponse res) {
        service.delete(Integer.valueOf(rq.path().pathParameters().get("id")));
        res.send("");
    }


}
