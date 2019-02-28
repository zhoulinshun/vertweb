package cn.miss.vert.service;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import java.util.Map;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-02-25 17:24
 */
public interface TypeInterface extends GraphQLQueryResolver {

    Map<String, Object> query();

}
