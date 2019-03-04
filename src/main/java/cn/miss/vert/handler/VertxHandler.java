package cn.miss.vert.handler;

import com.google.gson.Gson;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.FileUploadImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-02-26 17:18
 */
public class VertxHandler {
    private static final Logger log = LoggerFactory.getLogger(VertxHandler.class);

    private Vertx vertx;

    private EventBus eventBus;

    private Gson gson = new Gson();


    public VertxHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    /**
     * 文件上传自动处理
     *
     * @param routingContext
     */
    public void autoUpload(RoutingContext routingContext) {
        Set<FileUpload> fileUploads = routingContext.fileUploads();
        routingContext.response().setChunked(true);
        for (FileUpload fileUpload : fileUploads) {
            routingContext.response().write(gson.toJson(fileUpload));
        }
        routingContext.response().end();
    }

    /**
     * 文件上传手动处理
     *
     * @param routingContext
     */
    public void handlerUpload(RoutingContext routingContext) {
        routingContext.request().setExpectMultipart(true);
        routingContext.request().uploadHandler(httpServerFileUpload -> {
            Set<FileUpload> fileUploads = routingContext.fileUploads();
            log.info("httpServerFileUpload:{}", httpServerFileUpload.name());
            URL resource = this.getClass().getResource("/");
            httpServerFileUpload.streamToFileSystem(resource.getPath() + "/upload/" + httpServerFileUpload.filename());
            fileUploads.add(new FileUploadImpl(httpServerFileUpload.filename(), httpServerFileUpload));
        });
        Set<FileUpload> fileUploads = routingContext.fileUploads();
        log.info("file autoUpload :{}", fileUploads.size());
        routingContext.response().setChunked(true);
        //只有在前置处理器上传完成并且添加过 此处才会有文件对象
        for (FileUpload fileUpload : fileUploads) {
            routingContext.response().write("name:" + fileUpload.fileName() + "\n");
            routingContext.response().write("size:" + fileUpload.size() + "\n");
        }
        routingContext.response().end();
    }

    /**
     * 文件上传+form表单
     *
     * @param routingContext
     */
    public void formWithFile(RoutingContext routingContext) {
        routingContext.request().setExpectMultipart(true);
        routingContext.request().uploadHandler(httpServerFileUpload -> {
            Set<FileUpload> fileUploads = routingContext.fileUploads();
            log.info("httpServerFileUpload:{}", httpServerFileUpload.name());
            URL resource = this.getClass().getResource("/");
            httpServerFileUpload.streamToFileSystem(resource.getPath() + "/upload/" + httpServerFileUpload.filename());
            fileUploads.add(new FileUploadImpl(httpServerFileUpload.filename(), httpServerFileUpload));
        });

    }

    /**
     * 表单上传+query+path
     *
     * @param routingContext
     */
    public void form(RoutingContext routingContext) {
        // post 参数
        MultiMap params = routingContext.request().params();
        //query 参数
        MultiMap entries = routingContext.queryParams();
        //路径参数
        Map<String, String> stringStringMap = routingContext.pathParams();
        routingContext.response().setChunked(true);
        routingContext.response().write("post 参数:");
        for (Map.Entry<String, String> param : params) {
            routingContext.response().write(param.getKey() + ":" + param.getValue());
        }
        routingContext.response().write("query 参数:");
        for (Map.Entry<String, String> param : entries) {
            routingContext.response().write(param.getKey() + ":" + param.getValue());
        }
        routingContext.response().write("path param:");
        stringStringMap.forEach((k, v) -> routingContext.response().write(k + ":" + v));
        routingContext.response().end();
    }

    public void body(RoutingContext routingContext) {
        @Nullable String bodyAsString = routingContext.getBodyAsString();
        routingContext.response().end(bodyAsString);
    }


}
