package cn.miss.vert;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-04 20:43
 */
@Data
@Builder
public class User {
    private String id;
    private String intro;
    private String name;
    private int age;
}
