package com.yatracker.benchmark;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author ya
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 20)
@Measurement(iterations = 5, time = 30)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Threads(value = 8)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})
public class HashidsBenchmark {

    Hashids hashids1 = new HashidsBuilder().build();
    org.hashids.Hashids hashids3 = new org.hashids.Hashids();

    Hashids hashids2 = new HashidsBuilder().minLength(48).salt("niieani/hashids.js").build();

    org.hashids.Hashids hashids4 = new org.hashids.Hashids("niieani/hashids.js", 48);

    @Benchmark
    public void default_encode_my() {
        hashids1.encode(9_007_199_254_740_991L, 9_007_199_254_740_991L, 9_007_199_254_740_991L);
    }

    @Benchmark
    public void default_encode_oth() {
        hashids3.encode(9_007_199_254_740_991L, 9_007_199_254_740_991L, 9_007_199_254_740_991L);
    }

    @Benchmark
    public void default_decode_my() {
        hashids1.decode("5KoLLVL49RLhYkppOplM6piwWNNANny8N");
    }

    @Benchmark
    public void default_decode_oth() {
        hashids3.decode("5KoLLVL49RLhYkppOplM6piwWNNANny8N");
    }

    @Benchmark
    public void salt_length_encode_my() {
        hashids2.encode(9_007_199_254_740_991L, 9_007_199_254_740_991L, 9_007_199_254_740_991L);
    }

    @Benchmark
    public void salt_length_encode_oth() {
        hashids4.encode(9_007_199_254_740_991L, 9_007_199_254_740_991L, 9_007_199_254_740_991L);
    }

    @Benchmark
    public void salt_length_decode_my() {
        hashids2.decode("Nb7MGozgOMWDDBDJneDuLmvv9vVGEvu0rzzRzplZzwVaAjBq");
    }

    @Benchmark
    public void salt_length_decode_oth() {
        hashids4.decode("Nb7MGozgOMWDDBDJneDuLmvv9vVGEvu0rzzRzplZzwVaAjBq");
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(HashidsBenchmark.class.getSimpleName())
                .addProfiler(JavaFlightRecorderProfiler.class)
                .output("benchmark/benchmark.log")
                .build();

        new Runner(options).run();
    }

}
