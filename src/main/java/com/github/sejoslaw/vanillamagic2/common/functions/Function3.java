package com.github.sejoslaw.vanillamagic2.common.functions;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface Function3<TFirst, TSecond, TThird, TOut> {
    TOut apply(TFirst first, TSecond second, TThird third);
}
