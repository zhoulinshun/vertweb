import cn.miss.vert.GraphqlVerticle;
import org.junit.Test;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-02-27 18:47
 */
public class NormalTest {

    @org.junit.Test
    public void test() throws IOException {
        ClassRelativeResourceLoader classRelativeResourceLoader = new ClassRelativeResourceLoader(this.getClass());
        Resource resource = classRelativeResourceLoader.getResource("/");
        URI uri = resource.getURI();
        System.out.println(uri);
    }

    @Test
    public void test2(){
        URL resource = GraphqlVerticle.class.getResource("/");
        System.out.println(resource.getPath());
    }
}
