package com.midorlo.k12.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.midorlo.k12.security.PersistentTokenCache;
import org.junit.jupiter.api.Test;

class PersistentTokenCacheTest {

    @Test
    void testConstructorThrows() {
        Throwable caught = catchThrowable(() -> new PersistentTokenCache<String>(-1L));
        assertThat(caught).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAbsent() {
        PersistentTokenCache<String> cache = new PersistentTokenCache<>(100L);
        assertThat(cache.get("key")).isNull();
    }

    @Test
    void testAccess() {
        PersistentTokenCache<String> cache = new PersistentTokenCache<>(100L);
        cache.put("key", "val");
        assertThat(cache.size()).isEqualTo(1);
        assertThat(cache.get("key")).isEqualTo("val");
    }

    @Test
    void testReplace() {
        PersistentTokenCache<String> cache = new PersistentTokenCache<>(100L);
        cache.put("key", "val");
        cache.put("key", "foo");
        assertThat(cache.get("key")).isEqualTo("foo");
    }

    @Test
    void testExpires() {
        PersistentTokenCache<String> cache = new PersistentTokenCache<>(1L);
        cache.put("key", "val");
        try {
            Thread.sleep(100L);
        } catch (InterruptedException x) {
            // This should not happen
            throw new Error(x);
        }
        assertThat(cache.get("key")).isNull();
    }

    @Test
    void testPurge() {
        PersistentTokenCache<String> cache = new PersistentTokenCache<>(1L);
        cache.put("key", "val");
        try {
            Thread.sleep(100L);
        } catch (InterruptedException x) {
            // This should not happen
            throw new Error(x);
        }
        assertThat(cache.size()).isEqualTo(1);
        cache.purge();
        assertThat(cache.size()).isEqualTo(0);
    }
}
