package cn.miss.vert;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-04 20:47
 */
public class UserService {


    public User query(String id) {
        return new User.UserBuilder().id(id).age(12).name("小明").intro("hello").build();
    }


}
