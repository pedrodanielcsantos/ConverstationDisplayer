package com.pedrosantos.conversationdisplayer.promises;

import org.jdeferred.Promise;
import org.jdeferred.impl.DefaultDeferredManager;

/**
 * Wrapper around promise invocation to concentrate the usage of the DeferredManager or not.
 *
 * With this, and invoking CDPromise.when(), the code gets more readable when there's the need to
 * invoke one and several promises at once.
 */
public class CDPromise {

    public static <D, F, P> Promise<D, F, P> when(Promise<D, F, P> promise) {
        return promise;
    }

    public static <D, F, P> Promise when(Promise<D, F, P>... promises) {
        return new DefaultDeferredManager().when(promises);
    }
}
