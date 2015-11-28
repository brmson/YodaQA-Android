package in.vesely.eclub.yodaqa;

import android.os.Looper;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//This code is modified version of the com.github.anrwatchdog.ANRError to support serialization which is needed for ACRA
public class ANRError extends Error implements Serializable {

    private static class $ implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String _name;
        private final StackTraceElement[] _stackTrace;

        private class _Thread extends Throwable implements Serializable {
            private static final long serialVersionUID = 1L;

            private _Thread(_Thread other) {
                super(_name, other);
            }

            @Override
            public Throwable fillInStackTrace() {
                setStackTrace(_stackTrace);
                return this;
            }
        }

        private $(String name, StackTraceElement[] stackTrace) {
            _name = name;
            _stackTrace = stackTrace;
        }
    }

    private static final long serialVersionUID = 1L;


    private ANRError($._Thread st, Map<Thread, StackTraceElement[]> stackTraces) {
        super("Application Not Responding", st);
    }


    @Override
    public Throwable fillInStackTrace() {
        setStackTrace(new StackTraceElement[]{});
        return this;
    }

    static ANRError New(String prefix, boolean logThreadsWithoutStackTrace) {
        final Thread mainThread = Looper.getMainLooper().getThread();

        final Map<Thread, StackTraceElement[]> stackTraces = new TreeMap<Thread, StackTraceElement[]>(new Comparator<Thread>() {
            @Override
            public int compare(Thread lhs, Thread rhs) {
                if (lhs == rhs)
                    return 0;
                if (lhs == mainThread)
                    return 1;
                if (rhs == mainThread)
                    return -1;
                return rhs.getName().compareTo(lhs.getName());
            }
        });

        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet())
            if (entry.getKey() == mainThread || (entry.getKey().getName().startsWith(prefix) && (logThreadsWithoutStackTrace || entry.getValue().length > 0)))
                stackTraces.put(entry.getKey(), entry.getValue());

        $._Thread tst = null;
        for (Map.Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet())
            tst = new $(entry.getKey().getName(), entry.getValue()).new _Thread(tst);

        return new ANRError(tst, stackTraces);
    }

    static ANRError NewMainOnly() {
        final Thread mainThread = Looper.getMainLooper().getThread();
        final StackTraceElement[] mainStackTrace = mainThread.getStackTrace();

        final HashMap<Thread, StackTraceElement[]> stackTraces = new HashMap<Thread, StackTraceElement[]>(1);
        stackTraces.put(mainThread, mainStackTrace);

        return new ANRError(new $(mainThread.getName(), mainStackTrace).new _Thread(null), stackTraces);
    }
}
