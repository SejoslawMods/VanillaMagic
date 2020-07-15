package com.github.sejoslaw.vanillamagic2.common.functions;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface Function4<TFirst, TSecond, TThird, TFourth, TOut> {
    TOut apply(TFirst first, TSecond second, TThird third, TFourth fourth);
}
