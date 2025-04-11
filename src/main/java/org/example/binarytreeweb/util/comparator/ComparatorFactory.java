package org.example.binarytreeweb.util.comparator;

import java.util.Comparator;

public abstract class ComparatorFactory<T>{
    public abstract Comparator<T>  createComparator();

    static class DoubleComparator extends ComparatorFactory<Double>{

        @Override
        public Comparator<Double> createComparator() {
            return Double::compare;
        }
    }

    static class IntegerComparator extends ComparatorFactory<Integer>{

        @Override
        public Comparator<Integer> createComparator() {
            return Integer::compare;
        }
    }

    static class StringComparator extends ComparatorFactory<String>{

        @Override
        public Comparator<String> createComparator() {
            return String::compareTo;
        }
    }
}
