package cn.miss.vert.handler;

import cn.miss.vert.service.UserService;
import graphql.GraphQL;
import graphql.schema.*;

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
 * @Date: Created in 2019-02-24 15:00
 */
public class GraphQLHandler {
    private UserService userService = new UserService();
    public final GraphQL graphQL;

    public GraphQLHandler() {
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
                .field(newFieldDefinition()
                        .name("users")
                        .type(new GraphQLList(userType))
                        .dataFetcher(userService::queryAll))
                .field(newFieldDefinition()
                        .name("userUnion")
                        .type(unionType))
                .build();
        //更改
        GraphQLObjectType mutation = newObject().
                name("mutation").
                field(newFieldDefinition().
                        name("add").
                        type(new GraphQLList(userType)).
                        argument(newArgument().name("user").type(new GraphQLNonNull(userInputType))).
                        dataFetcher(userService::mutation)).
                field(newFieldDefinition().
                        name("update").
                        type(GraphQLInt).
                        argument(newArgument().name("user").type(new GraphQLNonNull(userInputType)))
                        .dataFetcher(userService::update)).
                build();

        //创建Schema
        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(queryType)
                .mutation(mutation)
                .build();
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    public Object handler(DataFetchingEnvironment dataFetchingEnvironment) {

        return null;
    }


}
