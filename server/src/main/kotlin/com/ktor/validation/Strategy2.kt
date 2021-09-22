package com.ktor.validation

import arrow.*
import arrow.core.Either
import arrow.core.EitherPartialOf
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.Validated
import arrow.core.ValidatedPartialOf
import arrow.core.nel
import arrow.typeclasses.ApplicativeError
import arrow.core.extensions.validated.applicativeError.applicativeError
import arrow.core.extensions.either.applicativeError.applicativeError
import arrow.core.extensions.nonemptylist.semigroup.semigroup

sealed class ValidationError2(val msg: String) {
    data class DoesNotContain(val value: String) : ValidationError2("Did not contain $value")
    data class MaxLength(val value: Int) : ValidationError2("Exceeded length of $value")
    data class NotAnEmail(val reasons: Nel<ValidationError2>) : ValidationError2("Not a valid email")
}

data class FormField(val label: String, val value: String)
data class Email(val value: String)

sealed class Rules<F>(A: ApplicativeError<F, Nel<ValidationError2>>) : ApplicativeError<F, Nel<ValidationError2>> by A {

    private fun FormField.contains(needle: String): Kind<F, FormField> =
        if (value.contains(needle, false)) just(this)
        else raiseError(ValidationError2.DoesNotContain(needle).nel())

    private fun FormField.maxLength(maxLength: Int): Kind<F, FormField> =
        if (value.length <= maxLength) just(this)
        else raiseError(ValidationError2.MaxLength(maxLength).nel())

    fun FormField.validateEmail(): Kind<F, Email> =
        map(contains("@"), maxLength(250), {
            Email(value)
        }).handleErrorWith { raiseError(ValidationError2.NotAnEmail(it).nel()) }

    object ErrorAccumulationStrategy :
        Rules<ValidatedPartialOf<Nel<ValidationError2>>>(Validated.applicativeError(NonEmptyList.semigroup()))

    object FailFastStrategy :
        Rules<EitherPartialOf<Nel<ValidationError2>>>(Either.applicativeError())

    companion object {
        infix fun <A> failFast(f: FailFastStrategy.() -> A): A = f(FailFastStrategy)
        infix fun <A> accumulateErrors(f: ErrorAccumulationStrategy.() -> A): A = f(ErrorAccumulationStrategy)
    }
}