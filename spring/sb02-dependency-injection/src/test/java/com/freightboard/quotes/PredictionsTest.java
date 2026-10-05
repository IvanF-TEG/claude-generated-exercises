package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" or -1 with what you expect, THEN run. Keep a comment on any you got wrong.
// ApplicationContextRunner starts a tiny Spring container containing ONLY the configuration classes you give it,
// so each prediction is a small, isolated experiment. (These don't depend on your TODOs.)
class PredictionsTest {

    interface Greeter {
        String greet();
    }

    record NeedsOneGreeter(Greeter greeter) {
    }

    /** Two beans of the same type. */
    @Configuration
    static class TwoGreeters {
        @Bean
        Greeter friendly() {
            return () -> "Hiya!";
        }

        @Bean
        Greeter formal() {
            return () -> "Good morning.";
        }
    }

    /** The same two beans, but one is @Primary. */
    @Configuration
    static class TwoGreetersOnePrimary {
        @Bean
        Greeter friendly() {
            return () -> "Hiya!";
        }

        @Bean
        @Primary
        Greeter formal() {
            return () -> "Good morning.";
        }
    }

    @Configuration
    static class WantsOneGreeter {
        @Bean
        NeedsOneGreeter needsOneGreeter(Greeter greeter) {
            return new NeedsOneGreeter(greeter);
        }
    }

    record NeedsAllGreeters(java.util.List<Greeter> greeters) {
    }

    @Configuration
    static class WantsAllGreeters {
        @Bean
        NeedsAllGreeters needsAllGreeters(java.util.List<Greeter> greeters) {
            return new NeedsAllGreeters(greeters);
        }
    }

    static final AtomicInteger COUNTER_CREATED = new AtomicInteger();

    static class Counter {
        Counter() {
            COUNTER_CREATED.incrementAndGet();
        }
    }

    record UsesCounter(Counter counter) {
    }

    @Configuration
    static class ThreeUsersOfOneCounter {
        @Bean
        Counter counter() {
            return new Counter();
        }

        @Bean
        UsesCounter a(Counter c) {
            return new UsesCounter(c);
        }

        @Bean
        UsesCounter b(Counter c) {
            return new UsesCounter(c);
        }

        @Bean
        UsesCounter c(Counter c) {
            return new UsesCounter(c);
        }
    }

    @Configuration
    static class PrototypeCounter {
        @Bean
        @Scope("prototype")
        Counter counter() {
            return new Counter();
        }
    }

    /** The simple class name of the deepest cause of the start-up failure, or "started fine". */
    static String startupFailure(Class<?>... configs) {
        String[] result = {"started fine"};
        new ApplicationContextRunner().withUserConfiguration(configs).run(context -> {
            Throwable t = context.getStartupFailure();
            while (t != null && t.getCause() != null) {
                t = t.getCause();
            }
            if (t != null) {
                result[0] = t.getClass().getSimpleName();
            }
        });
        return result[0];
    }

    @Test
    void twoCandidatesForOneInjectionPoint() {
        assertEquals("???", startupFailure(TwoGreeters.class, WantsOneGreeter.class));
    }

    @Test
    void noCandidateAtAll() {
        assertEquals("???", startupFailure(WantsOneGreeter.class));
    }

    @Test
    void primaryBreaksTheTie() {
        String[] greeting = new String[1];
        new ApplicationContextRunner()
                .withUserConfiguration(TwoGreetersOnePrimary.class, WantsOneGreeter.class)
                .run(context -> greeting[0] = context.getBean(NeedsOneGreeter.class).greeter().greet());
        assertEquals("???", greeting[0]);
    }

    @Test
    void howManyGreetersInAListOfAllOfThem() {
        int[] count = new int[1];
        new ApplicationContextRunner()
                .withUserConfiguration(TwoGreetersOnePrimary.class)
                .run(context -> count[0] = context.getBeanProvider(Greeter.class).stream().toList().size());
        assertEquals(-1, count[0]);
    }

    @Test
    void listOfAllGreetersWhenThereAreNone() {
        // "started fine" or the exception's simple name
        assertEquals("???", startupFailure(WantsAllGreeters.class));
    }

    @Test
    void singletonConstructorCallsWithThreeUsers() {
        COUNTER_CREATED.set(0);
        new ApplicationContextRunner().withUserConfiguration(ThreeUsersOfOneCounter.class).run(context -> {
        });
        assertEquals(-1, COUNTER_CREATED.get());
    }

    @Test
    void prototypeAskedForTwice() {
        boolean[] same = new boolean[1];
        new ApplicationContextRunner().withUserConfiguration(PrototypeCounter.class)
                .run(context -> same[0] = context.getBean(Counter.class) == context.getBean(Counter.class));
        assertEquals("???", String.valueOf(same[0]));
    }
}
