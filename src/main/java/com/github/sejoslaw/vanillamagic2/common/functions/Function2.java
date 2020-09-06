package com.github.sejoslaw.vanillamagic2.common.functions;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface Function2<TFirst, TSecond, TOut> {
    TOut apply(TFirst first, TSecond second);
}
