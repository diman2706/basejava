package com.urise.webapp;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

public class MainStream {
    public static int minValue(int[] values) {
       return IntStream.of(values).sorted().distinct().reduce((left, right) -> {
            left = left*10;
            return left + right;
        }).getAsInt();
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream()
                .collect(partitioningBy(x -> x % 2 == 0, toList()));
        return map.get(map.get(false).size() % 2 != 0);
    }

    public static void main(String[] args) {
        Random random = new Random();
        int[] array = new int[6];
        IntStream.range(0, array.length).forEachOrdered(i -> {
            array[i] = random.nextInt(9) + 1;
            System.out.println(array[i]);
        });
        System.out.println("Min Value is :" + minValue(array));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 8);
        System.out.println("List of oddOrEven : " + oddOrEven(list));
    }
}