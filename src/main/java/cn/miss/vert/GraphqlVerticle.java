package cn.miss.vert;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.*;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;

import java.util.Map;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-01 10:26
 */
public class GraphqlVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create()).handler(this::handler);
        vertx.createHttpServer().requestHandler(router).listen(8090);
    }

    private void handler(RoutingContext routingContext) {
        String schema = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{hello}");

        routingContext.response().write("")
                .putHeader("content-type", "text/plain")
                .end(executionResult.getData().toString());
    }

    private void test() {
        GraphQLObjectType fooType = newObject()
                .name("Foo")
                .field(newFieldDefinition()
                        .name("bar")
                        .type(GraphQLString))
                .build();
    }

    public static void main(String[] args) {
        GraphQLObjectType userType = newObject()
                .name("User")
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("sex").type(GraphQLString))
                .field(newFieldDefinition().name("intro").type(GraphQLString))
                .build();
        //定义暴露给客户端的查询query api
        GraphQLObjectType queryType = newObject()
                .name("userQuery")
                .field(newFieldDefinition().type(userType).name("user").dataFetcher(new DataFetcher<User>() {
                    @Override
                    public User get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
                        Object argument = dataFetchingEnvironment.getArgument("");

                        return null;
                    }
                }).dataFetcherFactory(new DataFetcherFactory() {
                    @Override
                    public DataFetcher get(DataFetcherFactoryEnvironment dataFetcherFactoryEnvironment) {
//                        dataFetcherFactoryEnvironment.
                        return null;
                    }
                }).staticValue(user))
                .build();
        //创建Schema
        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(queryType)
                .build();
        //测试输出
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();
        Map<String, Object> result = graphQL.execute("{user{name,sex,intro}}").getData();
        System.out.println(result);
    }


}
