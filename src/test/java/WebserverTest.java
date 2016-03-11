import Web.Configuration;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Robin Duda
 */
@RunWith(VertxUnitRunner.class)
public class WebserverTest {
    private Vertx vertx;

    @Rule
    public Timeout timeout = Timeout.seconds(15);

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new Launcher(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void getFileNotFound(TestContext context) {
        Async async = context.async();

        vertx.createHttpClient()
                .getNow(Configuration.WEB_PORT, "localhost", "", response -> {

                    response.bodyHandler(event -> {
                        JsonObject json = event.toJsonObject();

                        context.assertTrue(json.containsKey("page"));
                        context.assertEquals(404, json.getInteger("page"));

                        async.complete();
                    });
                });
    }
}