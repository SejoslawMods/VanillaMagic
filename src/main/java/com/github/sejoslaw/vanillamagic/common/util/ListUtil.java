package com.github.sejoslaw.vanillamagic.common.util;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Class which store various methods connected with List.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ListUtil {
    private ListUtil() {
    }

    @Nullable
    public static <T> T getRandomObjectFromList(List<T> list) {
        int randIndex = new Random().nextInt(list.size() - 1);

        return list.get(randIndex);
    }

    @Nullable
    public static <T> T getRandomObjectFromTab(T[] tab) {
        int randIndex = new Random().nextInt(tab.length - 1);

        return tab[randIndex];
    }

    public static int getRandomObjectFromTab(int[] tab) {
        int randIndex = new Random().nextInt(tab.length - 1);

        return tab[randIndex];
    }

    /**
     * @return Returns the combined lists.
     */
    public static <T> List<T> combineLists(List<T> l1, List<T> l2) {
        l1.addAll(l2);
        return l1;
    }
}