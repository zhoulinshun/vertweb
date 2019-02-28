package cn.miss.vert.service;

import cn.miss.vert.bean.User;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-04 20:47
 */
public class UserService {

    private static final List<User> list = new ArrayList<>();

    static {
        list.add(new User("123", "hha", "小明", 12));
        list.add(new User("123", "hha", "小丽", 12));
        list.add(new User("123", "hha", "小红", 12));
        list.add(new User("123", "hha", "小张", 12));
        list.add(new User("123", "hha", "小白", 12));
    }


    public User query(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = dataFetchingEnvironment.getArgument("id");
        return list.stream().filter(user -> Objects.equals(user.getId(), id)).findFirst().orElse(null);
    }

    public List<User> queryAll(DataFetchingEnvironment dataFetchingEnvironment) {
        return list;
    }

    public int update(DataFetchingEnvironment dataFetchingEnvironment) {
        User user = dataFetchingEnvironment.getArgument("user");

        return 1;
    }

    public List<User> mutation(DataFetchingEnvironment dataFetchingEnvironment) {
        try {
//            JDBCClient.createNonShared()
            Map<String, Object> arguments = dataFetchingEnvironment.getArguments();
            Map<String, Object> userMap = (Map<String, Object>) arguments.get("user");
            User user = new User();
            BeanUtils.populate(user, userMap);
            list.add(user);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }


}
