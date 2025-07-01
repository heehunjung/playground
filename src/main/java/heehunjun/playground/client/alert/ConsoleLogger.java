package heehunjun.playground.client.alert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleLogger implements Logger {

    @Override
    public void log(Throwable e) {
        log.error("{} ", e);
    }
}
