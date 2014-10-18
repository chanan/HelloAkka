package infrastructure;

import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import play.libs.Akka;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import java.util.function.Function;

public class RichResultSetFuture<R> {
    private RichResultSetFuture() { }

    public static Future<ResultSet> wrap(ResultSetFuture resultSetFuture) {
        final Promise<ResultSet> promise = Futures.promise();
        final Future<ResultSet> future = promise.future();
        if(resultSetFuture.isDone()) {
            try {
                promise.success(resultSetFuture.getUninterruptibly());
            } catch (Exception e) {
                promise.failure(e);
            }
        } else {
            resultSetFuture.addListener(() -> {
                try {
                    promise.success(resultSetFuture.getUninterruptibly());
                } catch (Exception e) {
                    promise.failure(e);
                }
            }, Akka.system().dispatcher());
        }
        return future;
    }

    public static<R> Mapper<ResultSet, R> map(Function<ResultSet, R> mapper) {
        return new Mapper<ResultSet, R>() {
            @Override
            public R apply(ResultSet rs) {
                return mapper.apply(rs);
            }
        };
    }

    public static<R> Future<R> wrapAndMap(ResultSetFuture resultSetFuture, Function<ResultSet, R> mapper) {
        Future<ResultSet> future = wrap(resultSetFuture);
        return future.map(map(mapper), Akka.system().dispatcher());
    }
}