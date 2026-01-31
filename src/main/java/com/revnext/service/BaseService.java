package com.revnext.service;

import com.revnext.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class BaseService {
    @Autowired
    protected UserRepository userRepository;

    protected static <T> List<T> filterList(List<T> inputList, Predicate<T> condition) {
        if (inputList == null || condition == null) {
            return Collections.emptyList();
        }
        return inputList.stream()
                .filter(condition)
                .toList();
    }

    public static <T, K, C> Predicate<T> fieldInContextualSet(
            C context,
            Function<C, Set<K>> contextToSet,
            Function<T, K> fieldExtractor) {
        Set<K> allowedValues = contextToSet.apply(context);
        return item -> allowedValues.contains(fieldExtractor.apply(item));
    }

}
