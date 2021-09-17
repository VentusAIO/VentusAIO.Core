package com.ventus.core.util;

import java.util.List;

public class Random {
    public static <T> T getRandomElement(List<T> list){
        java.util.Random r = new java.util.Random();
        return list.get(r.nextInt(list.size()));
    }

    public static int getRandom(int min, int max){
        return (int)(Math.random() * (max - min + 1) + min);
    }

    public static String generateRandomWords(int numberOfWords)
    {
        String[] randomStrings = new String[numberOfWords];
        java.util.Random random = new java.util.Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return String.valueOf(randomStrings);
    }
}
