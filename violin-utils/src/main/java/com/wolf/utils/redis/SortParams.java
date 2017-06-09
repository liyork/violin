
package com.wolf.utils.redis;

import static com.wolf.utils.redis.Protocol.Keyword.*;
import com.wolf.utils.redis.command.BaseCommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortParams {
    private List<byte[]> params = new ArrayList<byte[]>();
    protected StringTedisSerializer stringSerializer = new StringTedisSerializer();

    public SortParams by(final int namespace, final String pattern) {
        return by(namespace, SafeEncoder.encode(pattern));
    }

    public SortParams by(final int namespace, final byte[] pattern) {
        params.add(BY.raw);
        params.add(rawKey(namespace, pattern));
        return this;
    }

    private byte[] rawKey(int namespace, byte[] bytekey) {
        byte[] prefix = stringSerializer.serialize(String.valueOf(namespace));
        byte[] result = new byte[prefix.length + bytekey.length + 1];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(BaseCommands.PART, 0, result, prefix.length, 1);
        System.arraycopy(bytekey, 0, result, prefix.length + 1, bytekey.length);
        return result;
    }

    public SortParams nosort() {
        params.add(BY.raw);
        params.add(NOSORT.raw);
        return this;
    }

    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection(params);
    }

    public SortParams desc() {
        params.add(DESC.raw);
        return this;
    }

    public SortParams asc() {
        params.add(ASC.raw);
        return this;
    }

    public SortParams limit(final int start, final int count) {
        params.add(LIMIT.raw);
        params.add(Protocol.toByteArray(start));
        params.add(Protocol.toByteArray(count));
        return this;
    }

    public SortParams alpha() {
        params.add(ALPHA.raw);
        return this;
    }

    public SortParams get(int namespace, String... patterns) {
        for (final String pattern : patterns) {
            params.add(GET.raw);
            params.add(rawKey(namespace, SafeEncoder.encode(pattern)));
        }
        return this;
    }

    public SortParams get(int namespace, byte[]... patterns) {
        for (final byte[] pattern : patterns) {
            params.add(GET.raw);
            params.add(rawKey(namespace, pattern));
        }
        return this;
    }
}