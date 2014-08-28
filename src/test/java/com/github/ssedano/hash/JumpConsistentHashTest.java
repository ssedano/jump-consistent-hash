/*
Copyright 2014 ssedano

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */
package com.github.ssedano.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import com.github.ssedano.hash.JumpConsistentHash;

/**
 * @author <a href="mailto:serafin.sedano@gmail.com">Serafin Sedano</a>
 */
public class JumpConsistentHashTest {

    @Test
    public void hashInRange() {
	int bucket = JumpConsistentHash.jumpConsistentHash(ThreadLocalRandom
		.current().nextLong(), 10);
	assertTrue("Expected bucket in range [0, 10) but was " + bucket,
		bucket > -1 && bucket < 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeBucketsEqualsMinusOne() {
	JumpConsistentHash.jumpConsistentHash(1L, -1);
    }

    @Test
    public void hashInRangeForObject() {
	int bucket = JumpConsistentHash.jumpConsistentHash(new Object(), 10);
	assertTrue("Expected bucket in range [0, 10) but was " + bucket,
		bucket > -1 && bucket < 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeBucketsEqualsMinusOnForObjecte() {
	JumpConsistentHash.jumpConsistentHash(new Object(), -1);
    }

    @Test
    public void assignsToSameBucket() {
	int bucket1 = JumpConsistentHash.jumpConsistentHash(1L, 10);
	int bucket2 = JumpConsistentHash.jumpConsistentHash(1L, 11);

	assertEquals(bucket1, bucket2);
    }

    @Test
    public void assignsObjectToSameBucket() {
	int bucket1 = JumpConsistentHash.jumpConsistentHash("object", 10);
	int bucket2 = JumpConsistentHash.jumpConsistentHash("object", 11);

	assertEquals(bucket1, bucket2);
    }

    /**
     * Paper states a standar error of 0.00000000764 and (0.99999998,
     * 1.00000002) 99% confidence interval.
     *
     */
    @Test
    public void keyDistribution() {
	Map<Integer, Integer> sizes = new TreeMap<>();

	ThreadLocalRandom r = ThreadLocalRandom.current();
	int keys = 100_000;
	int buckets = 10;

	for (int i = 0; i < keys; i++) {
	    int b = JumpConsistentHash.jumpConsistentHash(r.nextInt(keys),
		    buckets);
	    sizes.compute(b, (k, v) -> v == null ? 0 : v + 1);
	}
	assertEquals(buckets, sizes.size());
	IntSummaryStatistics stats = sizes.values().stream().mapToInt(i -> i)
		.summaryStatistics();

	double percent99 = (double) keys / (double) buckets * 0.01d;

	assertTrue(
		"Expected over 99% avg (" + percent99 + ") but was "
			+ stats.getAverage(), stats.getAverage() > percent99);
    }
}
