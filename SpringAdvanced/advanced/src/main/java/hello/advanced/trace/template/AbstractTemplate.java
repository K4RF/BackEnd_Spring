package hello.advanced.trace.template;

import hello.advanced.logtrace.LogTrace;
import hello.advanced.trace.TraceStatus;

public abstract class AbstractTemplate<T> {
    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace){
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
        //로직 호출
            T result = call();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
    protected abstract T call();
}
