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
package org.ssedano.hash;

/**
 * {@link #jumpConsistentHash(long, int)} accepts "a 64-bit key and the number
 * of buckets. It outputs a number in the range [0, buckets)." <a
 * href="http://arxiv.org/ftp/arxiv/papers/1406/1406.2294.pdf">Paper</a>.
 * <p>
 * The C++ implementation they provide is as follows:
 *
 * <pre>
 * {@code
 *  int32_t JumpConsistentHash(uint64_t key, int32_t num_buckets) {
 *   int64_t b = ­-1, j = 0;
 *     while (j < num_buckets) {
 *       b = j;
 *       key = key * 2862933555777941757ULL + 1;
 *       j = (b + 1) * (double(1LL << 31) / double((key >> 33) + 1));
 *     }
 *     return b;
 *   }}
 * </pre>
 *
 * @author <a href="mailto:serafin.sedano@gmail.com">Serafin Sedano</a>
 */
public final class JumpConsistentHash {
    private static final long UNSIGNED_MASK = 0x7fffffffffffffffL;

    private static final long JUMP = 1L << 31;

    private static final long CONSTANT = Long
	    .parseUnsignedLong("2862933555777941757");

    private JumpConsistentHash() {
	throw new AssertionError(
		"No org.ssedano.hash.JumpConsistentHash instances for you!");
    }

    /**
     * Accepts "a 64-bit key and the number of buckets. It outputs a number in
     * the range [0, buckets].". This implementation uses as a key the
     * {@link Object#hashCode()} of the supplied argument.
     *
     * @param o
     *            object to store
     * @param buckets
     *            number of available buckets
     * @return the hash of the object <code>o</code>
     */
    public static int jumpConsistentHash(final Object o, final int buckets) {
	return jumpConsistentHash(o.hashCode(), buckets);
    }

    /**
     * Accepts "a 64-bit key and the number of buckets. It outputs a number in
     * the range [0, buckets]."
     *
     * @param key
     *            key to store
     * @param buckets
     *            number of available buckets
     * @return the hash of the key
     * @throws IllegalArgumentException
     *             if buckets is less than 0
     */
    public static int jumpConsistentHash(final long key, final int buckets) {
	checkBuckets(buckets);
	long k = key;
	long b = -1;
	long j = 0;

	while (j < buckets) {
	    b = j;
	    k = k * CONSTANT + 1L;

	    j = (long) ((b + 1L) * (JUMP / toDouble((k >>> 33) + 1L)));
	}
	return (int) b;
    }

    private static void checkBuckets(final int buckets) {
	if (buckets < 0) {
	    throw new IllegalArgumentException("Buckets cannot be less than 0");
	}
    }

    private static double toDouble(final long n) {
	double d = n & UNSIGNED_MASK;
	if (n < 0) {
	    d += 0x1.0p63;
	}
	return d;
    }

}
