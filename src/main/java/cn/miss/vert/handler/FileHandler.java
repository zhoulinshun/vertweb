package cn.miss.vert.handler;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-02-26 17:18
 */
public class FileHandler {
    private static final Logger log = LoggerFactory.getLogger(FileHandler.class);

    private Vertx vertx;

    public FileHandler(Vertx vertx) {
        this.vertx = vertx;
    }

    public void upload(RoutingContext routingContext) {
        Set<FileUpload> fileUploads = routingContext.fileUploads();
        log.info("file upload :{}", fileUploads.size());
        routingContext.response().setChunked(true);
        for (FileUpload fileUpload : fileUploads) {
            routingContext.response().write("name:" + fileUpload.fileName()+"\n");
            routingContext.response().write("size:" + fileUpload.size()+"\n");
        }

//        routingContext.request().setExpectMultipart(true);
//        routingContext.request().uploadHandler(httpServerFileUpload -> {
//            log.info("{}", httpServerFileUpload.filename());
//            httpServerFileUpload.streamToFileSystem("/Users/zhoulinshun/" + httpServerFileUpload.filename());
//
//        });
//        MultiMap params = routingContext.request().params();
//        JsonObject requestParams = getRequestParams(params);
//        log.info("param:{}", params);params
        routingContext.response().end();
    }

    private EventBus eventBus;

    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final int BAD_REQUEST_ERROR_CODE = 400;


    private JsonObject getRequestParams(MultiMap params) {

        JsonObject paramMap = new JsonObject();
        for (Map.Entry entry : params.entries()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            paramMap.put(key, value);
        }
        return paramMap;
    }

    private static JsonObject getQueryMap(String query) {
        String[] params = query.split("&");
        JsonObject map = new JsonObject();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = "";
            try {
                value = URLDecoder.decode(param.split("=")[1], "UTF-8");
            } catch (Exception e) {
            }
            map.put(name, value);
        }
        return map;
    }

}
