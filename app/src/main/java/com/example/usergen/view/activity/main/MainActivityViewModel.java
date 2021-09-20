package com.example.usergen.view.activity.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.usergen.service.generator.RandomUserGeneratorInput;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivityViewModel extends ViewModel {

    private final PublishSubject<Event> events = PublishSubject.create();

    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }

    public void startSingleUserQuery(@NonNull Input input) {

        RandomUserGeneratorInput generatorInput = validateInput(input);

        if (generatorInput != null) {
            events.onNext(new GenerateSingleUserEvent(generatorInput));
        }
    }

    public void startMultipleUserQuery(@NonNull Input input) {

        RandomUserGeneratorInput generatorInput = validateInput(input);

        if (generatorInput != null) {
            events.onNext(new GenerateManyUsersEvent(generatorInput));
        }
    }

    @Nullable
    private RandomUserGeneratorInput validateInput(Input input) {

        if (!input.queryMale && !input.queryFemale) {

            events.onNext(new UncheckedSexError());

            return null;
        } else {
            String queryGender = input.queryFemale ? "female" : "male";

            String nationality = input.nationality;

            if (input.nationality.trim().isEmpty()) {
                nationality = null;
            }

            return new RandomUserGeneratorInput(
                    nationality,
                    queryGender
            );
        }

    }

    public static class Input {
        public boolean queryFemale;
        public boolean queryMale;

        @NonNull
        public String nationality;

        public Input(boolean queryFemale, boolean queryMale, @NonNull String nationality) {
            this.queryFemale = queryFemale;
            this.queryMale = queryMale;
            this.nationality = nationality;
        }
    }


    public interface Event {
        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visit(@NonNull UncheckedSexError error);

            void visit(@NonNull GenerateSingleUserEvent event);

            void visit(@NonNull GenerateManyUsersEvent event);
        }
    }

    static class UncheckedSexError implements Event {

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

    static class GenerateSingleUserEvent implements Event {

        @NonNull
        public final RandomUserGeneratorInput input;

        public GenerateSingleUserEvent(@NonNull RandomUserGeneratorInput input) {
            this.input = input;
        }

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

    static class GenerateManyUsersEvent implements Event {

        @NonNull
        public final RandomUserGeneratorInput input;

        public GenerateManyUsersEvent(@NonNull RandomUserGeneratorInput input) {
            this.input = input;
        }

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

}
