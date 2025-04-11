package org.example.binarytreeweb.util.comparator;

public class GenericComparatorFactory {
    public static <T> ComparatorFactory getFactory(T type){
        if (type instanceof String){
            return new ComparatorFactory.StringComparator();
        } else if (type instanceof Integer){
            return new ComparatorFactory.IntegerComparator();
        } else if (type instanceof Double){
            return new ComparatorFactory.DoubleComparator();
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
