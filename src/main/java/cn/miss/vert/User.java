package cn.miss.vert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-01-04 20:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String intro;
    private String name;
    private int age;
}
