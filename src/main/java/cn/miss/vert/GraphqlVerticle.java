package cn.miss.vert;

import cn.miss.vert.handler.GraphQLHandler;
import cn.miss.vert.handler.VertxHandler;
import graphql.GraphQL;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-01 10:26
 */
public class GraphqlVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(GraphqlVerticle.class);

    private VertxHandler vertxHandler = new VertxHandler(vertx);


    @Override
    public synchronized void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().path("/hello").handler(CookieHandler.create()).handler(this::handler);
        URL resource = this.getClass().getResource("/");
        //自动上传文件
        router.route(HttpMethod.POST, "/upload/autoUpload").handler(BodyHandler.create().setHandleFileUploads(true).setUploadsDirectory(resource.getPath() + "/upload").setMergeFormAttributes(true));
        //手动文件上传
        router.route(HttpMethod.POST, "/upload/handlerUpload").handler(vertxHandler::autoUpload);
        router.route("/formWithFile").handler(vertxHandler::formWithFile);
        router.route("/form/:pram1/:param2").handler(vertxHandler::form);


        vertx.createHttpServer().requestHandler(router).listen(8090);
        WebClient webClient = WebClient.create(vertx, null);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private void handler(RoutingContext routingContext) {
        routingContext.response().sendFile("file.html");


//        String schema = "type Query{hello: String}";
//
//        SchemaParser schemaParser = new SchemaParser();
//        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
//
//        RuntimeWiring runtimeWiring = newRuntimeWiring()
//                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
//                .build();
//
//        SchemaGenerator schemaGenerator = new SchemaGenerator();
//        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
//
//        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
//        ExecutionResult executionResult = build.execute("{hello}");

//        String bodyAsString = routingContext.getBodyAsString();
//        ExecutionResult execute = graphQL.execute(bodyAsString);
//        routingContext.response().write("")
//                .putHeader("content-type", "text/plain")
//                .end(execute.getData().toString());
    }

    public static void main(String[] args) {
        GraphQLHandler graphqlVerticle = new GraphQLHandler();
        GraphQL graphQL = graphqlVerticle.graphQL;
        Map<String, Object> user = graphQL.execute("query {user(id:123){name,age,intro}}").getData();
        log.info("{}", user);

        Map<String, Object> result = graphQL.execute("mutation {add(user:{name:\"test2User\",id:\"124\",age:12,intro:\"简介\"}){name,intro,id}}").getData();
        System.out.println(result);

        Map<String, Object> users = graphQL.execute("query {users{name,age,intro}}").getData();
        System.out.println(users);
    }

}
