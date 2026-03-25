package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

public class RequestBase implements Constants{

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = BASE_URL;
        RestAssured.port = PORT;
        RestAssured.basePath = BASE_PATH;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        resBuilder.log(LogDetail.ALL);
        RestAssured.responseSpecification = resBuilder.build();

        // RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

}
