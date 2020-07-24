package com.github.sejoslaw.vanillamagic2.common.functions;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface Function5<TFirst, TSecond, TThird, TFourth, TFifth, TOut> {
    TOut apply(TFirst first, TSecond second, TThird third, TFourth fourth, TFifth fifth);
}
