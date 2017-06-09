
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;
import com.wolf.utils.redis.ZParams;

import java.util.Set;

/**
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 上午10:33:14
 * @version 1.0
 */
public interface RedisZSetCommands {
    public enum Aggregate {
        SUM, MIN, MAX;
    }

    public class Tuple {
        private final double score;
        private final byte[] value;

        public Tuple(byte[] value, double score) {
            this.score = score;
            this.value = value;
        }

        public double getScore() {
            return score;
        }

        public byte[] getValue() {
            return value;
        }
    }

    @Process(Policy.WRITE)
    Boolean zAdd(byte[] key, double score, byte[] value);

    @Process(Policy.WRITE)
    Long zAdd(byte[] key, Tuple... value);

    @Process(Policy.WRITE)
    Long zRem(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Double zIncrBy(byte[] key, double increment, byte[] value);

    @Process(Policy.READ)
    Long zRank(byte[] key, byte[] value);

    @Process(Policy.READ)
    Long zRevRank(byte[] key, byte[] value);

    @Process(Policy.READ)
    Set<byte[]> zRange(byte[] key, long begin, long end);

    @Process(Policy.READ)
    Set<Tuple> zRangeWithScore(byte[] key, long begin, long end);

    @Process(Policy.READ)
    Set<byte[]> zRevRange(byte[] key, long begin, long end);

    @Process(Policy.READ)
    Set<Tuple> zRevRangeWithScore(byte[] key, long begin, long end);

    @Process(Policy.READ)
    Set<byte[]> zRangeByScore(byte[] key, double min, double max);

    @Process(Policy.READ)
    Set<Tuple> zRangeByScoreWithScore(byte[] key, double min, double max);

    @Process(Policy.READ)
    Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count);

    @Process(Policy.READ)
    Set<Tuple> zRangeByScoreWithScore(byte[] key, double min, double max, long offset, long count);

    @Process(Policy.READ)
    Set<byte[]> zRevRangeByScore(byte[] key, double min, double max);

    @Process(Policy.READ)
    Set<Tuple> zRevRangeByScoreWithScore(byte[] key, double min, double max);

    @Process(Policy.READ)
    Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count);

    @Process(Policy.READ)
    Set<Tuple> zRevRangeByScoreWithScore(byte[] key, double min, double max, long offset, long count);

    @Process(Policy.READ)
    Long zCount(byte[] key, double min, double max);

    @Process(Policy.READ)
    Long zCard(byte[] key);

    @Process(Policy.READ)
    Double zScore(byte[] key, byte[] value);

    @Process(Policy.WRITE)
    Long zRemRange(byte[] key, long begin, long end);

    @Process(Policy.WRITE)
    Long zRemRangeByScore(byte[] key, double min, double max);

    @Process(Policy.WRITE)
    Long zUnionStore(byte[] destKey, byte[]... sets);

    @Process(Policy.WRITE)
    Long zUnionStore(byte[] destKey, ZParams params, int[] weights, byte[]... sets);

    @Process(Policy.WRITE)
    Long zInterStore(byte[] destKey, byte[]... sets);

    @Process(Policy.WRITE)
    Long zInterStore(byte[] destKey, ZParams aggregate, int[] weights, byte[]... sets);
}
