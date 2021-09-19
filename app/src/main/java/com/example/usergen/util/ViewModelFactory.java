package com.example.usergen.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Supplier;

public class ViewModelFactory {

    @NonNull
    public static <VM extends ViewModel> ViewModelProvider.Factory from(
            @NonNull Class<VM> viewModelClass,
            @NonNull Supplier<VM> supplier
    ) {
       return new ViewModelProvider.Factory() {
           @SuppressWarnings("unchecked")
           @NonNull
           @Override
           public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

               if (!modelClass.equals(viewModelClass)) {
                   throw new IllegalArgumentException(
                           "expected class " + viewModelClass + " but got class " + modelClass
                   );
               }

               return (T) supplier.get();
           }
       };
    }
}
