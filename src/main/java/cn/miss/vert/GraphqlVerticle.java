package cn.miss.vert;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLUnionType.newUnionType;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-01 10:26
 */
public class GraphqlVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(GraphqlVerticle.class);
    private final GraphQL graphQL;

    private UserService userService;

    public GraphqlVerticle() {
        //声明类型
        GraphQLObjectType userType = newObject()
                .name("User")
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("id").type(GraphQLString))
                .field(newFieldDefinition().name("age").type(GraphQLInt))
                .field(newFieldDefinition().name("intro").type(GraphQLString))
                .build();
        GraphQLInputType userInputType = newInputObject()
                .name("inputType")
                .field(newInputObjectField().name("name").type(GraphQLString))
                .field(newInputObjectField().name("id").type(GraphQLString))
                .field(newInputObjectField().name("age").type(GraphQLInt))
                .field(newInputObjectField().name("intro").type(GraphQLString))
                .build();

        GraphQLUnionType unionType = newUnionType()
                .name("unionType").possibleType(userType).typeResolver(env -> userType).build();

        //查询
        GraphQLObjectType queryType = newObject()
                .name("userQuery")
                .field(newFieldDefinition().
                        type(userType).
                        name("user").
                        argument(newArgument().name("id").type(GraphQLString)).
                        dataFetcher(userService::query))
//                .field(newFieldDefinition()
//                        .name("users")
//                        .type(new GraphQLList(userType))
//                        .dataFetcher(env -> list))
                .field(newFieldDefinition()
                        .name("userUnion")
                        .type(unionType))
                .build();
        //更改
        GraphQLObjectType mutation = newObject().
                name("mutationABC").
                field(newFieldDefinition().
                        name("addUser").
                        type(new GraphQLList(userType)).
                        argument(newArgument().name("user").type(new GraphQLNonNull(userInputType))).
                        dataFetcher(userService::mutation)).
                build();

        //创建Schema
        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(queryType)
                .mutation(mutation)
                .build();
        graphQL = GraphQL.newGraphQL(schema).build();

    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create()).handler(this::handler);
        vertx.createHttpServer().requestHandler(router).listen(8090);
    }

    private void handler(RoutingContext routingContext) {
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

        String bodyAsString = routingContext.getBodyAsString();
        ExecutionResult execute = graphQL.execute(bodyAsString);
        routingContext.response().write("")
                .putHeader("content-type", "text/plain")
                .end(execute.getData().toString());
    }

    public static void main(String[] args) {
        GraphqlVerticle graphqlVerticle = new GraphqlVerticle();
        GraphQL graphQL = graphqlVerticle.graphQL;
        Map<String, Object> user = graphQL.execute("query {user(id:\"123\"){name,age,intro}}").getData();
        log.info("{}", user);

        Map<String, Object> result = graphQL.execute("mutation {addUser(user:{name:\"test2User\",id:\"124\",age:12,intro:\"简介\"}){name,intro,id}}").getData();
        System.out.println(result);

        Map<String, Object> users = graphQL.execute("query {users{name,age,intro}}").getData();
        System.out.println(users);
    }

}
