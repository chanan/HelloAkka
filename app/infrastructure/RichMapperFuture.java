package infrastructure;

import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.google.common.util.concurrent.ListenableFuture;
import play.libs.Akka;
import scala.Function1;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import java.util.function.Function;

public class RichMapperFuture<T, R> {
    private RichMapperFuture() { }

    public static<T> Future<T> wrap(ListenableFuture<T> listenableFuture) {
        final Promise<T> promise = Futures.promise();
        final Future<T> future = promise.future();
        if(listenableFuture.isDone()) {
            try {
                promise.success(listenableFuture.get());
            } catch (Exception e) {
                promise.failure(e);
            }
        } else {
            listenableFuture.addListener(() -> {
                try {
                    promise.success(listenableFuture.get());
                } catch (Exception e) {
                    promise.failure(e);
                }
            }, Akka.system().dispatcher());
        }
        return future;
    }

    public static<T, R> Mapper<T, R> map(Function<T, R> mapper) {
        return new Mapper<T, R>() {
            @Override
            public R apply(T t) {
                return mapper.apply(t);
            }
        };
    }

    public static<T, R> Future<R> wrapAndMap(ListenableFuture<T> listenableFuture, Function<T, R> mapper) {
        Future<T> future = wrap(listenableFuture);
        return future.map(map(mapper), Akka.system().dispatcher());
    }
}